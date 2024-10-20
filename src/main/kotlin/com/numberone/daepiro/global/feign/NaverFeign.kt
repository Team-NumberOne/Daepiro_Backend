package com.numberone.daepiro.global.feign

import com.numberone.daepiro.global.feign.dto.NaverUserInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "naverFeign", url = "\${naver.base-url}")
interface NaverFeign {
    @GetMapping(value = ["\${naver.token-info-url}"])
    fun getUserInfo(@RequestHeader(name = "Authorization") token: String): NaverUserInfo?
}
