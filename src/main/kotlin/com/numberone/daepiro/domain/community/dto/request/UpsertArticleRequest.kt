package com.numberone.daepiro.domain.community.dto.request

import com.numberone.daepiro.domain.community.entity.ArticleCategory
import com.numberone.daepiro.domain.community.entity.ArticleType
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject

@ParameterObject
@Schema(description = "게시글 생성 request dto")
data class UpsertArticleRequest(

    @Schema(description = "게시글 타입. <br>-동네생활: DONGNE<br>-정보: INFORMATION ", example = "DONGNE")
    val articleType: ArticleType,

    @Schema(description = "게시글 카테고리: <br>-일상: LIFE<br>-교통: TRAFFIC<br>-안전: SAFE<br>기타: OTHER", example = "LIFE")
    val articleCategory: ArticleCategory,

    @Schema(description = "현위치 포함 여부", example = "false")
    val visibility: Boolean,

    @Schema(description = "게시글 제목", example = "제목")
    val title: String,

    @Schema(description = "게시글 내용", example = "내용")
    val body: String,
)

@Schema(description = "게시글 생성 request dto")
data class UpdateArticleRequest(
    @Schema(description = "게시글 타입. <br>-동네생활: DONGNE<br>-정보: INFORMATION ", example = "DONGNE")
    val articleType: ArticleType? = null,

    @Schema(description = "게시글 카테고리: <br>-일상: LIFE<br>-교통: TRAFFIC<br>-안전: SAFE<br>기타: OTHER", example = "LIFE")
    val articleCategory: ArticleCategory? = null,

    @Schema(description = "현위치 포함 여부", example = "false")
    val visibility: Boolean? = null,

    @Schema(description = "게시글 제목", example = "제목")
    val title: String? = null,

    @Schema(description = "게시글 내용", example = "내용")
    val body: String? = null,
)
