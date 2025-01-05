package com.numberone.daepiro.domain.sponsor.api

import com.numberone.daepiro.domain.sponsor.dto.request.CheeringRequest
import com.numberone.daepiro.domain.sponsor.dto.request.CreateSponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.request.SponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.response.CheeringResponse
import com.numberone.daepiro.domain.sponsor.dto.response.SponsorResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Sponsor API", description = "후원페이지 API")
@RequestMapping("/v1/sponsors")
interface SponsorApiV1 {
    @GetMapping("/articles")
    @Operation(
        summary = "후원글 목록 조회",
        description = """
        후원글 목록을 조회합니다.
    """
    )
    fun getSponsors(): ApiResult<List<SponsorResponse>>

    @GetMapping("/articles/{id}")
    @Operation(
        summary = "후원글 상세 조회",
        description = """
        후원글 상세를 조회합니다.
    """
    )
    fun getSponsor(
        @Schema(description = "후원글 id", example = "793") @PathVariable id: Long
    ): ApiResult<SponsorResponse>

    @PostMapping
    @Operation(
        summary = "후원글 작성",
        description = """
        후원글을 작성합니다.
    """
    )
    fun createSponsor(
        @RequestBody request: CreateSponsorRequest
    ): ApiResult<Unit>

    @PutMapping("/articles/{id}")
    @Operation(
        summary = "후원하기",
        description = """
        후원을 합니다.
    """
    )
    fun sponsor(
        @Schema(description = "후원글 id", example = "793") @PathVariable id: Long,
        @RequestBody @Valid request: SponsorRequest
    ): ApiResult<Unit>

    @PostMapping("/cheering")
    @Operation(
        summary = "응원하기",
        description = """
        응원을 합니다.
    """
    )
    fun createCheering(
        @RequestBody @Valid request: CheeringRequest
    ): ApiResult<Unit>

    @PutMapping("/cheering/{id}")
    @Operation(
        summary = "응원 메세지 수정",
        description = """
        응원 메세지 내용을 수정합니다. 본인이 작성한 응원 메세지만 수정 가능합니다.
    """
    )
    fun updateCheering(
        @Schema(description = "응원 id", example = "2") @PathVariable id: Long,
        @RequestBody @Valid request: CheeringRequest
    ): ApiResult<Unit>

    @GetMapping("/cheering")
    @Operation(
        summary = "전체 응원 메세지 목록 조회",
        description = """
        응원의 한마디 페이지에서 사용될 전체 응원 메세지 목록을 조회합니다.
    """
    )
    fun getCheering(): ApiResult<List<CheeringResponse>>

    @GetMapping("/messages")
    @Operation(
        summary = "후원 홈 상단 응원 메세지 목록 조회",
        description = """
        후원 페이지 홈 상단에 표시될 응원 메세지 목록을 조회합니다.
    """
    )
    fun getMessages(): ApiResult<List<CheeringResponse>>
}
