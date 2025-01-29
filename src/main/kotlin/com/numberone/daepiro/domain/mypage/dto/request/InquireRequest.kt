package com.numberone.daepiro.domain.mypage.dto.request

import com.numberone.daepiro.domain.mypage.entity.InquiryType
import io.swagger.v3.oas.annotations.media.Schema

data class InquireRequest(
    @Schema(
        description = "문의 유형",
        example = "COMMUNITY"
    )
    val type: InquiryType,
    @Schema(
        description = "문의 내용",
        example = "커뮤니티 이용 중 발생한 문제에 대해 문의드립니다.",
    )
    val content: String,
    @Schema(
        description = "연락받을 이메일",
        example = "example@naver.com"
    )
    val email: String,
)
