package com.numberone.daepiro.domain.home.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class HomeDisasterFeed(
    @Schema(description = "재난 종류", example = "호우")
    val disasterType: String,

    @Schema(description = "제목", example = "서울시 성북구 쌍문동 호우 발생")
    val title: String,

    @Schema(description = "발생 시간", example = "2024-10-23T19:53:00")
    val time: LocalDateTime
)
