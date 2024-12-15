package com.numberone.daepiro.domain.community.dto.request

import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "신고 요청 스펙")
data class ReportRequest(
    @Schema(description = "상세 이유")
    val detail: String,
    @Schema(description = "신고 유형")
    val type: String,
    @Schema(description = "연락받을 이메일")
    val email: String,
) {
    init {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        if (!emailRegex.matches(email)) {
            throw CustomException(CustomErrorContext.INVALID_VALUE, "올바르지 않은 이메일 형식입니다: $email")
        }
    }
}
