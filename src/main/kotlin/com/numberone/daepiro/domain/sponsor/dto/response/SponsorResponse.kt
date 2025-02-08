package com.numberone.daepiro.domain.sponsor.dto.response

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ArticleType
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class SponsorResponse(
    @Schema(description = "아이디", example = "793")
    val id: Long,

    @Schema(description = "마감 기한", example = "2025-08-01T00:00:00")
    val deadline: LocalDateTime?,

    @Schema(description = "후원사", example = "포항시")
    val sponsorName: String,

    @Schema(description = "제목", example = "제목")
    val title: String,

    @Schema(description = "부제목", example = "부제목")
    val subtitle: String,

    @Schema(
        description = "썸네일",
        example = "https://img8.yna.co.kr/photo/yna/YH/2024/11/05/PYH2024110509530001300_T2.jpg"
    )
    val thumbnail: String,

    @Schema(description = "현재 하트", example = "102311")
    val currentHeart: Int,

    @Schema(description = "목표 하트", example = "150000")
    val targetHeart: Int,

    @Schema(description = "후원사 설명", example = "후원사 설명")
    val sponsorDescription: String,

    @Schema(description = "후원사 URL", example = "https://www.naver.com")
    val sponsorUrl: String,

    @Schema(description = "요약")
    val summary: List<String>,

    @Schema(description = "본문 내용", example = "본문 내용")
    val body: String,

    @Schema(description = "재난 종류", example = "지진")
    val disasterType: String
) {
    companion object {
        fun of(
            article: Article,
        ): SponsorResponse {
            if (article.type != ArticleType.SPONSOR) {
                throw IllegalArgumentException("해당 게시글은 후원글이 아닙니다.")
            }
            return SponsorResponse(
                id = article.id!!,
                deadline = article.deadline,
                sponsorName = article.sponsorName!!,
                title = article.title,
                subtitle = article.subtitle!!,
                thumbnail = article.thumbnail!!,
                currentHeart = article.currentHeart!!,
                targetHeart = article.targetHeart!!,
                sponsorDescription = article.sponsorDescription!!,
                sponsorUrl = article.sponsorUrl!!,
                summary = article.summary,
                body = article.body,
                disasterType = article.disasterType!!.type.korean
            )
        }
    }
}
