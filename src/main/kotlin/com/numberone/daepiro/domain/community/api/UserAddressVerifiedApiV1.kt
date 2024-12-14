package com.numberone.daepiro.domain.community.api

import com.numberone.daepiro.domain.community.dto.request.UserAddressVerifiedRequest
import com.numberone.daepiro.domain.community.dto.response.UserAddressVerifiedResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "UserAddressVerified API", description = "위치기반 동네(지역) 인증 관련 API")
@RequestMapping("/v1/user-address-verified")
interface UserAddressVerifiedApiV1 {

    @Operation(
        summary = "해당 유저가 동네 인증을 받았는지 확인하는 API"
    )
    @GetMapping
    fun getUserAddressVerified(): ApiResult<List<UserAddressVerifiedResponse>>


    @Operation(
        summary = "동네 인증을 수행합니다."
    )
    @PutMapping
    fun putUserAddressVerified(
        @RequestBody request: UserAddressVerifiedRequest,
    ): ApiResult<Unit>

}
