package com.numberone.daepiro.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(Components().addSecuritySchemes("JWT", bearerAuth()))
            .info(configurationInfo())
            .addSecurityItem(SecurityRequirement().addList("JWT"))
            .servers(listOf(Server().url("/")))
    }

    @Bean
    fun readyToUseGroup(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("1) ready to use")
            .pathsToMatch(
                "/v1/auth/**",
                "/v1/users/**",
                "/v1/home/**",
                "/v1/disastercontents/**",
                "/v1/shelters/**",
                "/v1/disastersituations/**",
                "/v1/articles/**",
                "/v1/comments/**",
                "/v1/user-address-verified",
                "/v1/behaviourtips/**",
                "/v1/sponsors/**",
                "/v1/my-page/**",
            )
            .pathsToExclude("/v1/behaviourtips","/v1/my-page/announcement/create","/v1/sponsors")
            .build()
    }

    @Bean
    fun readyForAdminGroup(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("2) ready for admin")
            .pathsToMatch(
                "/v1/datacollector/**",
                "/v1/behaviourtips",
                "/v1/sponsors",
                "/v1/my-page/announcement/create"
            )
            .build()
    }

    @Bean
    fun workInProgressGroup(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("3) work in progress")
            .pathsToMatch(
                ""
            )
            .build()
    }

    private fun configurationInfo(): Info {
        return Info()
            .title("대피로 백엔드 API")
            .description("API Documentation")
            .version("1.0.0")
    }

    private fun bearerAuth(): SecurityScheme {
        return SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER)
            .name(HttpHeaders.AUTHORIZATION)
    }
}
