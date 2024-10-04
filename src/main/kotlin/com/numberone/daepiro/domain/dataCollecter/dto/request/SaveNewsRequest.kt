package com.numberone.daepiro.domain.dataCollecter.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class SaveNewsRequest(
    val news: List<NewsRequest>
)

data class NewsRequest(
    @Schema(description = "뉴스 제목", example = "제목")
    val title: String,

    @Schema(description = "뉴스 발행 시간", example = "2021-08-01T00:00:00")
    val publishedAt: LocalDateTime,

    @Schema(description = "뉴스 부제목", example = "부제목")
    val subtitle: String,

    @Schema(description = "뉴스 본문", example = "본문")
    val body: String,

    @Schema(description = "뉴스 출처", example = "출처")
    val thumbnailUrl: String,
)
