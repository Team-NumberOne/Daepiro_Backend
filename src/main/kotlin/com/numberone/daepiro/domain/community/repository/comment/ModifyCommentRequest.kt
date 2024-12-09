package com.numberone.daepiro.domain.community.repository.comment

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "댓글 수정 요청")
data class ModifyCommentRequest(
    val body: String,
)
