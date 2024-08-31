package com.numberone.daepiro.domain.user.controller

import com.numberone.daepiro.domain.user.dto.response.GetUserResponse
import com.numberone.daepiro.domain.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/users")
@RestController
class UserController(
    userService: UserService
) {
    @GetMapping("/v1")
    fun getUser(): ResponseEntity<GetUserResponse> {
        return ResponseEntity.ok(GetUserResponse.fake())
    }
}
