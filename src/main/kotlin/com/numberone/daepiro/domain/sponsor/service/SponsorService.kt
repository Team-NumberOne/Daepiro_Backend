package com.numberone.daepiro.domain.sponsor.service

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ArticleCategory
import com.numberone.daepiro.domain.community.entity.ArticleType
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.sponsor.dto.request.CreateSponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.response.SponsorResponse
import com.numberone.daepiro.global.dto.ApiResult
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
}
