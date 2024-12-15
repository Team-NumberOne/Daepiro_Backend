package com.numberone.daepiro.domain.community.controller

import com.numberone.daepiro.domain.community.api.CommentApiV1
import com.numberone.daepiro.domain.community.dto.request.CreateCommentRequest
import com.numberone.daepiro.domain.community.dto.request.ReportRequest
import com.numberone.daepiro.domain.community.dto.response.CommentLikeResponse
import com.numberone.daepiro.domain.community.dto.response.CommentSimpleResponse
import com.numberone.daepiro.domain.community.repository.comment.ModifyCommentRequest
import com.numberone.daepiro.domain.community.service.CommentService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentControllerV1(
    private val commentService: CommentService,
) : CommentApiV1 {
    override fun createComment(request: CreateCommentRequest): ApiResult<CommentSimpleResponse> {

        val response = commentService.createComment(
            userId = SecurityContextUtils.getUserId(),
            request = request
        )

        return ApiResult.ok(
            data = response,
            path = "/v1/comments"
        )
    }

    override fun report(
        id: Long,
        request: ReportRequest
    ): ApiResult<Unit> {
        commentService.report(
            userId = SecurityContextUtils.getUserId(),
            commentId = id,
            request = request
        )
        return ApiResult.noContent(path = "/v1/comments/$id", message = "reported")
    }

    override fun deleteComment(id: Long): ApiResult<Unit> {
        commentService.deleteComment(id)
        return ApiResult.ok(path = "/v1/comments/$id")
    }

    override fun modifyComment(id: Long, request: ModifyCommentRequest): ApiResult<CommentSimpleResponse> {

        val response = commentService.modifyComment(
            commentId = id,
            request = request
        )

        return ApiResult.ok(
            data = response,
            path = "/v1/comments/$id"
        )
    }

    override fun like(id: Long): ApiResult<CommentLikeResponse> {

        val response = commentService.like(
            commentId = id,
            userId = SecurityContextUtils.getUserId()
        )

        return ApiResult.ok(
            data = response,
            path = "/v1/comments/$id/like",
        )
    }
}
