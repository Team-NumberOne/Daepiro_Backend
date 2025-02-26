package com.numberone.daepiro.domain.sponsor.service

import com.numberone.daepiro.domain.community.dto.request.ReportRequest
import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ArticleCategory
import com.numberone.daepiro.domain.community.entity.ArticleType
import com.numberone.daepiro.domain.community.entity.ReportedDocument
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.community.repository.reported.ReportedDocumentRepository
import com.numberone.daepiro.domain.community.repository.reported.isAlreadyReportedCheering
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import com.numberone.daepiro.domain.disaster.repository.DisasterTypeRepository
import com.numberone.daepiro.domain.notification.entity.NotificationCategory
import com.numberone.daepiro.domain.notification.service.NotificationService
import com.numberone.daepiro.domain.sponsor.dto.request.CheeringRequest
import com.numberone.daepiro.domain.sponsor.dto.request.CreateSponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.request.SponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.response.CheeringResponse
import com.numberone.daepiro.domain.sponsor.dto.response.SponsorResponse
import com.numberone.daepiro.domain.sponsor.entity.Cheering
import com.numberone.daepiro.domain.sponsor.repository.CheeringRepository
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_ARTICLE
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SponsorService(
    private val articleRepository: ArticleRepository,
    private val cheeringRepository: CheeringRepository,
    private val userRepository: UserRepository,
    private val disasterTypeRepository: DisasterTypeRepository,
    private val reportedDocumentRepository: ReportedDocumentRepository,
    private val notificationService: NotificationService
) {
    fun getSponsors(): ApiResult<List<SponsorResponse>> {
        val sponsors = articleRepository.findSponsorArticle()
            .map { SponsorResponse.of(it) }
        return ApiResult.ok(sponsors)
    }

    @Transactional
    fun createSponsorsArticle(
        request: CreateSponsorRequest
    ) {
        val sponsorArticle = Article.of(
            title = request.title,
            body = request.body,
            type = ArticleType.SPONSOR,
            category = ArticleCategory.OTHER,
            visibility = false,
            sponsorName = request.sponsorName,
            sponsorDescription = request.sponsorDescription,
            sponsorUrl = request.sponsorUrl,
            sponsorPostUrl = request.sponsorPostUrl,
            thumbnail = request.thumbnail,
            summary = request.summary,
            deadline = request.deadline,
            currentHeart = request.currentHeart,
            targetHeart = request.targetHeart,
            disasterType = disasterTypeRepository.findByType(DisasterType.DisasterValue.kor2code(request.disasterType))
                ?: throw CustomException(CustomErrorContext.NOT_FOUND_DISASTER_TYPE),
            subtitle = request.subtitle,
        )
        notificationService.sendNotification(
            users = userRepository.findAll(),
            title = "대피로에 새로운 후원이 열렸어요.",
            body = request.title,
            category = NotificationCategory.SPONSOR
        )
        articleRepository.save(sponsorArticle)
    }

    fun getSponsor(id: Long): ApiResult<SponsorResponse> {
        val sponsor = articleRepository.findById(id)
            .orElseThrow { CustomException(NOT_FOUND_ARTICLE) }
        if (sponsor.type != ArticleType.SPONSOR) {
            throw CustomException(NOT_FOUND_ARTICLE)
        }
        return ApiResult.ok(SponsorResponse.of(sponsor))
    }

    @Transactional
    fun sponsor(id: Long, request: SponsorRequest) {
        articleRepository.findById(id)
            .orElseThrow { CustomException(NOT_FOUND_ARTICLE) }
            .apply {
                increaseHeartCount(request.heart)
            }
    }

    @Transactional
    fun createCheering(request: CheeringRequest, userId: Long) {
        val user = userRepository.findByIdOrThrow(userId)
        val cheering = Cheering.of(user, request.content)

        cheeringRepository.save(cheering)
    }

    @Transactional
    fun updateCheering(id: Long, request: CheeringRequest, userId: Long) {
        val user = userRepository.findByIdOrThrow(userId)
        val cheering = cheeringRepository.findById(id)
            .orElseThrow { CustomException(CustomErrorContext.NOT_FOUND_CHEERING) }

        if (cheering.user.id != user.id) {
            throw CustomException(CustomErrorContext.NOT_CHEERING_AUTHOR)
        }
        cheering.updateContent(request.content)
    }

    fun getCheering(userId: Long): ApiResult<List<CheeringResponse>> {
        val cheeringList = cheeringRepository.findTop100ByOrderByCreatedAtDesc()
            .map { CheeringResponse.of(it, it.user.id == userId) }
        return ApiResult.ok(cheeringList)
    }

    fun getMessages(userId: Long): ApiResult<List<CheeringResponse>> {
        val cheeringList = cheeringRepository.findTop20ByOrderByCreatedAtDesc()
            .map { CheeringResponse.of(it, it.user.id == userId) }
        return ApiResult.ok(cheeringList)
    }

    @Transactional
    fun report(userId: Long, cheeringId: Long, request: ReportRequest) {
        val user = userRepository.findByIdOrThrow(userId)
        val cheering = cheeringRepository.findById(cheeringId)
            .orElseThrow { CustomException(CustomErrorContext.NOT_FOUND_CHEERING) }

        if (reportedDocumentRepository.isAlreadyReportedCheering(user, cheering)) {
            throw CustomException(CustomErrorContext.INVALID_VALUE, "해당 유저에 의해 이미 신고된 댓글입니다.")
        }

        reportedDocumentRepository.save(
            ReportedDocument.from(
                cheering = cheering,
                user = user,
                type = request.type,
                detail = request.detail,
                email = request.email,
            )
        )
    }

    @Transactional
    fun deleteCheering(id: Long, userId: Long) {
        val user = userRepository.findByIdOrThrow(userId)
        val cheering = cheeringRepository.findById(id)
            .orElseThrow { CustomException(CustomErrorContext.NOT_FOUND_CHEERING) }

        if (cheering.user.id != user.id) {
            throw CustomException(CustomErrorContext.NOT_CHEERING_AUTHOR)
        }
        cheeringRepository.delete(cheering)
    }
}
