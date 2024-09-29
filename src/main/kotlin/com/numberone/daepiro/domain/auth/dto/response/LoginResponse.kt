package com.numberone.daepiro.domain.auth.dto.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val isCompletedOnboarding: Boolean
) {
    companion object {
        fun of(
            accessToken: String,
            refreshToken: String,
            isCompletedOnboarding: Boolean
        ): LoginResponse {
            return LoginResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                isCompletedOnboarding = isCompletedOnboarding
            )
        }
    }
}
