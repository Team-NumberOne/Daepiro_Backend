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

@Service
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
    fun kakaoLogin(
        request: SocialLoginRequest
    ): ApiResult<TokenResponse> {
        val userInfo = kakaoFeign.getUserInfo(JwtUtils.PREFIX_BEARER + request.socialToken)
            ?: throw CustomException(INVALID_SOCIAL_TOKEN)
        val socialId = userInfo.id.toString()
        val user = userRepository.findBySocialIdAndPlatform(socialId, SocialPlatform.KAKAO)
            ?: userRepository.save(UserEntity.of(SocialPlatform.KAKAO, socialId))

        return ApiResult.ok(TokenResponse.of(user, secretKey, accessTokenExpire, refreshTokenExpire))
    }

    fun naverLogin(
        request: SocialLoginRequest
    ): ApiResult<TokenResponse> {
        val userInfo = naverFeign.getUserInfo(JwtUtils.PREFIX_BEARER + request.socialToken)
            ?: throw CustomException(INVALID_SOCIAL_TOKEN)
        val socialId = userInfo.response.id
        val user = userRepository.findBySocialIdAndPlatform(socialId, SocialPlatform.NAVER)
            ?: userRepository.save(UserEntity.of(SocialPlatform.NAVER, socialId))

        return ApiResult.ok(TokenResponse.of(user, secretKey, accessTokenExpire, refreshTokenExpire))
    }

    fun adminLogin(
        request: AdminLoginRequest
    ): ApiResult<TokenResponse> {
        val user = userRepository.findByUsername(request.username)
            ?: throw CustomException(NOT_FOUND_USER)
        require(user.passwordLoginInformation != null)
        if (!passwordEncoder.matches(request.password, user.passwordLoginInformation!!.password))
            throw CustomException(INVALID_PASSWORD)

        return ApiResult.ok(TokenResponse.of(user, secretKey, adminTokenExpire, adminTokenExpire))
    }

    fun refreshToken(
        request: RefreshTokenRequest
    ): ApiResult<TokenResponse> {
        // todo redis에서 refresh token 관리하도록 변경
        val tokenInfo = JwtUtils.extractInfoFromToken(request.refreshToken, secretKey)
        if (tokenInfo.type != TokenType.REFRESH)
            throw CustomException(INVALID_TOKEN)
        val user = userRepository.findByIdOrNull(tokenInfo.id)
            ?: throw CustomException(NOT_FOUND_USER)

        return ApiResult.ok(TokenResponse.of(user, secretKey, accessTokenExpire, refreshTokenExpire))
    }
}
