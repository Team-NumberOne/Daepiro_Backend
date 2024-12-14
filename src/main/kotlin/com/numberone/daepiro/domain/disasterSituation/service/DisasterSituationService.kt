package com.numberone.daepiro.domain.disasterSituation.service

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ArticleCategory
import com.numberone.daepiro.domain.community.entity.ArticleType
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.disaster.entity.Disaster
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import com.numberone.daepiro.domain.disaster.repository.DisasterTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DisasterSituationService(
    val articleRepository: ArticleRepository,
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
}
