package com.numberone.daepiro.global.feign

import com.numberone.daepiro.global.feign.dto.KakaoLocalAddress
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "kakaoLocalFeign", url = "\${kakao.local-api-url}")
interface KakaoLocalFeign {
    @GetMapping
    fun getAddress(
        @RequestHeader(name = "Authorization") clientId: String,
        @RequestParam("x") longitude: Double,
        @RequestParam("y") latitude: Double
    ): KakaoLocalAddress?
}
