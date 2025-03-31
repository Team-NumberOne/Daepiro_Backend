package com.numberone.daepiro.domain.home.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class GetStatusResponse(
    @Schema(description = "현재 재난 알림 유무", example = "true")
    val isOccurred: Boolean
)
