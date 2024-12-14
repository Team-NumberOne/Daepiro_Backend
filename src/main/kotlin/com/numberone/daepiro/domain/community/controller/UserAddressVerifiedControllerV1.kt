package com.numberone.daepiro.domain.community.controller

import com.numberone.daepiro.domain.community.api.UserAddressVerifiedApiV1
import com.numberone.daepiro.domain.community.dto.request.UserAddressVerifiedRequest
import com.numberone.daepiro.domain.community.dto.response.UserAddressVerifiedResponse
import com.numberone.daepiro.domain.community.service.UserAddressVerifiedService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.web.bind.annotation.RestController

@RestController
class UserAddressVerifiedControllerV1(
    private val userAddressVerifiedService: UserAddressVerifiedService,
) : UserAddressVerifiedApiV1 {
    override fun getUserAddressVerified(): ApiResult<List<UserAddressVerifiedResponse>> {

        val response = userAddressVerifiedService.getVerified(
            userId = SecurityContextUtils.getUserId()
        )

        return ApiResult.ok(data = response)
    }

    override fun putUserAddressVerified(request: UserAddressVerifiedRequest): ApiResult<Unit> {

        userAddressVerifiedService.verify(
            userId = SecurityContextUtils.getUserId(),
            request = request,
        )

        return ApiResult.noContent()
    }
}
