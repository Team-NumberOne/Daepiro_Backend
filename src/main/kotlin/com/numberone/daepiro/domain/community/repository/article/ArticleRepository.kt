package com.numberone.daepiro.domain.community.repository.article

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ArticleType
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface ArticleRepository : JpaRepository<Article, Long>, ArticleRepositoryCustom {
    fun findByIdAndType(id: Long, type: ArticleType): Article?
}

fun ArticleRepository.findByIdOrThrow(id: Long): Article {
    return findByIdOrNull(id) ?: throw IllegalArgumentException("Article with id $id not found")
}

fun ArticleRepository.findDisasterSituationByIdOrThrow(id: Long): Article {
    return findByIdAndType(id, ArticleType.DISASTER)
        ?: throw CustomException(CustomErrorContext.NOT_FOUND_ARTICLE)
}
