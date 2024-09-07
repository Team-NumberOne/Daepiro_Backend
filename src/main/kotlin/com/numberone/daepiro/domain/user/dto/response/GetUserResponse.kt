package com.numberone.daepiro.domain.user.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class GetUserResponse(
    @Schema(description = "primary key", example = "1")
    val id: Long,
    @Schema(description = "realname", example = "실명")
    val realname: String,
    @Schema(description = "nickname", example = "닉네임")
    val nickname: String
) {
    companion object {
        fun fake(): GetUserResponse {
            return GetUserResponse(
                id = 1L,
                realname = "fake",
                nickname = "fake"
            )
        }
    }
}
