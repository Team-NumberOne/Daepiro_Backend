package com.numberone.daepiro.domain.community.api

import com.numberone.daepiro.domain.community.dto.request.CreateCommentRequest
import com.numberone.daepiro.domain.community.dto.response.CommentLikeResponse
import com.numberone.daepiro.domain.community.dto.response.CommentSimpleResponse
import com.numberone.daepiro.domain.community.repository.comment.ModifyCommentRequest
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Comment API", description = "댓글 관련 API")
@RequestMapping("/v1/comments")
interface CommentApiV1 {
    @Operation(
        summary = "댓글 작성"
    )
    @PostMapping
    fun createComment(
        @RequestBody request: CreateCommentRequest,
    ): ApiResult<CommentSimpleResponse>

    @Operation(
        summary = "댓글 제거"
    )
    @DeleteMapping("/{id}")
    fun deleteComment(
        @PathVariable id: Long,
    ): ApiResult<Unit>

    @Operation(
        summary = "댓글 수정"
    )
    @PutMapping("/{id}")
    fun modifyComment(
        @PathVariable id: Long,
        @RequestBody request: ModifyCommentRequest,
    ): ApiResult<CommentSimpleResponse>

    @Operation(
        summary = "댓글에 좋아요 추가/해제"
    )
    @PutMapping("/{id}/like")
    fun like(
        @PathVariable id: Long,
    ): ApiResult<CommentLikeResponse>

}
