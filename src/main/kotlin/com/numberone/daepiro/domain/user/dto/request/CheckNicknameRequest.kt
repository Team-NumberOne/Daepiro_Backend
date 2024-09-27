package com.numberone.daepiro.domain.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

data class CheckNicknameRequest(
    @Schema(description = "닉네임", example = "초코송이")
    @field:Size(min = 1, max = 10, message = "닉네임은 1자 이상 10자 이하로 입력해주세요.")
    val nickname: String
)
