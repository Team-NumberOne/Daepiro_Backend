package com.numberone.daepiro.domain.community.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "신고 요청 스펙")
data class ReportRequest(
    @Schema(description = "상세 이유")
    val detail: String,
    @Schema(description = "신고 유형")
    val type: String,
    @Schema(description = "연락받을 이메일")
    val email: String,
)
