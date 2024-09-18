package com.numberone.daepiro.domain.user.controller

import com.numberone.daepiro.domain.user.dto.response.GetUserResponse
import com.numberone.daepiro.domain.user.service.UserService
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/users")
@RestController
@Tag(name = "User API", description = "회원 관련 API")//todo 인터페이스로 swagger 내용 분리
class UserController(
    userService: UserService
) {
    @GetMapping
    @Operation(summary = "Get user", description = "Get user")
    fun getUser(): ApiResult<GetUserResponse> {
        return ApiResult.ok(GetUserResponse.fake(), "/users/v1")
    }
}
