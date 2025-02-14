package com.numberone.daepiro.domain.mypage.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class CreateAnnouncementRequest(
    @Schema(
        description = "공지사항 제목",
        example = "공지사항 제목",
    )
    val title: String,
    @Schema(
        description = "공지사항 내용",
        example = "공지사항 내용",
    )
    val body: String
)
