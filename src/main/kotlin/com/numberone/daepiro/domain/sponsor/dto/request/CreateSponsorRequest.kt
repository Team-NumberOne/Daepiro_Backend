package com.numberone.daepiro.domain.sponsor.dto.request

import com.amazonaws.services.cloudformation.model.ChangeSetSummary
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class CreateSponsorRequest(
    @Schema(description = "제목", example = "극한 호우로 삶의 터전을 잃은 이웃들에게 힘이 돼 주세요")
    val title: String,

    @Schema(description = "부제목", example = "서울의 15% 넘는 면적이 물에 잠겼습니다.")
    val subtitle: String,

    @Schema(description = "본문", example = "추석 연휴가 끝나자마자, 남부 지역을 중심으로 매우 강하고 많은 비가 쏟아졌습니다. 21일 하루에만, 부산 대부분 지역과 경남 창원시에 400mm 안팎의 폭우가 내렸습니다. 1년 강수량의 25%가량이 단 하루 만에 내리면서, 집과 도로, 축사와 건물 등 서울 면적의 15% 이상이 침수됐습니다. 거센 비바람을 피해 많은 이웃들이 집을 버리고 대피소로 몸을 피했습니다. 전남, 부산, 그리고 경남 등지에서 1,841세대, 2,626명이 집을 떠나 잠 못 이루는 밤을 보내야 했습니다")
    val body: String,

    @Schema(description = "후원사", example = "희망 브리지 전국재해구호협회")
    val sponsorName: String,

    @Schema(description = "후원사 설명", example = "희망브리지 전국재해구호협회는 갑작스러운 재난으로 힘들어하는 이웃을 돕기 위해 1961년 전국의 신문사와 방송사, 사회단체가 힘을 모아 설립한 순수 민간 구호단체입니다. 2001년 재해구호법 개정으로 국내 자연재해 피해 구호금을 지원할 수 있도록 대한민국 정부로부터 유일하게 권한을 부여받은 법정 구호단체로 재도약했습니다.")
    val sponsorDescription: String,

    @Schema(description = "후원사 URL", example = "https://www.naver.com")
    val sponsorUrl: String,

    @Schema(description = "후원사 게시글 URL", example = "https://www.naver.com")
    val sponsorPostUrl: String,

    @Schema(description = "썸네일", example = "https://img8.yna.co.kr/photo/yna/YH/2024/11/05/PYH2024110509530001300_T2.jpg")
    val thumbnail: String,

    @Schema(description = "요약", example = "[\"추석 연휴 이후 남부 지역에 강한 비가 쏟아졌어요.\", \"서울 면적의 15% 이상이 침수 됐어요.\", \"전남, 부산, 경남에서 많은 이웃들이 대피했어요.\"]")
    val summary: List<String>,

    @Schema(description = "마감 기한(null은 상시)", example = "2025-03-08T12:19:43.450Z")
    val deadline: LocalDateTime?,

    @Schema(description = "현재 하트", example = "102311")
    val currentHeart: Int,

    @Schema(description = "목표 하트", example = "150000")
    val targetHeart: Int,

    @Schema(description = "재난 종류", example = "호우")
    val disasterType: String
)
