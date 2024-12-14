package com.numberone.daepiro.domain.community.repository.article

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ArticleType
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface ArticleRepository : JpaRepository<Article, Long>, ArticleRepositoryCustom {
    @Query("SELECT a FROM Article a WHERE a.type = 'DISASTER' AND a.createdAt>:time")
    fun findDisasterSituation(@Param("time") time: LocalDateTime): List<Article>
}

fun ArticleRepository.findByIdOrThrow(id: Long): Article {
    return findByIdOrNull(id) ?: throw IllegalArgumentException("Article with id $id not found")
}
