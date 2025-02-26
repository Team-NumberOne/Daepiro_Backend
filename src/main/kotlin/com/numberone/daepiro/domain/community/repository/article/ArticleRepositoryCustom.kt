package com.numberone.daepiro.domain.community.repository.article

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.mypage.dto.request.GetMyArticleRequest
import org.springframework.data.domain.Slice

interface ArticleRepositoryCustom {
    fun getArticles(
        request: GetArticleRequest,
        addressValues: List<Address>
    ): Slice<ArticleListResponse>

    fun getLatestArticles(
        request: GetArticleRequest,
        addressValues: List<Address>
    ): Slice<ArticleListResponse>

    fun getMyArticles(
        userId: Long,
        request: GetMyArticleRequest
    ): Slice<ArticleListResponse>
}
