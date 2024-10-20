package com.numberone.daepiro.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.numberone.daepiro.domain.auth.filter.JwtFilter
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_AUTHENTICATED
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_AUTHORIZED
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.access.ExceptionTranslationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val jwtFilter: JwtFilter,
    private val objectMapper: ObjectMapper
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
                authorize(POST, "/v1/auth/user", permitAll)
                authorize("/v1/datacollector/**", hasAuthority("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            addFilterBefore<ExceptionTranslationFilter>(jwtFilter)
            cors { configurationSource = corsConfigurationSource() }
            exceptionHandling {
                authenticationEntryPoint = authNFailHandler
                accessDeniedHandler = authZFailHandler
            }
            sessionManagement { sessionCreationPolicy = STATELESS }
        }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("POST", "GET", "DELETE", "PUT")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticaionEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { _, response, _ ->
            setErrorResponse(response, NOT_AUTHENTICATED)
        }
    }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler {
        return AccessDeniedHandler { _, response, _ ->
            setErrorResponse(response, NOT_AUTHORIZED)
        }
    }

    private fun setErrorResponse(
        response: HttpServletResponse,
        exceptionContext: CustomErrorContext
    ) {
        val result = ApiResult.error(exceptionContext)
        val json = objectMapper.writeValueAsString(result)

        response.status = exceptionContext.httpStatus.value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(json)
    }
}
