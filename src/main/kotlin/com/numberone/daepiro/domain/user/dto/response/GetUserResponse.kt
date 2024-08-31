package com.numberone.daepiro.domain.user.dto.response

data class GetUserResponse(
    val id: Long,
    val name: String,
    val nickname: String
) {
    companion object {
        fun fake(): GetUserResponse {
            return GetUserResponse(
                id = 1L,
                name = "fake",
                nickname = "fake"
            )
        }
    }
}
