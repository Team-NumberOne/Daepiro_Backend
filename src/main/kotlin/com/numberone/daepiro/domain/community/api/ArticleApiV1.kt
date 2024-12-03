package com.numberone.daepiro.domain.community.api

import com.numberone.daepiro.domain.community.dto.request.UpsertArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleDetailResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Article API", description = "게시글 관련 API")
@RequestMapping("/v1/articles")
interface ArticleApiV1 {
    @Operation(
        summary = "게시글 작성",
        description = ""
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createArticle(
        @ModelAttribute request: UpsertArticleRequest,
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

    @GetMapping("{id}")
    fun getArticle(@PathVariable("id") id: Long): ApiResult<ArticleDetailResponse>

    @Operation(
        summary = "게시글 수정(Upsert)",
        description = "요청한 request spec 대로 게시글 데이터를 덮어씌웁니다."
    )
    @PostMapping(path = ["/{id}"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateArticle(
        @PathVariable(value = "id", required = true) id: Long,
        @ModelAttribute request: UpsertArticleRequest,
        @Parameter(
            description = "첨부파일 리스트 (이미지). 해당 아티클에 대해서 이미 첨부파일이 존재한다면, 모두 제거 후 새로 요청온 파일을 등록합니다.",
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
