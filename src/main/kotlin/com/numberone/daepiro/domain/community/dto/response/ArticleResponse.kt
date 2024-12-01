package com.numberone.daepiro.domain.community.dto.response

import com.numberone.daepiro.domain.community.entity.Article

data class ArticleSimpleResponse(
    val id: Long,
) {
    companion object {
        fun from(
            article: Article,
        ): ArticleSimpleResponse {
            return ArticleSimpleResponse(
                id = article.id!!,
            )
        }
    }
}
