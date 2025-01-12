package com.numberone.daepiro.domain.disasterSituation.service

import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.vo.AddressInfo
import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ArticleCategory
import com.numberone.daepiro.domain.community.entity.ArticleType
import com.numberone.daepiro.domain.community.entity.Comment
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.community.repository.article.findByIdOrThrow
import com.numberone.daepiro.domain.community.repository.comment.CommentRepository
import com.numberone.daepiro.domain.community.service.UserAddressVerifiedService
import com.numberone.daepiro.domain.disaster.entity.Disaster
import com.numberone.daepiro.domain.disasterSituation.dto.request.CreateSituationCommentRequest
import com.numberone.daepiro.domain.disasterSituation.dto.response.DisasterSituationResponse
import com.numberone.daepiro.domain.disasterSituation.dto.response.SituationCommentResponse
import com.numberone.daepiro.domain.user.repository.UserLikeRepository
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findAllLikedCommentId
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class DisasterSituationService(
    private val articleRepository: ArticleRepository,
    private val addressRepository: AddressRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val userAddressVerifiedService: UserAddressVerifiedService,
    private val userLikeRepository: UserLikeRepository
) {
    @Transactional
    fun createDisasterSituation(disasters: List<Disaster>) {
        val articles: List<Article> = disasters.map {
            Article.of(
                title = it.getTitle(),
                body = it.message,
                type = ArticleType.DISASTER,
                category = ArticleCategory.OTHER,
                visibility = false,
                disasterType = it.disasterType,
                address = it.address
            )
        }
        articleRepository.saveAll(articles)
    }

    fun getDisasterSituations(
        userId: Long
    ): ApiResult<List<DisasterSituationResponse>> {
        val user = userRepository.findByIdOrThrow(userId)
        val userAddresses = user.userAddresses.map { it.address }
        val addressIds = mutableSetOf<Long>()
        val typeIds = user.userDisasterTypes.map { it.disasterType.id!! }.toSet()

        for (address in userAddresses) {
            val addressInfo = AddressInfo.from(address)
            val parentAddressIds = addressRepository.findParentAddress(addressInfo)
                .map { it.id!! }
            addressIds.addAll(parentAddressIds + address.id!!)
        }

        val articles = articleRepository.findDisasterSituation(LocalDateTime.now().minusDays(1))

        return ApiResult.ok(articles.map {
            val commentEntities = commentRepository.findPopularComments(it.id!!)
            val comments = commentEntities.map { comment ->
                Pair(comment, listOf<Comment>())//todo 의미없이 pair를 썼음. 고쳐야함. 쩔수 없나.
            }

            DisasterSituationResponse.of(
                it,
                isReceivedForUser(it, addressIds, typeIds),
                comments,
                user
            )
        })
    }

    private fun isReceivedForUser(
        situation: Article,
        addressIds: Set<Long>,
        typeIds: Set<Long>
    ): Boolean {
        return addressIds.contains(situation.address!!.id) && typeIds.contains(situation.disasterType!!.id)
    }

    fun getComments(userId: Long, situationId: Long): ApiResult<List<SituationCommentResponse>> {
        val user = userRepository.findByIdOrThrow(userId)
        val comments = commentRepository.findParentComments(situationId)
            .map { Pair(it, commentRepository.findChildComments(it.id!!)) }
        val article = articleRepository.findByIdOrThrow(situationId)

        val result = comments
            .filter {
                !it.first.isDeleted() ||
                    it.second.any { !it.isDeleted() }
            }
            .map {
                SituationCommentResponse.of(
                    it.first,
                    user,
                    userAddressVerifiedService.getVerifiedOne(user.id!!, article.address!!.id!!),
                    it.second,
                    it.second.associate {
                        it.id!! to userAddressVerifiedService.getVerifiedOne(
                            it.authUser!!.id!!,
                            article.address!!.id!!
                        )
                    }
                )
            }
        val likedCommentIdSet = userLikeRepository.findAllLikedCommentId(userId)
        result.forEach {
            it.isLiked = likedCommentIdSet.contains(it.id)
            it.childComments.forEach { childComment ->
                childComment.isLiked = likedCommentIdSet.contains(childComment.id)
            }
        }
        return ApiResult.ok(result)
    }
}
