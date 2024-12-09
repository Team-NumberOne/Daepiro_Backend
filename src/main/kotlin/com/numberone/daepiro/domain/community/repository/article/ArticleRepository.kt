package com.numberone.daepiro.domain.community.repository.article

import com.numberone.daepiro.domain.community.entity.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface ArticleRepository : JpaRepository<Article, Long>, ArticleRepositoryCustom

fun ArticleRepository.findByIdOrThrow(id: Long): Article {
    return findByIdOrNull(id) ?: throw IllegalArgumentException("Article with id $id not found")
}
