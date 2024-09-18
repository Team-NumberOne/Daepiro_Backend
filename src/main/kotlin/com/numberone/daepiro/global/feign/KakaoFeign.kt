package com.numberone.daepiro.global.feign

import com.numberone.daepiro.global.feign.dto.KakaoUserInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader


@FeignClient(name = "kakaoFeign", url = "\${oauth.kakao.base-url}")
interface KakaoFeign {
    @GetMapping(value = ["\${oauth.kakao.token-info-url}"])
    fun getUserInfo(@RequestHeader(name = "Authorization") token: String): KakaoUserInfo?
}
