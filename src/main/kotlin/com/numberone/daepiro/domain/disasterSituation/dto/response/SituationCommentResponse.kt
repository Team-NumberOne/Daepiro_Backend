package com.numberone.daepiro.domain.disasterSituation.dto.response

import com.numberone.daepiro.domain.community.entity.Comment
import com.numberone.daepiro.domain.user.entity.UserEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class SituationCommentResponse(
    @Schema(description = "댓글 id", example = "325")
    val id: Long,

    @Schema(description = "작성자 이름", example = "김연지")
    val name: String,

    @Schema(description = "작성 시간", example = "2024-10-23T19:53:00")
    val time: LocalDateTime,

    @Schema(description = "댓글 내용", example = "이런 상황에서는 이렇게 대처하는 것이 좋아요!")
    val content: String,

    @Schema(description = "좋아요 수", example = "3")
    val likeCount: Long,

    @Schema(description = "내 댓글 여부", example = "true")
    val isMine: Boolean,

    @Schema(description = "자식 댓글 목록", example= "[\n" +
        "        {\n" +
        "          \"id\": 326,\n" +
        "          \"name\": \"짱구\",\n" +
        "          \"time\": \"2024-12-14T12:13:55.883Z\",\n" +
        "          \"content\": \"싫어요!\",\n" +
        "          \"likeCount\": 3,\n" +
        "          \"isMine\": true,\n" +
        "          \"childComments\": []" +
        "        }\n" +
        "      ]")
    val childComments: List<SituationCommentResponse>
) {
    companion object {
        // todo fake api용 코드입니다. 추후 삭제 필요
        fun ofFake(
            id: Long,
            name: String,
            time: LocalDateTime,
            content: String,
            likeCount: Long,
            isMine: Boolean,
            childComments: List<SituationCommentResponse>
        ): SituationCommentResponse {
            return SituationCommentResponse(
                id = id,
                name = name,
                time = time,
                content = content,
                likeCount = likeCount,
                isMine = isMine,
                childComments = childComments
            )
        }

        fun of(
            comment: Comment,
            user: UserEntity,
            childComments: List<Comment>
        ): SituationCommentResponse {
            return SituationCommentResponse(
                id = comment.id!!,
                name = comment.authUser!!.nickname!!,
                time = comment.createdAt,
                content = comment.body,
                likeCount = comment.likeCount.toLong(),
                isMine = comment.authUser!!.id == user.id,
                childComments = childComments.map { of(it, user, listOf()) }
            )
        }
    }
}
