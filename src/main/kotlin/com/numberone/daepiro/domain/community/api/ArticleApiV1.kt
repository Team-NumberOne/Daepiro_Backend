package com.numberone.daepiro.domain.community.api

import com.numberone.daepiro.domain.community.dto.request.CreateArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Article API", description = "게시글 관련 API")
@RequestMapping("/v1/articles")
interface ArticleApiV1 {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "게시글 작성", description = "")
    fun createArticle(
        @ModelAttribute request: CreateArticleRequest,
        @Parameter(
            description = "첨부파일 리스트 (이미지)",
            content = [Content(
                mediaType = "image/*",
                array = ArraySchema(schema = Schema(
                    type = "string",
                    format = "binary"
                ))
            )]
        )
        @RequestPart(required = false) attachFileList: List<MultipartFile>?
    ): ApiResult<ArticleSimpleResponse>
}
