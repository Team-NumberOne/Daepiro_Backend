package com.numberone.daepiro.domain.disasterSituation.dto.response

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.Comment
import com.numberone.daepiro.domain.user.entity.UserEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class DisasterSituationResponse(
    @Schema(description = "재난상황 게시글 id", example = "1032")
    val id: Long,

    @Schema(description = "재난 종류", example = "호우")
    val type: String,

    @Schema(description = "제목", example = "서울특별시 성북구 쌍문동 호우 발생")
    val title: String,

    @Schema(description = "내용", example = "금일 10.23. 19:39경 소촌동 855 화재 발생, 인근주민은 안전 유의 및 차량우회바랍니다. 960-8222")
    val content: String,

    @Schema(description = "위치", example = "서울특별시 성북구")
    val location: String,

    @Schema(description = "발생 시간", example = "2024-10-23T19:53:00")
    val time: LocalDateTime,

    @Schema(description = "댓글 수", example = "3")
    val commentCount: Long,

    @Schema(description = "수신 여부", example = "true")
    val isReceived: Boolean,

    @Schema(description = "댓글 목록")
    val comments: List<SituationCommentResponse>
) {
    companion object {
        fun of(
            article: Article,
            isReceived: Boolean,
            comments: List<Pair<Comment, List<Comment>>>,
            user: UserEntity
        ): DisasterSituationResponse {
            return DisasterSituationResponse(
                id = article.id!!,
                type = article.disasterType!!.type.korean,
                title = article.title,
                content = article.body,
                location = article.address!!.toFullAddress(),
                time = article.createdAt,
                commentCount = comments.size.toLong(),
                isReceived = isReceived,
                comments = comments.map { SituationCommentResponse.of(it.first, user, false,it.second, mapOf()) }
            )
        }
    }
}
