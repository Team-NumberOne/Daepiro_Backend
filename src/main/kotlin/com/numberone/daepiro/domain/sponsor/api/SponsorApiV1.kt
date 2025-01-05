package com.numberone.daepiro.domain.sponsor.api

import com.numberone.daepiro.domain.sponsor.dto.request.CreateSponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.request.SponsorRequest
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
}
