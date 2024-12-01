package com.numberone.daepiro.domain.shelter.api

import com.numberone.daepiro.domain.shelter.dto.response.GetNearbySheltersResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Shelter API", description = "대피소 관련 API")
@RequestMapping("/v1/shelters")
interface ShelterApiV1 {
    @GetMapping("/{type}")
    @Operation(
        summary = "주변 대피소 목록 조회", description = """
        사용자의 주변 대피소 목록을 조회합니다.
        쿼리 파라미터로 대피소 유형을 지정해주세요. (temperature: 쉼터, earthquake: 지진, tsunami: 지진해일, civil: 민방위)
        이 api 호출 이전에 gps 정보 업데이트 api를 통해 사용자의 위치 정보를 업데이트 해주세요.
        """
    )
    fun getNearbyShelters(
        @Schema(
            description = "대피소 유형 (temperature | earthquake | tsunami | civil)",
            example = "temperature"
        ) @PathVariable type: String,
    ): ApiResult<GetNearbySheltersResponse>
}
