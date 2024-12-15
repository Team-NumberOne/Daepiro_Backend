package com.numberone.daepiro.domain.community.api

import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.request.ReportRequest
import com.numberone.daepiro.domain.community.dto.request.UpdateArticleRequest
import com.numberone.daepiro.domain.community.dto.request.UpsertArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleDetailResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleLikeResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleSimpleResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Slice
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Article API", description = "게시글 관련 API")
@RequestMapping("/v1/articles")
interface ArticleApiV1 {
    @Operation(
        summary = "게시글 작성",
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createArticle(
        @ModelAttribute request: UpsertArticleRequest,
        @Parameter(
            description = "첨부파일 리스트 (이미지)",
            content = [Content(
                mediaType = "image/*",
                array = ArraySchema(
                    schema = Schema(
                        type = "string",
                        format = "binary"
                    )
                )
            )]
        )
        @RequestPart(required = false) attachFileList: List<MultipartFile>?
    ): ApiResult<ArticleSimpleResponse>

    @Operation(
        summary = "게시글 단건 조회",
    )
    @GetMapping("{id}")
    fun getArticle(@PathVariable("id") id: Long): ApiResult<ArticleDetailResponse>

    @Operation(
        summary = "게시글 수정(Upsert)",
        description = "요청한 request spec 에서, null 이 아닌 데이터 요청에 대해서는 게시글 데이터를 update 합니다."
    )
    @PostMapping(path = ["/{id}"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateArticle(
        @PathVariable(value = "id", required = true) id: Long,
        @ModelAttribute request: UpdateArticleRequest,
        @Parameter(
            description = "첨부파일 리스트 (이미지). 해당 아티클에 대해서 이미 첨부파일이 존재한다면, 모두 제거 후 새로 요청온 파일을 등록합니다.",
            content = [Content(
                mediaType = "image/*",
                array = ArraySchema(
                    schema = Schema(
                        type = "string",
                        format = "binary"
                    )
                )
            )]
        )
        @RequestPart(required = false) attachFileList: List<MultipartFile>?
    ): ApiResult<ArticleSimpleResponse>


    @Operation(
        summary = "게시글 리스트 조회 (무한 스크롤) @see Figma. 2.1",
    )
    @GetMapping
    fun getArticles(
        @ModelAttribute request: GetArticleRequest,
    ): ApiResult<Slice<ArticleListResponse>>

    @Operation(
        summary = "게시글 좋아요 추가/취소"
    )
    @PutMapping("/{id}/like")
    fun like(
        @PathVariable("id") id: Long,
    ): ApiResult<ArticleLikeResponse>

    @Operation(
        summary = "신고하기"
    )
    @PutMapping("/{id}/report")
    fun report(
        @PathVariable("id") id: Long,
        @RequestBody request: ReportRequest,
    ): ApiResult<Unit>

}
