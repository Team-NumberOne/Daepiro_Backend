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
import com.numberone.daepiro.domain.disaster.entity.Disaster
import com.numberone.daepiro.domain.disasterSituation.dto.request.CreateSituationCommentRequest
import com.numberone.daepiro.domain.disasterSituation.dto.response.DisasterSituationResponse
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class DisasterSituationService(
    val articleRepository: ArticleRepository,
    val addressRepository: AddressRepository,
    val userRepository: UserRepository,
    val commentRepository: CommentRepository
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
            DisasterSituationResponse.of(
                it,
                isReceivedForUser(it, addressIds, typeIds),
                listOf(),
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

    @Transactional
    fun createComment(situationId: Long, userId: Long, request: CreateSituationCommentRequest) {
        val user = userRepository.findByIdOrThrow(userId)
        val article = articleRepository.findByIdOrThrow(situationId)
        val comment = Comment.of(
            body = request.content,
            authUser = user,
            parentComment = null,
            article = article
        )

        commentRepository.save(comment)
    }
}
