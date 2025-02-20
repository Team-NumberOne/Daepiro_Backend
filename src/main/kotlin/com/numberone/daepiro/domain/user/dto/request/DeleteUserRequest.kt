package com.numberone.daepiro.domain.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class DeleteUserRequest(
    @Schema(description = "애플 인가코드")
    val appleCode: String?
)
