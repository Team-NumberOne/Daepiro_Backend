package com.numberone.daepiro.domain.user.controller

import com.numberone.daepiro.domain.user.api.UserApiV1
import com.numberone.daepiro.domain.user.dto.request.CheckNicknameRequest
import com.numberone.daepiro.domain.user.dto.request.OnboardingRequest
import com.numberone.daepiro.domain.user.dto.response.CheckNicknameResponse
import com.numberone.daepiro.domain.user.dto.response.GetUserResponse
import com.numberone.daepiro.domain.user.service.UserService
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) : UserApiV1 {
    override fun getUser(
    ): ResponseEntity<ApiResult<GetUserResponse>> {
        return ApiResult.ok(GetUserResponse.fake(), "/users/v1")
            .toResponseEntity()
    }

    override fun checkNickname(
        request: CheckNicknameRequest
    ): ResponseEntity<ApiResult<CheckNicknameResponse>> {
        return userService.checkNickname(request)
            .toResponseEntity()
    }

    override fun setOnboardingData(
        request: OnboardingRequest
    ): ResponseEntity<ApiResult<Unit>> {
        return userService.setOnboardingData(request)
            .toResponseEntity()
    }
}
