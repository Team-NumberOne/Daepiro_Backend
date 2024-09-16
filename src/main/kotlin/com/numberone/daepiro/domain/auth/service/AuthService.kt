package com.numberone.daepiro.domain.auth.service

import com.numberone.daepiro.domain.auth.dto.request.AdminLoginRequest
import com.numberone.daepiro.domain.auth.dto.request.RefreshTokenRequest
import com.numberone.daepiro.domain.auth.dto.request.SocialLoginRequest
import com.numberone.daepiro.domain.auth.dto.response.TokenResponse
import com.numberone.daepiro.domain.auth.enums.TokenType
import com.numberone.daepiro.domain.auth.utils.JwtUtils
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.global.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_USER
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val useRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${jwt.secret-key}") private val secretKey: String,
    @Value("\${jwt.access-token-expire}") private val accessTokenExpire: Long,
    @Value("\${jwt.refresh-token-expire}") private val refreshTokenExpire: Long,
    @Value("\${jwt.admin-token-expire}") private val adminTokenExpire: Long
) {
    fun kakaoLogin(
        request: SocialLoginRequest
    ): ResponseEntity<ApiResult<TokenResponse>> {
    }

    fun naverLogin(
        request: SocialLoginRequest
    ): ResponseEntity<ApiResult<TokenResponse>> {
    }

    fun appleLogin(
        request: SocialLoginRequest
    ): ResponseEntity<ApiResult<TokenResponse>> {
    }

    fun adminLogin(
        request: AdminLoginRequest
    ): ResponseEntity<ApiResult<TokenResponse>> {

    }

    fun refreshToken(
        request: RefreshTokenRequest
    ): ResponseEntity<ApiResult<TokenResponse>> {
        val tokenInfo = JwtUtils.extractInfoFromToken(request.refreshToken, secretKey)
        val user = useRepository.findById(tokenInfo.id)
            .orElseThrow { throw CustomException(NOT_FOUND_USER) }

        return ResponseEntity.ok(
            ApiResult.ok(
                TokenResponse.of(user, secretKey, accessTokenExpire, refreshTokenExpire)
            )
        )
    }

}
