package com.numberone.daepiro.domain.mypage.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.numberone.daepiro.domain.community.entity.Article
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class AnnouncementResponse(
    @Schema(description = "공지사항 ID", example = "1")
    val id: Long,

    @Schema(description = "공지사항 제목", example = "공지사항 제목")
    val title: String,

    @Schema(description = "공지사항 내용", example = "공지사항 내용")
    val body: String,

    @Schema(description = "공지사항 작성일", example = "2021-08-01T00:00:00")
    val createdAt: LocalDateTime,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val next: List<AnnouncementResponse>?
) {
    companion object {
        fun of(announcement: Article, next: List<Article>? = null): AnnouncementResponse {
            return AnnouncementResponse(
                id = announcement.id!!,
                title = announcement.title,
                body = announcement.body,
                createdAt = announcement.createdAt,
                next = next?.map { AnnouncementResponse.of(it, null) }
            )
        }
    }
}
