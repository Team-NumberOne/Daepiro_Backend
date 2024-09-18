package com.numberone.daepiro.config

import com.numberone.daepiro.domain.auth.filter.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.access.ExceptionTranslationFilter

@Configuration
class SecurityConfig(
    private val jwtFilter: JwtFilter
) {
    @Bean
    fun filterChain(
        http: HttpSecurity,
        authNFailHandler: AuthenticationEntryPoint,
        authZFailHandler: AccessDeniedHandler
    ): SecurityFilterChain {
        http {
            csrf { disable() }
            authorizeRequests {
                authorize("/favicon.ico", permitAll)
                authorize("/error", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/swagger-resources/**", permitAll)
                authorize("/api-docs/**", permitAll)
                authorize(POST, "/v1/auth/login/**", permitAll)
                authorize(POST, "/v1/auth/refresh", permitAll)
                authorize(POST, "/v1/auth/admin", permitAll)
                authorize(anyRequest, authenticated)
            }
            addFilterBefore<ExceptionTranslationFilter>(jwtFilter)
            exceptionHandling {
                authenticationEntryPoint = authNFailHandler
                accessDeniedHandler = authZFailHandler
            }
            sessionManagement { sessionCreationPolicy = STATELESS }
        }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticaionEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { request, response, authException ->
            response.sendError(401, "Unauthorized")
        }
    }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler {
        return AccessDeniedHandler { request, response, accessDeniedException ->
            response.sendError(403, "Forbidden")
        }
    }
}
