package com.numberone.daepiro.domain.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

data class OnboardingRequest(
    @field:Size(min = 1, max = 6, message = "실명은 1자 이상 6자 이하로 입력해주세요.")
    val realname: String,

    @field:Size(min = 1, max = 10, message = "닉네임은 1자 이상 10자 이하로 입력해주세요.")
    val nickname: String,
)
