package com.numberone.daepiro.domain.sponsor.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

data class SponsorRequest(
    @Schema(description = "후원할 하트 수", example = "100")
    @field:Min(1)
    val heart: Int
)
