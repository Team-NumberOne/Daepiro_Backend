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
    @Query("SELECT a FROM Article a WHERE a.type = 'DISASTER' AND a.createdAt>:time ORDER BY a.createdAt DESC")
    fun findDisasterSituation(@Param("time") time: LocalDateTime): List<Article>

    @Query("SELECT a FROM Article a WHERE a.type = 'SPONSOR' AND ( a.deadline > CURRENT_TIMESTAMP OR a.deadline IS NULL ) ORDER BY a.createdAt ASC")
    fun findSponsorArticle(): List<Article>

    @Query("SELECT a FROM Article a WHERE a.type = 'ANNOUNCEMENT' ORDER BY a.createdAt DESC")
    fun findAllAnnouncement(): List<Article>

    @Query("SELECT a FROM Article a WHERE a.type = 'ANNOUNCEMENT' AND a.id = :id ORDER BY a.createdAt DESC")
    fun findAnnouncementById(@Param("id") id: Long): Article

    @Query(
        "SELECT * FROM article WHERE type = 'ANNOUNCEMENT' AND id < :id ORDER BY created_at DESC LIMIT 3",
        nativeQuery = true
    )
    fun findNextAnnouncement(@Param("id") id: Long): List<Article>

}

fun ArticleRepository.findByIdOrThrow(id: Long): Article {
    return findByIdOrNull(id) ?: throw CustomException(CustomErrorContext.NOT_FOUND_ARTICLE)
}
