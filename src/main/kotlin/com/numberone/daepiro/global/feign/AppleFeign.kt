package com.numberone.daepiro.global.feign

import com.numberone.daepiro.global.feign.dto.AppleTokenInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam


@FeignClient(name = "appleFeign", url = "\${apple.base-url}")
interface AppleFeign {
    @PostMapping("/auth/token")
    fun getIdToken(
        @RequestParam("client_id") clientId: String?,
        @RequestParam("client_secret") clientSecret: String?,
        @RequestParam("grant_type") grantType: String?,
        @RequestParam("code") code: String?,
        @RequestParam("redirect_uri") redirectUri: String?
    ): AppleTokenInfo?

    @PostMapping("/auth/revoke")
    fun revokeToken(
        @RequestParam("client_id") clientId: String?,
        @RequestParam("client_secret") clientSecret: String?,
        @RequestParam("token") token: String?
    ): ResponseEntity<Void>
}
