package com.numberone.daepiro.domain.auth.controller

import com.numberone.daepiro.domain.auth.api.AuthApiV1
import com.numberone.daepiro.domain.auth.dto.request.AdminLoginRequest
import com.numberone.daepiro.domain.auth.dto.request.RefreshTokenRequest
import com.numberone.daepiro.domain.auth.dto.request.SocialLoginRequest
import com.numberone.daepiro.domain.auth.dto.response.LoginResponse
import com.numberone.daepiro.domain.auth.dto.response.TokenResponse
import com.numberone.daepiro.domain.auth.service.AuthService
import com.numberone.daepiro.domain.user.enums.SocialPlatform
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.UNSUPPORTED_PLATFORM
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) : AuthApiV1 {
    override fun socialLogin(
        platform: String,
        request: SocialLoginRequest
    ): ApiResult<LoginResponse> {
        return when (platform) {
            SocialPlatform.KAKAO.path -> authService.kakaoLogin(request)
            SocialPlatform.NAVER.path -> authService.naverLogin(request)
            else -> throw CustomException(UNSUPPORTED_PLATFORM)
        }
    }

    override fun adminLogin(
        request: AdminLoginRequest
    ): ApiResult<TokenResponse> {
        return authService.adminLogin(request)
    }

    override fun refreshToken(
        request: RefreshTokenRequest
    ): ApiResult<TokenResponse> {
        return authService.refreshToken(request)
    }
}
