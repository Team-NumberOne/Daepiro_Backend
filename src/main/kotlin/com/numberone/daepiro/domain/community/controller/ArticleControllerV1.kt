package com.numberone.daepiro.domain.community.controller

import com.numberone.daepiro.domain.community.api.ArticleApiV1
import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.request.ReportRequest
import com.numberone.daepiro.domain.community.dto.request.UpdateArticleRequest
import com.numberone.daepiro.domain.community.dto.request.UpsertArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleDetailResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleLikeResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.domain.community.service.ArticleService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class ArticleController(
    private val articleService: ArticleService,
) : ArticleApiV1 {
    override fun createArticle(
        request: UpsertArticleRequest,
        attachFileList: List<MultipartFile>?
    ): ApiResult<ArticleSimpleResponse> {
        val response = articleService.createOne(
            request = request,
            attachFileList = attachFileList,
            userId = SecurityContextUtils.getUserId()
        )
        return ApiResult.ok(data = response, path = "/v1/articles")
    }

    override fun getArticle(articleId: Long): ApiResult<ArticleDetailResponse> {
        return ApiResult.ok(
            data = articleService.getOne(
                articleId = articleId,
                userId = SecurityContextUtils.getUserId()
            ), "/v1/articles"
        )
    }

    override fun updateArticle(
        id: Long,
        request: UpdateArticleRequest,
        attachFileList: List<MultipartFile>?
    ): ApiResult<ArticleSimpleResponse> {
        val response = articleService.upsertOne(
            id = id,
            request = request,
            attachFileList = attachFileList,
        )
        return ApiResult.ok(data = response, path = "/v1/articles/$id")
    }

    override fun getArticles(
        request: GetArticleRequest,
    ): ApiResult<Slice<ArticleListResponse>> {
        val response = articleService.getMulti(
            request = request,
            userId = SecurityContextUtils.getUserId()
        )
        return ApiResult.ok(data = response, path = "/v1/articles")
    }

    override fun like(id: Long): ApiResult<ArticleLikeResponse> {
        val response = articleService.like(
            userId = SecurityContextUtils.getUserId(),
            articleId = id,
        )
        return ApiResult.ok(data = response, path = "/v1/articles/$id/like")
    }

    override fun report(id: Long, request: ReportRequest): ApiResult<Unit> {
        articleService.report(
            userId = SecurityContextUtils.getUserId(),
            articleId = id,
            request = request,
        )
        return ApiResult.noContent(path = "/v1/articles/$id/report")
    }
}
