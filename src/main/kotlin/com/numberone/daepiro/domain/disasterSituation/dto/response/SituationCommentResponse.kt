package com.numberone.daepiro.domain.disasterSituation.dto.response

import com.numberone.daepiro.domain.community.entity.Comment
import com.numberone.daepiro.domain.user.entity.UserEntity
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.LastModifiedDate
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

    @Schema(description = "삭제 여부", example = "false")
    val isDeleted: Boolean,

    @Schema(description = "수정 여부", example = "false")
    val isModified: Boolean,

    @Schema(description = "동네 인증 여부", example = "true")
    val isVerified: Boolean,

    @Schema(
        description = "자식 댓글 목록", example = "[\n" +
            "        {\n" +
            "          \"id\": 326,\n" +
            "          \"name\": \"짱구\",\n" +
            "          \"time\": \"2024-12-14T12:13:55.883Z\",\n" +
            "          \"content\": \"싫어요!\",\n" +
            "          \"likeCount\": 3,\n" +
            "          \"isMine\": true,\n" +
            "          \"isDeleted\": false,\n" +
            "          \"isModified\": false,\n" +
            "          \"isVerified\": true,\n" +
            "          \"childComments\": []" +
            "        }\n" +
            "      ]"
    )
    val childComments: List<SituationCommentResponse>
) {
    companion object {
        fun of(
            comment: Comment,
            user: UserEntity,
            isVerified: Boolean,
            childComments: List<Comment>,
            isChildVerified: Map<Long, Boolean>
        ): SituationCommentResponse {
            val isDeleted = comment.deletedAt != null
            val isModified = comment.lastModifiedAt != comment.createdAt
            return SituationCommentResponse(
                id = comment.id!!,
                name = comment.authUser!!.nickname!!,
                time = if (isModified) comment.lastModifiedAt else comment.createdAt,
                content = if (isDeleted) "작성자에 의해 삭제된 댓글입니다." else comment.body,
                likeCount = comment.likeCount.toLong(),
                isMine = comment.authUser!!.id == user.id,
                isDeleted = isDeleted,
                isModified = isModified,
                isVerified = isVerified,
                childComments = childComments.map {
                    of(
                        it,
                        user,
                        isChildVerified[it.id!!] ?: false,
                        listOf(),
                        mapOf()
                    )
                },
            )
        }
    }
}
