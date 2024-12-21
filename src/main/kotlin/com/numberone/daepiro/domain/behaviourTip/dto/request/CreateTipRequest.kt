package com.numberone.daepiro.domain.behaviourTip.dto.request

import com.numberone.daepiro.domain.disaster.entity.DisasterType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class CreateTipRequest(
    @Schema(description = "재난 유형", example = "지진")
    @NotBlank
    val disasterType: String,

    @Schema(description = "필터", example = "실내")
    @NotBlank
    val filter: String,

    @Schema(description = "행동요령", example = "지진 발생 시 테이블 아래로 숨으세요.")
    @NotBlank
    val tip: String
)
