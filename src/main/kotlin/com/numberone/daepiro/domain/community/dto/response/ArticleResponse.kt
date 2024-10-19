package com.numberone.daepiro.domain.community.dto.response

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ArticleCategory
import com.numberone.daepiro.domain.community.entity.ArticleStatus
import com.numberone.daepiro.domain.community.entity.ArticleType
import java.time.LocalDateTime

data class ArticleSimpleResponse(
    val id: Long,
    val title: String,
    val body: String,
    val type: ArticleType,
    val category: ArticleCategory,
    val isLocationVisible: Boolean,
    val likeCount: Int,
    val viewCount: Int,
    val commentCount: Int,
    val reportCount: Int,
    val status: ArticleStatus,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(article: Article): ArticleSimpleResponse {
            val articleId = article.id

            require(articleId != null)

            return ArticleSimpleResponse(
                id = articleId,
                title = article.title,
                body = article.body,
                type = article.type,
                category = article.category,
                isLocationVisible = article.isLocationVisible,
                likeCount = article.likeCount,
                viewCount = article.viewCount,
                commentCount = article.commentCount,
                reportCount = article.reportCount,
                status = article.status,
                article.createdAt,
            )
        }
    }
}
