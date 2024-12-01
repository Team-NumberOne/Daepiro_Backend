package com.numberone.daepiro.domain.disasterSituation.api

import com.numberone.daepiro.domain.disasterSituation.dto.response.DisasterSituationResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Disaster Situation API", description = "재난 상황용 API")
@RequestMapping("/v1/disastersituations")
interface DisasterSituationApiV1 {
    @GetMapping
    @Operation(summary = "재난상황 글 목록 조회", description = """
        재난상황 글 목록을 조회합니다.
        프론트에서 수신/전체 필터링은 반환된 리스트 안에 각 데이터의 isReceived 필드를 이용하여 구분하도록 구현하였습니다.
    """)
    fun getDisasterSituations(): ApiResult<List<DisasterSituationResponse>>
}
