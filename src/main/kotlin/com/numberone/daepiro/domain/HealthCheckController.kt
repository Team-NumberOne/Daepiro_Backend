package com.numberone.daepiro.domain

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneOffset

@RestController
@RequestMapping("/health-check")
class HealthCheckController {
    @GetMapping
    fun healthCheck(): ResponseEntity<String> {
        return ResponseEntity.ok("I'm alive at ${LocalDateTime.now().atOffset(ZoneOffset.UTC)} (UTC time)")
    }
}