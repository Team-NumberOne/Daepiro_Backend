package com.numberone.daepiro.domain.home.api

import com.numberone.daepiro.domain.dataCollecter.dto.response.GetLatestNewsResponse
import com.numberone.daepiro.domain.disasterContent.dto.response.DisasterContentResponse
import com.numberone.daepiro.domain.disasterContent.dto.response.GetHomeDisasterContentsResponse
import com.numberone.daepiro.domain.home.dto.response.GetStatusResponse
import com.numberone.daepiro.domain.home.dto.response.GetWarningResponse
import com.numberone.daepiro.domain.home.dto.response.HomeDisasterFeed
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Home API", description = "홈 피드 관련 API")
@RequestMapping("/v1/home")
interface HomeApiV1 {
    @GetMapping("/disasters")
    @Operation(summary = "(평상 시) 홈 재난문자 내역 피드 조회", description = "홈 화면에 표시할 재난문자 내역을 조회합니다.")
    fun getHomeDisasters(): ApiResult<List<HomeDisasterFeed>>

    @GetMapping("/news")
    @Operation(summary = "(평상 시) 홈 최신 정보콘텐츠 피드 조회", description = "홈 화면에 표시할 최신 정보콘텐츠를 조회합니다.")
    fun getHomeNews(): ApiResult<GetHomeDisasterContentsResponse>

    @GetMapping("/warnings")
    @Operation(summary = "(재난 발생 시) 홈 현재 발생 재난 피드 조회", description = "홈 화면에 표시할 현재 발생 중인 재난을 조회합니다.")
    fun getWarning(): ApiResult<GetWarningResponse>

    @GetMapping("/status")
    @Operation(summary = "현재 재난 발생 유무 조회", description = "현재 재난이 발생 중인지 조회합니다.")
    fun getStatus(): ApiResult<GetStatusResponse>
}
