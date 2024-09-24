package com.numberone.daepiro.domain.auth.dto.response

import com.numberone.daepiro.domain.auth.enums.TokenType
import com.numberone.daepiro.domain.auth.utils.JwtUtils
import com.numberone.daepiro.domain.user.entity.UserEntity

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun of(
            accessToken: String,
            refreshToken: String
        ): TokenResponse {
            return TokenResponse(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }
}
