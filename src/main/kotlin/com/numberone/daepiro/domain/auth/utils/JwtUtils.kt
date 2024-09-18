package com.numberone.daepiro.domain.auth.utils

import com.numberone.daepiro.domain.auth.enums.TokenType
import com.numberone.daepiro.domain.auth.vo.TokenInfo
import com.numberone.daepiro.domain.user.entity.UserEntity
import com.numberone.daepiro.domain.user.enums.Role
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_TOKEN
import com.numberone.daepiro.global.exception.CustomException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*

object JwtUtils {
    const val CLAIM_ID = "id"
    const val CLAIM_AUTHORITY = "authority"
    const val CLAIM_TYPE = "type"
    const val PREFIX_BEARER = "Bearer "

    fun createToken(
        user: UserEntity,
        type: TokenType,
        expire: Long,
        secretKey: String,
    ): String {
        return Jwts.builder()
            .claim(CLAIM_ID, user.id)
            .claim(CLAIM_AUTHORITY, user.role.name)
            .claim(CLAIM_TYPE, type.name)
            .expiration(Date(System.currentTimeMillis() + expire))
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray(UTF_8)))
            .compact()
    }

    fun extractInfoFromToken(
        token: String,
        secretKey: String,
    ): TokenInfo {
        val claims: Claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.toByteArray(UTF_8)))
            .build()
            .parseSignedClaims(token)
            .payload
        val tokenType = TokenType.valueOf(claims.get(CLAIM_TYPE, String::class.java))

        return TokenInfo(
            claims.get(CLAIM_ID, Integer::class.java).toLong(),
            Role.valueOf(claims.get(CLAIM_AUTHORITY, String::class.java)),
            tokenType
        )
    }
}
