package com.numberone.daepiro.domain.user.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class CheckNicknameResponse(
    @Schema(description = "닉네임 사용 가능 여부", example = "true")
    val isAvailable: Boolean
) {
    companion object {
        fun of(
            isAvailable: Boolean
        ): CheckNicknameResponse {
            return CheckNicknameResponse(
                isAvailable = isAvailable
            )
        }
    }
}
