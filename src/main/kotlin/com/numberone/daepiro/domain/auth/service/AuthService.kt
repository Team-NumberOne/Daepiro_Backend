package com.numberone.daepiro.domain.auth.service

import com.numberone.daepiro.domain.auth.dto.request.AdminLoginRequest
import com.numberone.daepiro.domain.auth.dto.request.RefreshTokenRequest
import com.numberone.daepiro.domain.auth.dto.request.SocialLoginRequest
import com.numberone.daepiro.domain.auth.dto.response.TokenResponse
import com.numberone.daepiro.domain.auth.enums.TokenType
import com.numberone.daepiro.domain.auth.utils.JwtUtils
import com.numberone.daepiro.domain.user.entity.UserEntity
import com.numberone.daepiro.domain.user.enums.SocialPlatform
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_PASSWORD
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_SOCIAL_TOKEN
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_TOKEN
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_USER
import com.numberone.daepiro.global.exception.CustomException
import com.numberone.daepiro.global.feign.KakaoFeign
import com.numberone.daepiro.global.feign.NaverFeign
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val kakaoFeign: KakaoFeign,
    private val naverFeign: NaverFeign,
    @Value("\${jwt.secret-key}") private val secretKey: String,
    @Value("\${jwt.access-token-expire}") private val accessTokenExpire: Long,
    @Value("\${jwt.refresh-token-expire}") private val refreshTokenExpire: Long,
    @Value("\${jwt.admin-token-expire}") private val adminTokenExpire: Long
) {
    @Transactional
    fun kakaoLogin(
        request: SocialLoginRequest
    ): ApiResult<TokenResponse> {
        val userInfo = kakaoFeign.getUserInfo(JwtUtils.PREFIX_BEARER + request.socialToken)
            ?: throw CustomException(INVALID_SOCIAL_TOKEN)
        val socialId = userInfo.id.toString()
        val user = getOrCreateUser(socialId, SocialPlatform.KAKAO)

        return ApiResult.ok(
            getTokenResponse(
                user,
                accessTokenExpire,
                refreshTokenExpire
            )
        )
    }

    @Transactional
    fun naverLogin(
        request: SocialLoginRequest
    ): ApiResult<TokenResponse> {
        val userInfo = naverFeign.getUserInfo(JwtUtils.PREFIX_BEARER + request.socialToken)
            ?: throw CustomException(INVALID_SOCIAL_TOKEN)
        val socialId = userInfo.response.id
        val user = getOrCreateUser(socialId, SocialPlatform.NAVER)

        return ApiResult.ok(
            getTokenResponse(
                user,
                accessTokenExpire,
                refreshTokenExpire
            )
        )
    }

    fun adminLogin(
        request: AdminLoginRequest
    ): ApiResult<TokenResponse> {
        val user = userRepository.findByUsername(request.username)
            ?: throw CustomException(NOT_FOUND_USER)

        validatePassword(request.password, user)
        return ApiResult.ok(
            getTokenResponse(
                user,
                adminTokenExpire,
                adminTokenExpire
            )
        )
    }

    fun refreshToken(
        request: RefreshTokenRequest
    ): ApiResult<TokenResponse> {
        // todo redis에서 refresh token 관리하도록 변경
        val tokenInfo = JwtUtils.extractInfoFromToken(request.refreshToken, secretKey)
        if (tokenInfo.type != TokenType.REFRESH)
            throw CustomException(INVALID_TOKEN)
        val user = userRepository.findByIdOrThrow(tokenInfo.id)

        return ApiResult.ok(
            getTokenResponse(
                user,
                accessTokenExpire,
                refreshTokenExpire
            )
        )
    }

    private fun getTokenResponse(
        user: UserEntity,
        accessExpire: Long,
        refreshExpire: Long
    ): TokenResponse {
        val accessToken = JwtUtils.createToken(user, TokenType.ACCESS, accessExpire, secretKey)
        val refreshToken = JwtUtils.createToken(user, TokenType.REFRESH, refreshExpire, secretKey)

        return TokenResponse.of(accessToken, refreshToken)
    }

    private fun getOrCreateUser(
        socialId: String,
        platform: SocialPlatform
    ): UserEntity {
        return userRepository.findBySocialIdAndPlatform(socialId, platform)
            ?: userRepository.save(UserEntity.of(platform, socialId))
    }

    private fun validatePassword(
        password: String,
        user: UserEntity
    ) {
        val passwordLoginInformation = user.passwordLoginInformation
            ?: throw CustomException(INVALID_PASSWORD)

        if (!passwordEncoder.matches(password, passwordLoginInformation.password))
            throw CustomException(INVALID_PASSWORD)
    }
}
