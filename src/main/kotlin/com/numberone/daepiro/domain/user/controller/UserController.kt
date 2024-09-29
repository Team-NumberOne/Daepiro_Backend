package com.numberone.daepiro.domain.user.controller

import com.numberone.daepiro.domain.user.api.UserApiV1
import com.numberone.daepiro.domain.user.dto.request.OnboardingRequest
import com.numberone.daepiro.domain.user.dto.response.CheckNicknameResponse
import com.numberone.daepiro.domain.user.dto.response.GetUserResponse
import com.numberone.daepiro.domain.user.service.UserService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) : UserApiV1 {
    override fun getUser(): ApiResult<GetUserResponse> {
        return ApiResult.ok(GetUserResponse.fake(), "/users/v1")
    }

    override fun checkNickname(
        nickname: String
    ): ApiResult<CheckNicknameResponse> {
        return userService.checkNickname(nickname)
    }

    override fun setOnboardingData(
        request: OnboardingRequest
    ): ApiResult<Unit> {
        return userService.setOnboardingData(
            request,
            SecurityContextUtils.getUserId()
        )
    }
}
