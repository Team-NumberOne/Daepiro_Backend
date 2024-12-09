package com.numberone.daepiro.domain.community.controller

import com.numberone.daepiro.domain.community.api.CommentApiV1
import com.numberone.daepiro.domain.community.dto.request.CreateCommentRequest
import com.numberone.daepiro.domain.community.dto.response.CommentSimpleResponse
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
}
