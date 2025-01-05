package com.numberone.daepiro.domain.sponsor.service

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ArticleCategory
import com.numberone.daepiro.domain.community.entity.ArticleType
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.sponsor.dto.request.CreateSponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.request.SponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.response.SponsorResponse
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
            thumbnail = request.thumbnail,
            summary = request.summary,
            deadline = request.deadline,
            currentHeart = request.currentHeart,
            targetHeart = request.targetHeart
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
}
