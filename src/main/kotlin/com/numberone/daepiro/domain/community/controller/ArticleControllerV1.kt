package com.numberone.daepiro.domain.community.controller

import com.numberone.daepiro.domain.community.api.ArticleApiV1
import com.numberone.daepiro.domain.community.dto.request.UpsertArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleDetailResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.domain.community.service.ArticleService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
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

    override fun getArticle(id: Long): ApiResult<ArticleDetailResponse> {
        return ApiResult.ok(data = articleService.getOne(id), "/v1/articles" )
    }
}
