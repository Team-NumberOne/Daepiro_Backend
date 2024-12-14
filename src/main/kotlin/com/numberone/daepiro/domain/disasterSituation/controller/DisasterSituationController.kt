package com.numberone.daepiro.domain.disasterSituation.controller

import com.numberone.daepiro.domain.disasterSituation.api.DisasterSituationApiV1
import com.numberone.daepiro.domain.disasterSituation.dto.request.CreateSituationCommentRequest
import com.numberone.daepiro.domain.disasterSituation.dto.request.EditSituationCommentRequest
import com.numberone.daepiro.domain.disasterSituation.dto.response.DisasterSituationResponse
import com.numberone.daepiro.domain.disasterSituation.dto.response.SituationCommentResponse
import com.numberone.daepiro.domain.disasterSituation.service.DisasterSituationService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class DisasterSituationController(
    private val disasterSituationService: DisasterSituationService
) : DisasterSituationApiV1 {
    override fun getDisasterSituations(): ApiResult<List<DisasterSituationResponse>> {
        val userId = SecurityContextUtils.getUserId()
        return disasterSituationService.getDisasterSituations(userId)
    }

    override fun getComments(situationId: Long): ApiResult<List<SituationCommentResponse>> {
        val comments = listOf(
            SituationCommentResponse.ofFake(
                id = 1,
                name = "김연지",
                time = LocalDateTime.of(2024, 10, 23, 19, 53),
                content = "이런 상황에서는 이렇게 대처하는 것이 좋아요!",
                likeCount = 3,
                isMine = true,
                childComments = listOf(
                    SituationCommentResponse.ofFake(
                        id = 2,
                        name = "한나영",
                        time = LocalDateTime.of(2024, 10, 23, 19, 53),
                        content = "싫은데요?",
                        likeCount = 1,
                        isMine = false,
                        childComments = emptyList()
                    ),
                    SituationCommentResponse.ofFake(
                        id = 3,
                        name = "김서윤",
                        time = LocalDateTime.of(2024, 10, 23, 19, 53),
                        content = "절대 안해야지",
                        likeCount = 2,
                        isMine = false,
                        childComments = emptyList()
                    )
                )
            )
        )

        return ApiResult.ok(comments)
    }

    override fun createComment(situationId: Long, request: CreateSituationCommentRequest): ApiResult<Unit> {
        val userId = SecurityContextUtils.getUserId()
        disasterSituationService.createComment(situationId, userId, request)
        return ApiResult.ok()
    }

    override fun editComment(commentId: Long, request: EditSituationCommentRequest): ApiResult<Unit> {
        return ApiResult.ok()
    }

    override fun deleteComment(commentId: Long): ApiResult<Unit> {
        return ApiResult.ok()
    }
}
