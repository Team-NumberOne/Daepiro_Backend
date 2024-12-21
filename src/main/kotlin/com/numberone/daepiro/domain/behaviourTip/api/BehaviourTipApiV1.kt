package com.numberone.daepiro.domain.behaviourTip.api

import com.numberone.daepiro.domain.behaviourTip.dto.response.BehaviourTipDisasterResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "BehaviourTip API", description = "행동요령 관련 API")
@RequestMapping("/v1/behaviourtips")
interface BehaviourTipApiV1 {
    @GetMapping("/list/{type}")
    @Operation(summary = "행동요령 재난유형 목록 조회", description = "행동요령 페이지에서 제공하는 재난 유형 목록을 가나다 순으로 제공합니다.")
    fun getBehaviourTips(
        @Schema(description = "행동요령 유형 (emergency | common)", example = "emergency") type: String
    ): ApiResult<List<BehaviourTipDisasterResponse>>
}
