package com.numberone.daepiro.domain.disasterSituation.api

import com.numberone.daepiro.domain.disasterSituation.dto.request.EditCommentRequest
import com.numberone.daepiro.domain.disasterSituation.dto.response.DisasterSituationResponse
import com.numberone.daepiro.domain.disasterSituation.dto.response.SituationCommentResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Disaster Situation API", description = "재난 상황용 API")
@RequestMapping("/v1/disastersituations")
interface DisasterSituationApiV1 {
    @GetMapping
    @Operation(
        summary = "(fake api) 재난상황 글 목록 조회",
        description = """
        재난상황 글 목록을 조회합니다.
        프론트에서 수신/전체 필터링은 반환된 리스트 안에 각 데이터의 isReceived 필드를 이용하여 구분하도록 구현하였습니다.
        (fake api는 미구현 기능에 대해서 프론트 테스트를 위해 어떤 요청값이든 고정된 응답값만 반환하도록 설계되어 있습니다.)
    """
    )
    fun getDisasterSituations(): ApiResult<List<DisasterSituationResponse>>

    @GetMapping("/comments/{situationId}")
    @Operation(
        summary = "(fake api) 댓글 목록 조회",
        description = """
        특정 재난상황 글의 댓글 목록을 조회합니다.
        댓글의 대댓글은 childComments 필드를 이용하여 구현하였습니다.
        (fake api는 미구현 기능에 대해서 프론트 테스트를 위해 어떤 요청값이든 고정된 응답값만 반환하도록 설계되어 있습니다.)
    """
    )
    fun getComments(
        @Schema(description = "재난상황 글 id", example = "1032") @PathVariable situationId: Long
    ): ApiResult<List<SituationCommentResponse>>

    @PutMapping("/comments/{commentId}")
    @Operation(
        summary = "(fake api) 댓글 수정",
        description = """
        댓글을 수정합니다.
        댓글 작성자만 수정할 수 있습니다.
        (fake api는 미구현 기능에 대해서 프론트 테스트를 위해 어떤 요청값이든 고정된 응답값만 반환하도록 설계되어 있습니다.)
    """
    )
    fun editComment(
        @Schema(description = "댓글 id", example = "325") @PathVariable commentId: Long,
        @RequestBody @Valid request: EditCommentRequest
    ): ApiResult<Unit>

    @DeleteMapping("/comments/{commentId}")
    @Operation(
        summary = "(fake api) 댓글 삭제",
        description = """
        댓글을 삭제합니다.
        댓글 작성자만 삭제할 수 있습니다.
        (fake api는 미구현 기능에 대해서 프론트 테스트를 위해 어떤 요청값이든 고정된 응답값만 반환하도록 설계되어 있습니다.)
    """
    )
    fun deleteComment(
        @Schema(description = "댓글 id", example = "325") @PathVariable commentId: Long
    ): ApiResult<Unit>
}
