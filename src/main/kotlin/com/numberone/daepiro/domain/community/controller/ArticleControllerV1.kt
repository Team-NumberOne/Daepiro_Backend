package com.numberone.daepiro.domain.community.controller

import com.numberone.daepiro.domain.community.api.ArticleApiV1
import com.numberone.daepiro.domain.community.dto.request.CreateArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.domain.community.service.ArticleService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController(
    private val articleService: ArticleService,
) : ArticleApiV1 {
    override fun createArticle(
        request: CreateArticleRequest,
    ): ApiResult<ArticleSimpleResponse> {
        val response = articleService.createOne(
            request = request,
            userId = SecurityContextUtils.getUserId()
        )
        return ApiResult.ok(data = response, path = "/v1/articles")
    }
}
