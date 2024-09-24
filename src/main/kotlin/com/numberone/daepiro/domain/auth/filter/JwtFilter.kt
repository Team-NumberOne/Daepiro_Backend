package com.numberone.daepiro.domain.auth.filter

import com.numberone.daepiro.domain.auth.enums.TokenType
import com.numberone.daepiro.domain.auth.utils.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private val logger = KotlinLogging.logger {}

@Component
class JwtFilter(
    @Value("\${jwt.secret-key}") private val secretKey: String,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = request.getHeader(AUTHORIZATION)
        if (jwt != null) {
            try {
                val tokenInfo = JwtUtils.extractInfoFromToken(
                    jwt.replace(JwtUtils.PREFIX_BEARER, ""),
                    secretKey
                )
                if (tokenInfo.type == TokenType.ACCESS) {
                    val auth = UsernamePasswordAuthenticationToken
                        .authenticated(
                            tokenInfo.id,
                            null,
                            listOf(SimpleGrantedAuthority(tokenInfo.role.name))
                        )
                    SecurityContextHolder.getContext().authentication = auth
                }
            } catch (e: Exception) {
                logger.warn("Invalid JWT")
            }
        }
        filterChain.doFilter(request, response)
    }
}
