package com.numberone.daepiro.domain.dataCollecter.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class GetLatestDisasterResponse(
    @Schema(description = "재난문자 ID", example = "1")
    val messageId: Long
) {
    companion object {
        fun from(
            messageId: Long
        ): GetLatestDisasterResponse {
            return GetLatestDisasterResponse(
                messageId = messageId
            )
        }
    }
}
