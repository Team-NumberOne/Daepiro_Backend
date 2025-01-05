package com.numberone.daepiro.domain.sponsor.dto.response

import com.numberone.daepiro.domain.sponsor.entity.Cheering
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class CheeringResponse(
    @Schema(description = "응원 id", example = "1")
    val id: Long,

    @Schema(description = "응원 내용", example = "응원합니다.")
    val content: String,

    @Schema(description = "작성자", example = "홍길동")
    val author: String,

    @Schema(description = "작성 시간", example = "2021-08-01T00:00:00")
    val time: LocalDateTime,

    @Schema(description = "수정 여부", example = "false")
    val isModified: Boolean,

    @Schema(description = "본인이 작성한 댓글인지 여부", example = "false")
    val isMine: Boolean
) {
    companion object {
        fun of(
            cheering: Cheering,
            isMine: Boolean
        ): CheeringResponse {
            val isModified = cheering.lastModifiedAt != cheering.createdAt
            return CheeringResponse(
                id = cheering.id!!,
                content = cheering.content,
                author = cheering.user.nickname!!,
                time = if (isModified) cheering.lastModifiedAt else cheering.createdAt,
                isModified = isModified,
                isMine = isMine
            )
        }
    }
}
