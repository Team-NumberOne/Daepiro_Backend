package com.numberone.daepiro.domain.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.numberone.daepiro.domain.auth.dto.request.AdminLoginRequest
import com.numberone.daepiro.domain.auth.dto.request.RefreshTokenRequest
import com.numberone.daepiro.domain.auth.dto.request.SocialLoginRequest
import com.numberone.daepiro.domain.auth.dto.response.LoginResponse
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
import com.numberone.daepiro.global.feign.AppleFeign
import com.numberone.daepiro.global.feign.KakaoAuthFeign
import com.numberone.daepiro.global.feign.NaverFeign
import io.jsonwebtoken.Jwts
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.PrivateKey
import java.security.Security
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@Service
@Transactional(readOnly = true)
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val kakaoAuthFeign: KakaoAuthFeign,
    private val naverFeign: NaverFeign,
    private val appleFeign: AppleFeign,
    @Value("\${jwt.secret-key}") private val secretKey: String,
    @Value("\${jwt.access-token-expire}") private val accessTokenExpire: Long,
    @Value("\${jwt.refresh-token-expire}") private val refreshTokenExpire: Long,
    @Value("\${jwt.admin-token-expire}") private val adminTokenExpire: Long,
    @Value("\${apple.client-id}") private val appleClientId: String,
    @Value("\${apple.grant-type}") private val appleGrantType: String,
    @Value("\${apple.redirect-uri}") private val appleRedirectUri: String,
    @Value("\${apple.team-id}") private val appleTeamId: String,
    @Value("\${apple.key-id}") private val appleKeyId: String,
    @Value("\${apple.private-key}") private val applePrivateKey: String,


    ) {
    @Transactional
    fun kakaoLogin(
        request: SocialLoginRequest
    ): ApiResult<LoginResponse> {
        val userInfo = kakaoAuthFeign.getUserInfo(JwtUtils.PREFIX_BEARER + request.socialToken)
            ?: throw CustomException(INVALID_SOCIAL_TOKEN)
        val socialId = userInfo.id.toString()
        val user = getOrCreateUser(socialId, SocialPlatform.KAKAO)

        val (accessToken, refreshToken) = createTokens(user, accessTokenExpire, refreshTokenExpire)
        return ApiResult.ok(
            LoginResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                isCompletedOnboarding = user.isCompletedOnboarding
            )
        )
    }

    @Transactional
    fun naverLogin(
        request: SocialLoginRequest
    ): ApiResult<LoginResponse> {
        val userInfo = naverFeign.getUserInfo(JwtUtils.PREFIX_BEARER + request.socialToken)
            ?: throw CustomException(INVALID_SOCIAL_TOKEN)
        val socialId = userInfo.response.id
        val user = getOrCreateUser(socialId, SocialPlatform.NAVER)

        val (accessToken, refreshToken) = createTokens(user, accessTokenExpire, refreshTokenExpire)
        return ApiResult.ok(
            LoginResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                isCompletedOnboarding = user.isCompletedOnboarding
            )
        )
    }

    fun adminLogin(
        request: AdminLoginRequest
    ): ApiResult<TokenResponse> {
        val user = userRepository.findByUsername(request.username)
            ?: throw CustomException(NOT_FOUND_USER)

        validatePassword(request.password, user)
        val (accessToken, refreshToken) = createTokens(user, adminTokenExpire, adminTokenExpire)
        return ApiResult.ok(
            TokenResponse(
                accessToken = accessToken,
                refreshToken = refreshToken
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

        val (accessToken, refreshToken) = createTokens(user, accessTokenExpire, refreshTokenExpire)
        return ApiResult.ok(
            TokenResponse(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )
    }

    private fun createTokens(
        user: UserEntity,
        accessExpire: Long,
        refreshExpire: Long
    ): Pair<String, String> {
        val accessToken = JwtUtils.createToken(user, TokenType.ACCESS, accessExpire, secretKey)
        val refreshToken = JwtUtils.createToken(user, TokenType.REFRESH, refreshExpire, secretKey)

        return Pair(accessToken, refreshToken)
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

    fun appleLogin(request: SocialLoginRequest): ApiResult<LoginResponse> {
        val tokenInfo = appleFeign.getIdToken(
            clientId = appleClientId,
            clientSecret = generateClientSecret(),
            grantType = appleGrantType,
            code = request.socialToken, //애플의 경우만 사실상 인가코드
            redirectUri = appleRedirectUri
        ) ?: throw CustomException(INVALID_SOCIAL_TOKEN)


        val decodedJWT: DecodedJWT = JWT.decode(tokenInfo.id_token)
        val socialId = decodedJWT.subject
        val user = getOrCreateUser(socialId, SocialPlatform.APPLE)

        val (accessToken, refreshToken) = createTokens(user, accessTokenExpire, refreshTokenExpire)
        return ApiResult.ok(
            LoginResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                isCompletedOnboarding = user.isCompletedOnboarding
            )
        )
    }

    private fun generateClientSecret(): String {
        val expiration: LocalDateTime = LocalDateTime.now().plusMinutes(5)
        return Jwts.builder()
            .header().add("kid", appleKeyId).and() // 새로운 방식으로 헤더 설정
            .issuer(appleTeamId)
            .claim("aud", "https://appleid.apple.com")
            .subject(appleClientId)
            .expiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
            .issuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(getPrivateKey(), Jwts.SIG.ES256) // 최신 메서드로 서명 적용
            .compact()

    }

    private fun getPrivateKey(): PrivateKey {
        Security.addProvider(BouncyCastleProvider())
        val converter = JcaPEMKeyConverter().setProvider("BC")

        return try {
            val privateKeyBytes: ByteArray = Base64.getDecoder().decode(applePrivateKey)
            val privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes)
            converter.getPrivateKey(privateKeyInfo)
        } catch (e: Exception) {
            throw RuntimeException("Error converting private key from String", e)
        }
    }
}
