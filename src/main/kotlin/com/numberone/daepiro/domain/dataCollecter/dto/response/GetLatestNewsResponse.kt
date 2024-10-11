package com.numberone.daepiro.domain.dataCollecter.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class GetLatestNewsResponse(
    @Schema(description = "뉴스 발행 시간", example = "2021-08-01T00:00:00")
    val publishedAt: LocalDateTime
) {
    companion object {
        fun from(
            publishedAt: LocalDateTime
        ): GetLatestNewsResponse {
            return GetLatestNewsResponse(
                publishedAt = publishedAt
            )
        }
    }
}
