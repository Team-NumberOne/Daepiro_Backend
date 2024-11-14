package com.numberone.daepiro.domain.disasterContent.dto.response

import com.numberone.daepiro.domain.dataCollecter.entity.News
import io.swagger.v3.oas.annotations.media.Schema

data class GetDisasterContentsResponse(
    @Schema(description = "다음 페이지를 조회하기 위한 cursor 값", example = "971")
    val nextCursor: Long?,

    val contents: List<DisasterContentResponse>
) {
    companion object {
        fun of(
            news: List<News>
        ): GetDisasterContentsResponse {
            if (news.isEmpty()) {
                return GetDisasterContentsResponse(
                    nextCursor = null,
                    contents = emptyList()
                )
            }
            return GetDisasterContentsResponse(
                nextCursor = news.last().id!! - 1,
                contents = news.map { DisasterContentResponse.of(it) }
            )
        }
    }
}
