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
            user: UserEntity,
            secretKey: String,
            accessTokenExpire: Long,
            refreshTokenExpire: Long
        ): TokenResponse {
            return TokenResponse(
                accessToken = JwtUtils.createToken(user, TokenType.ACCESS, accessTokenExpire, secretKey),
                refreshToken = JwtUtils.createToken(user, TokenType.REFRESH, refreshTokenExpire, secretKey)
            )
        }
    }
}
