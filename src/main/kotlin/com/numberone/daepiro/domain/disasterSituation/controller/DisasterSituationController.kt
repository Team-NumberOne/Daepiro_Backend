package com.numberone.daepiro.domain.disasterSituation.controller

import com.numberone.daepiro.domain.disasterSituation.api.DisasterSituationApiV1
import com.numberone.daepiro.domain.disasterSituation.dto.request.EditCommentRequest
import com.numberone.daepiro.domain.disasterSituation.dto.response.DisasterSituationResponse
import com.numberone.daepiro.domain.disasterSituation.dto.response.SituationCommentResponse
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class DisasterSituationController : DisasterSituationApiV1 {
    //todo fake api용 코드입니다. 추후 삭제 필요
    override fun getDisasterSituations(): ApiResult<List<DisasterSituationResponse>> {
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

        val disasterSituations = listOf(
            DisasterSituationResponse.ofFake(
                id = 1032,
                type = "호우",
                title = "서울특별시 성북구 쌍문동 호우 발생",
                content = "금일 10.23. 19:39경 소촌동 855 화재 발생, 인근주민은 안전 유의 및 차량우회바랍니다. 960-8222",
                location = "서울특별시 성북구",
                time = LocalDateTime.of(2024, 10, 23, 19, 53),
                commentCount = 1,
                isReceived = true,
                comments = comments
            ),
            DisasterSituationResponse.ofFake(
                id = 1033,
                type = "화재",
                title = "서울특별시 성북구 쌍문동 화재 발생",
                content = "금일 10.23. 19:39경 소촌동 855 화재 발생, 인근주민은 안전 유의 및 차량우회바랍니다. 960-8222",
                location = "서울특별시 성북구",
                time = LocalDateTime.of(2024, 10, 23, 19, 53),
                commentCount = 1,
                isReceived = false,
                comments = emptyList()
            ),
            DisasterSituationResponse.ofFake(
                id = 1034,
                type = "지진",
                title = "서울특별시 성북구 쌍문동 지진 발생",
                content = "금일 10.23. 19:39경 소촌동 855 지진 발생, 인근주민은 안전 유의 및 차량우회바랍니다. 960-8222",
                location = "서울특별시 성북구",
                time = LocalDateTime.of(2024, 10, 23, 19, 53),
                commentCount = 1,
                isReceived = false,
                comments = listOf(
                    SituationCommentResponse.ofFake(
                        id = 5,
                        name = "오종석",
                        time = LocalDateTime.of(2024, 10, 23, 19, 53),
                        content = "지진이라니",
                        likeCount = 3,
                        isMine = false,
                        childComments = emptyList()
                    )
                )
            )
        )

        return ApiResult.ok(disasterSituations)
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

    override fun editComment(commentId: Long, request: EditCommentRequest): ApiResult<Unit> {
        return ApiResult.ok()
    }

    override fun deleteComment(commentId: Long): ApiResult<Unit> {
        return ApiResult.ok()
    }
}
