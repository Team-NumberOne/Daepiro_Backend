package com.numberone.daepiro.domain.home.api

import com.numberone.daepiro.domain.behaviourTip.dto.response.GetBehaviourTipResponse
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.dataCollecter.dto.response.GetLatestNewsResponse
import com.numberone.daepiro.domain.disasterContent.dto.response.DisasterContentResponse
import com.numberone.daepiro.domain.disasterContent.dto.response.GetHomeDisasterContentsResponse
import com.numberone.daepiro.domain.home.dto.request.GetHomeArticleRequest
import com.numberone.daepiro.domain.home.dto.response.GetStatusResponse
import com.numberone.daepiro.domain.home.dto.response.GetWarningResponse
import com.numberone.daepiro.domain.home.dto.response.GetWeatherResponse
import com.numberone.daepiro.domain.home.dto.response.HomeDisasterFeed
import com.numberone.daepiro.domain.shelter.dto.response.GetNearbySheltersResponse
import com.numberone.daepiro.domain.user.dto.request.UpdateGpsRequest
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Home API", description = "홈 피드 관련 API")
@RequestMapping("/v1/home")
interface HomeApiV1 {
    @GetMapping("/disasters")
    @Operation(summary = "홈 재난문자 내역 피드 조회", description = "홈 화면에 표시할 재난문자 내역을 조회합니다.")
    fun getHomeDisasters(): ApiResult<List<HomeDisasterFeed>>

    @GetMapping("/news")
    @Operation(summary = "(평상 시) 홈 최신 정보콘텐츠 피드 조회", description = "홈 화면에 표시할 최신 정보콘텐츠를 조회합니다.")
    fun getHomeNews(): ApiResult<GetHomeDisasterContentsResponse>

    @GetMapping("/articles")
    @Operation(summary = "(평상 시) 홈 동네생활 인기게시글 피드 조회", description = "홈 화면에 표시할 인기 게시글을 조회합니다.")
    fun getHomeArticles(
        @ModelAttribute request: GetHomeArticleRequest
    ): ApiResult<List<ArticleListResponse>>

    @GetMapping("/warnings")
    @Operation(summary = "(재난 발생 시) 홈 현재 발생 재난 피드 조회", description = "홈 화면에 표시할 현재 발생 중인 재난을 조회합니다.")
    fun getWarning(): ApiResult<GetWarningResponse>

    @GetMapping("/status")
    @Operation(summary = "현재 재난 발생 유무 조회", description = "현재 재난이 발생 중인지 조회합니다.")
    fun getStatus(): ApiResult<GetStatusResponse>

    @GetMapping("/shelters/{type}")
    @Operation(summary = "(재난 발생 시) 홈 대피소 피드 조회", description = "대피소 목록을 조회합니다.")
    fun getShelters(
        @Schema(
            description = "대피소 유형 (temperature | earthquake | tsunami | civil)",
            example = "temperature"
        ) @PathVariable type: String,
    ): ApiResult<GetNearbySheltersResponse>

    @GetMapping("/tip/{disasterId}")
    @Operation(
        summary = "(재난 발생 시) 홈 행동요령 피드 조회", description = """
        홈 행동요령 피드를 조회합니다.
        현재 발생한 재난 조회 api에서 얻은 재난유형 id값을 입력해주세요.
        """
    )
    fun getBehaviourTip(
        @Schema(description = "현재 발생한 재난의 재난유형 id", example = "26") @PathVariable disasterId: Long
    ): ApiResult<GetBehaviourTipResponse>

    @GetMapping("/weather")
    @Operation(
        summary = "(평상 시) 홈 GPS 및 날씨 정보 피드 조회", description = """
            홈 화면에 표시할 GPS와 날씨 정보를 조회합니다.
            (참고!)날씨정보는 아직 외부api와 연동을 하지않아 항상 맑음으로 표기됩니다.
            """
    )
    fun getWeather():ApiResult<GetWeatherResponse>
}
