package com.numberone.daepiro.domain.auth.api

import com.numberone.daepiro.domain.auth.dto.request.AdminLoginRequest
import com.numberone.daepiro.domain.auth.dto.request.RefreshTokenRequest
import com.numberone.daepiro.domain.auth.dto.request.SocialLoginRequest
import com.numberone.daepiro.domain.auth.dto.response.TokenResponse
import com.numberone.daepiro.global.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Auth API", description = "인증 관련 API")
@RequestMapping("/v1/auth")
interface AuthApi {
    @PostMapping("/login/{platform}")
    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "소셜 로그인 성공",
            ),
            ApiResponse(
                responseCode = "201",
                description = "신규 유저로 소셜 로그인 성공"
            ),
            ApiResponse(
                responseCode = "400",
                description = "요청 형식 또는 값이 잘못됨"
            ),
            ApiResponse(
                responseCode = "500",
                description = "서버 오류"
            ),
        ]
    )
    fun socialLogin(
        @Schema(description = "소셜 로그인 플랫폼") @PathVariable platform: String,
        @RequestBody request: SocialLoginRequest
    ): ResponseEntity<ApiResult<Unit>>

    @PostMapping("/admin")
    @Operation(summary = "관리자 로그인", description = "관리자 로그인을 합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "관리자 로그인 성공",
            ),
            ApiResponse(
                responseCode = "400",
                description = "요청 형식 또는 값이 잘못됨"
            ),
            ApiResponse(
                responseCode = "500",
                description = "서버 오류"
            ),
        ]
    )
    fun adminLogin(
        @RequestBody request: AdminLoginRequest
    ): ResponseEntity<ApiResult<Unit>>

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "토큰을 갱신합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "토큰 갱신 성공",
            ),
            ApiResponse(
                responseCode = "400",
                description = "요청 형식 잘못됨"
            ),
            ApiResponse(
                responseCode = "500",
                description = "서버 오류"
            ),
        ]
    )
    fun refreshToken(
        @RequestBody request: RefreshTokenRequest
    ): ResponseEntity<ApiResult<TokenResponse>>
}
