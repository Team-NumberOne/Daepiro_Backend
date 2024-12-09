package com.numberone.daepiro.domain.community.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "댓글 작성을 위한 요청 데이터")
data class CreateCommentRequest(
    @Schema(description = "댓글 본문")
    val body: String,
    @Schema(description = "대댓글인 경우에는 부모 댓글의 Id 를 담아서 요청. 외에는 null")
    val parentCommentId: Long? = null,
    @Schema(description = "게시글의 아이디")
    val articleId: Long,
)
