package com.numberone.daepiro.domain.sponsor.dto.request

import com.amazonaws.services.cloudformation.model.ChangeSetSummary
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class CreateSponsorRequest(
    @Schema(description = "제목", example = "제목")
    val title: String,

    @Schema(description = "본문", example = "본문")
    val body: String,

    @Schema(description = "후원사", example = "포항시")
    val sponsorName: String,

    @Schema(description = "후원사 설명", example = "후원사 설명")
    val sponsorDescription: String,

    @Schema(description = "후원사 URL", example = "https://www.naver.com")
    val sponsorUrl: String,

    @Schema(
        description = "썸네일",
        example = "https://img8.yna.co.kr/photo/yna/YH/2024/11/05/PYH2024110509530001300_T2.jpg"
    )
    val thumbnail: String,

    @Schema(description = "요약", example = "요약")
    val summary: String,

    @Schema(description = "마감 기한", example = "2021-08-01T00:00:00")
    val deadline: LocalDateTime,

    @Schema(description = "현재 하트", example = "102311")
    val currentHeart: Int,

    @Schema(description = "목표 하트", example = "150000")
    val targetHeart: Int
)
