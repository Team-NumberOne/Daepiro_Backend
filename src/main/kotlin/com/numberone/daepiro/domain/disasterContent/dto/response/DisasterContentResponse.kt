package com.numberone.daepiro.domain.disasterContent.dto.response

import com.numberone.daepiro.domain.dataCollecter.entity.News
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class DisasterContentResponse(
    @Schema(description = "제목", example = "[내일날씨] 전국 대체로 맑음…낮 기온 20도 안팎 '포근'")
    val title: String,

    @Schema(description = "썸네일 이미지 url", example = "https://img8.yna.co.kr/photo/yna/YH/2024/11/05/PYH2024110509530001300_T2.jpg")
    val thumbnailUrl: String?,

    @Schema(description = "기사 url", example = "https://www.yna.co.kr/view/AKR20241110003300004?section=safe/news")
    val bodyUrl: String,

    @Schema(description = "출처", example = "연합뉴스")
    val source: String,

    @Schema(description = "게시일", example = "2021-08-01T00:00:00")
    val publishedAt: LocalDateTime,

    @Schema(description = "조회수", example = "10")
    val viewCount: Int,

    @Schema(description = "좋아요 수", example = "10")
    val likeCount: Int,
) {
    companion object {
        fun of(news: News): DisasterContentResponse {
            return DisasterContentResponse(
                title = news.title,
                thumbnailUrl = news.thumbnailUrl,
                bodyUrl = news.body,
                source = "연합뉴스", // todo(추후 기획에서 뉴스 이외에 콘텐츠가 추가 되면 변경)
                publishedAt = news.publishedAt,
                viewCount = 10, // todo(조회수와 좋아요수는 article 도메인이 만들어진 후에 반영)
                likeCount = 5,
            )
        }
    }
}
