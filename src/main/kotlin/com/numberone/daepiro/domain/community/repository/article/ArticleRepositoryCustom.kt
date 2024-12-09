package com.numberone.daepiro.domain.community.repository.article

import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import org.springframework.data.domain.Slice

interface ArticleRepositoryCustom {
    fun getArticles(
        request: GetArticleRequest,
        siDo: String? = null,
        siGunGu: String? = null,
    ): Slice<ArticleListResponse>
}
