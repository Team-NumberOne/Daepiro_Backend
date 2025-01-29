package com.numberone.daepiro.domain.mypage.controller

import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.mypage.api.MyPageApiV1
import com.numberone.daepiro.domain.mypage.dto.request.GetMyArticleRequest
import com.numberone.daepiro.domain.mypage.dto.response.MyAddressesResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyDisasterTypesResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyNotificationResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyProfileResponse
import com.numberone.daepiro.domain.mypage.service.MyPageService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.RestController

@RestController
class MyPageController(
    private val myPageService: MyPageService
) : MyPageApiV1 {
    override fun getMyProfile(): ApiResult<MyProfileResponse> {
        val userId = SecurityContextUtils.getUserId()
        return myPageService.getMyProfile(userId)
    }

    override fun getMyNotification(): ApiResult<MyNotificationResponse> {
        val userId = SecurityContextUtils.getUserId()
        return myPageService.getMyNotification(userId)
    }

    override fun getMyAddresses(): ApiResult<MyAddressesResponse> {
        val userId = SecurityContextUtils.getUserId()
        return myPageService.getMyAddresses(userId)
    }

    override fun getMyDisasterTypes(): ApiResult<MyDisasterTypesResponse> {
        val userId = SecurityContextUtils.getUserId()
        return myPageService.getMyDisasterTypes(userId)
    }

    override fun getMyArticles(
        request: GetMyArticleRequest
    ): ApiResult<Slice<ArticleListResponse>> {
        val userId = SecurityContextUtils.getUserId()
        return myPageService.getMyArticles(userId, request)
    }
}
