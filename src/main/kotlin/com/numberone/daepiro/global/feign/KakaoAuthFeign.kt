package com.numberone.daepiro.global.feign

import com.numberone.daepiro.global.feign.dto.KakaoUserInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "kakaoAuthFeign", url = "\${kakao.base-url}")
interface KakaoAuthFeign {
    @GetMapping(value = ["\${kakao.token-info-url}"])
    fun getUserInfo(@RequestHeader(name = "Authorization") token: String): KakaoUserInfo?
}
