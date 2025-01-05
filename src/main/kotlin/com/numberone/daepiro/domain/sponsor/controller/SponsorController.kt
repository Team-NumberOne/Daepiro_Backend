package com.numberone.daepiro.domain.sponsor.controller

import com.numberone.daepiro.domain.sponsor.api.SponsorApiV1
import com.numberone.daepiro.domain.sponsor.dto.request.CheeringRequest
import com.numberone.daepiro.domain.sponsor.dto.request.CreateSponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.request.SponsorRequest
import com.numberone.daepiro.domain.sponsor.dto.response.CheeringResponse
import com.numberone.daepiro.domain.sponsor.dto.response.SponsorResponse
import com.numberone.daepiro.domain.sponsor.service.SponsorService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
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

    override fun sponsor(id: Long, request: SponsorRequest): ApiResult<Unit> {
        sponsorService.sponsor(id, request)
        return ApiResult.ok()
    }

    override fun createCheering(request: CheeringRequest): ApiResult<Unit> {
        val userId = SecurityContextUtils.getUserId()
        sponsorService.createCheering(request,userId)
        return ApiResult.ok()
    }

    override fun updateCheering(id: Long, request: CheeringRequest): ApiResult<Unit> {
        val userId = SecurityContextUtils.getUserId()
        sponsorService.updateCheering(id, request, userId)
        return ApiResult.ok()
    }

    override fun getCheering(): ApiResult<List<CheeringResponse>> {
        val userId = SecurityContextUtils.getUserId()
        return sponsorService.getCheering(userId)
    }

    override fun getMessages(): ApiResult<List<CheeringResponse>> {
        val userId = SecurityContextUtils.getUserId()
        return sponsorService.getMessages(userId)
    }
}
