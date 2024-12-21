package com.numberone.daepiro.domain.behaviourTip.api

import com.numberone.daepiro.domain.behaviourTip.dto.request.CreateTipRequest
import com.numberone.daepiro.domain.behaviourTip.dto.response.BehaviourTipDisasterResponse
import com.numberone.daepiro.domain.behaviourTip.dto.response.GetBehaviourTipResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "BehaviourTip API", description = "행동요령 관련 API")
@RequestMapping("/v1/behaviourtips")
interface BehaviourTipApiV1 {
    @GetMapping("/list/{type}")
    @Operation(summary = "행동요령 재난유형 목록 조회", description = "행동요령 페이지에서 제공하는 재난 유형 목록을 가나다 순으로 제공합니다.")
    fun getBehaviourTips(
        @Schema(description = "행동요령 유형 (emergency | common)", example = "emergency") type: String
    ): ApiResult<List<BehaviourTipDisasterResponse>>

    @GetMapping("/search/{keyword}")
    @Operation(summary = "재난유형 검색", description = "행동요령 페이지에서 제공하는 재난 유형을 검색합니다.")
    fun searchBehaviourTips(
        @Schema(description = "검색어", example = "가") keyword: String
    ): ApiResult<List<BehaviourTipDisasterResponse>>

    @PostMapping
    @Operation(summary = "행동요령 생성", description = "행동요령을 생성합니다.")
    fun createBehaviourTip(
        request: CreateTipRequest
    ):ApiResult<Unit>

    @GetMapping("/{disasterId}")
    @Operation(summary = "재난유형에 대한 행동요령 조회", description = "재난유형에 대한 행동요령 조회합니다.")
    fun getBehaviourTip(
        @Schema(description = "재난유형 id", example = "26") disasterTypeId: Long
    ): ApiResult<GetBehaviourTipResponse>
}
