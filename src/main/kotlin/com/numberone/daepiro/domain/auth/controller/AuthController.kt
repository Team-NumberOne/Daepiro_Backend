package com.numberone.daepiro.domain.auth.controller

import com.numberone.daepiro.domain.auth.api.AuthApi
import com.numberone.daepiro.domain.auth.dto.request.AdminLoginRequest
import com.numberone.daepiro.domain.auth.dto.request.RefreshTokenRequest
import com.numberone.daepiro.domain.auth.dto.request.SocialLoginRequest
import com.numberone.daepiro.domain.auth.dto.response.TokenResponse
import com.numberone.daepiro.domain.auth.service.AuthService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.UNSUPPORTED_PLATFORM
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController


@RestController
class AuthController(
    private val authService: AuthService
) : AuthApi {
    override fun socialLogin(
        platform: String,
        request: SocialLoginRequest
    ): ResponseEntity<ApiResult<TokenResponse>> {
        return when (platform) {
            "kakao" -> authService.kakaoLogin(request)
                .toResponseEntity()

            "naver" -> authService.naverLogin(request)
                .toResponseEntity()

            else -> throw CustomException(UNSUPPORTED_PLATFORM)
        }
    }

    override fun adminLogin(
        request: AdminLoginRequest
    ): ResponseEntity<ApiResult<TokenResponse>> {
        return authService.adminLogin(request)
            .toResponseEntity()
    }

    override fun refreshToken(
        request: RefreshTokenRequest
    ): ResponseEntity<ApiResult<TokenResponse>> {
        return authService.refreshToken(request)
            .toResponseEntity()
    }
}
