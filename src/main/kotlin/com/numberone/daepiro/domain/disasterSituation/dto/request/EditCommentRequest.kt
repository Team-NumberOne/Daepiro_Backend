package com.numberone.daepiro.domain.disasterSituation.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class EditCommentRequest(
    @Schema(description = "수정할 댓글 내용", example = "이런 상황에서는 이렇게 대처하는 것이 좋아요!")
    @NotBlank
    val content: String
)
