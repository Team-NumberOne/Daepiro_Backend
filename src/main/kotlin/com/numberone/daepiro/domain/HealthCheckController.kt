package com.numberone.daepiro.domain

import com.numberone.daepiro.global.ApiResult
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneOffset

@RestController
@RequestMapping("/health-check")
class HealthCheckController {

    @Operation(summary = "health check", description = "health check")
    @GetMapping
    fun healthCheck(): ApiResult<String> {
        return ApiResult.ok("I'm alive at ${LocalDateTime.now().atOffset(ZoneOffset.UTC)} (UTC time)")
    }
}
