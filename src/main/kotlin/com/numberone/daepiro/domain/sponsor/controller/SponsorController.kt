package com.numberone.daepiro.domain.sponsor.controller

import com.numberone.daepiro.domain.sponsor.api.SponsorApiV1
import com.numberone.daepiro.domain.sponsor.dto.request.CreateSponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.response.SponsorResponse
import com.numberone.daepiro.domain.sponsor.service.SponsorService
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.web.bind.annotation.RestController

@RestController
class SponsorController(
    private val sponsorService: SponsorService
) :SponsorApiV1{
    override fun getSponsors(): ApiResult<List<SponsorResponse>> {
        return sponsorService.getSponsors()
    }

    override fun getSponsor(id: Long): ApiResult<SponsorResponse> {
        return sponsorService.getSponsor(id)
    }

    override fun createSponsor(request: CreateSponsorRequest): ApiResult<Unit> {
        sponsorService.createSponsorsArticle(request)
        return ApiResult.ok()
    }
}
