package com.numberone.daepiro.domain.community.dto.request

import com.numberone.daepiro.domain.community.entity.ArticleCategory
import com.numberone.daepiro.domain.community.entity.ArticleType
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.multipart.MultipartFile

@ParameterObject
@Schema(description = "게시글 생성 request dto")
data class CreateArticleRequest(
    @Schema(description = "게시글 타입. <br>-동네생활: DONGNE<br>-정보: INFORMATION ", example = "DONGNE")
    val articleType: ArticleType,
    @Schema(description = "게시글 카테고리: <br>-일상: LIFE<br>-교통: TRAFFIC<br>-안전: SAFE<br>기타: OTHER", example = "LIFE")
    val articleCategory: ArticleCategory,
    @Schema(description = "현위치 포함 여부", example = "false")
    val isLocationVisible: Boolean,
    @Schema(description = "게시글 제목", example = "제목")
    val title: String,
    @Schema(description = "게시글 내용", example = "내용")
    val body: String,
    @Schema(description = "시", example = "서울특별시")
    val si: String,
    @Schema(description = "구", example = "강동구")
    val gu: String,
    @Schema(description = "동", example = "천호동")
    val dong: String,
    @Schema(description = "첨부파일 리스트")
    val attachFileList: List<MultipartFile>?
)
