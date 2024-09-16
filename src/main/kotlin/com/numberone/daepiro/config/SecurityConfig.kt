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
                authorize(POST, "/v1/auth/login", permitAll)
                authorize(POST, "/v1/auth/refresh", permitAll)
                authorize(POST, "/v1/auth/signup", permitAll)
                authorize(POST, "/v1/auth/admin", permitAll)
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
    fun authenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { request, response, authException ->
            response.sendError(401, "Unauthorized")//todo 공통 response 형식과 통일
        }
    }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler {
        return AccessDeniedHandler { request, response, accessDeniedException ->
            response.sendError(403, "Forbidden")//todo 공통 response 형식과 통일
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
