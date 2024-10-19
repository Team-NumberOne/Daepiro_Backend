package com.numberone.daepiro.domain.community.api

import com.numberone.daepiro.domain.community.dto.request.CreateArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Article API", description = "게시글 관련 API")
@RequestMapping("/v1/articles")
interface ArticleApiV1 {
    @PostMapping
    @Operation(summary = "게시글 작성", description = "")
    fun createArticle(
        @RequestBody request: CreateArticleRequest
    ): ApiResult<ArticleSimpleResponse>
}
