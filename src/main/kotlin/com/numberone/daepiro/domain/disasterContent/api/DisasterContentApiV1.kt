package com.numberone.daepiro.domain.disasterContent.api

import com.numberone.daepiro.domain.disasterContent.dto.response.GetDisasterContentsResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "Disaster Content API", description = "재난 콘텐츠용 API")
@RequestMapping("/v1/disastercontents")
interface DisasterContentApiV1 {
    @GetMapping("/list/{sortType}")
    @Operation(summary = "재난 콘텐츠 목록 조회", description = "커서 기반 페이징 방식을 이용해 재난 콘텐츠 목록을 조회합니다.")
    fun getDisasterContents(
        @Schema(description = "정렬 방식 (latest | popular)", example = "latest")
        @PathVariable sortType: String,

        @Schema(description = "cursor 값에는 가장 최근에 이 api를 호출했을 때 반환된 nextCursor 값을 입력해주세요. cursor 값이 없을 경우 처음 페이지를 조회합니다.", example = "991")
        @RequestParam(required = false) cursor: Long?,

        @Schema(description = "조회할 콘텐츠 수", example = "20")
        @RequestParam size: Long
    ): ApiResult<GetDisasterContentsResponse>

    @GetMapping("/search/{keyword}")
    @Operation(summary = "재난 콘텐츠 검색", description = "키워드를 이용해 재난 콘텐츠를 검색합니다.")
    fun searchDisasterContents(
        @Schema(description = "검색할 키워드", example = "코로나")
        @PathVariable keyword: String,

        @Schema(description = "cursor 값에는 가장 최근에 이 api를 호출했을 때 반환된 nextCursor 값을 입력해주세요. cursor 값이 없을 경우 처음 페이지를 조회합니다.", example = "991")
        @RequestParam(required = false) cursor: Long?,

        @Schema(description = "조회할 콘텐츠 수", example = "20")
        @RequestParam size: Long
    ): ApiResult<GetDisasterContentsResponse>
}
