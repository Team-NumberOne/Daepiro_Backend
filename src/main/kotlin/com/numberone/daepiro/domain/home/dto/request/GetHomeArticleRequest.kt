package com.numberone.daepiro.domain.home.dto.request

import com.numberone.daepiro.domain.community.entity.ArticleCategory
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject

@ParameterObject
data class GetHomeArticleRequest(
    @Schema(description = "게시글 카테고리. <br>- LIFE: 일상 <br>- TRAFFIC: 교통 <br>- SAFE: 안전 <br>- OTHER: 기타", example = "LIFE")
    val category: ArticleCategory? = null,
)
