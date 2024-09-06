package com.numberone.daepiro.domain.user.controller

import com.numberone.daepiro.domain.user.dto.response.GetUserResponse
import com.numberone.daepiro.domain.user.service.UserService
import com.numberone.daepiro.global.DprApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/users")
@RestController
@Tag(name = "User API", description = "회원 관련 API")
class UserController(
    userService: UserService
) {
    @GetMapping("/v1")
    @Operation(summary = "Get user", description = "Get user")
    fun getUser(): DprApiResponse<GetUserResponse> {
        return DprApiResponse.ok(GetUserResponse.fake(), "/users/v1")
    }
}
