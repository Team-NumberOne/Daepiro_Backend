package com.numberone.daepiro.domain.dataCollecter.api

import com.numberone.daepiro.domain.dataCollecter.dto.request.SaveDisastersRequest
import com.numberone.daepiro.domain.dataCollecter.dto.request.SaveNewsRequest
import com.numberone.daepiro.domain.dataCollecter.dto.response.GetLatestDisasterResponse
import com.numberone.daepiro.domain.dataCollecter.dto.response.GetLatestNewsResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Data Collector API", description = "데이터 수집용 API")
@RequestMapping("/v1/datacollector")
interface DataCollectorApiV1 {
    @GetMapping("/news/latest")
    @Operation(summary = "최근 저장한 뉴스 정보 조회", description = "최근 저장한 뉴스 정보를 조회합니다.")
    fun getLatestNews(): ApiResult<GetLatestNewsResponse>

    @GetMapping("/disasters/latest")
    @Operation(summary = "최근 저장한 재난 정보 조회", description = "최근 저장한 재난 정보를 조회합니다.")
    fun getLatestDisasters(): ApiResult<GetLatestDisasterResponse>

    @PostMapping("/news")
    @Operation(summary = "뉴스 정보 저장", description = "뉴스 정보를 저장합니다.")
    fun saveNews(
        @RequestBody request: SaveNewsRequest
    ): ApiResult<Unit>

    @PostMapping("/disasters")
    @Operation(summary = "재난 정보 저장", description = "재난 정보를 저장합니다.")
    fun saveDisasters(
        @RequestBody request: SaveDisastersRequest
    ): ApiResult<Unit>
}
