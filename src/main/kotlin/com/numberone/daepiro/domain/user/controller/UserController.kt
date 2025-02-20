package com.numberone.daepiro.domain.user.controller

import com.numberone.daepiro.domain.user.api.UserApiV1
import com.numberone.daepiro.domain.user.dto.request.DeleteUserRequest
import com.numberone.daepiro.domain.user.dto.request.OnboardingRequest
import com.numberone.daepiro.domain.user.dto.request.UpdateFcmTokenRequest
import com.numberone.daepiro.domain.user.dto.request.UpdateGpsRequest
import com.numberone.daepiro.domain.user.dto.response.CheckNicknameResponse
import com.numberone.daepiro.domain.user.dto.response.DisasterWithRegionResponse
import com.numberone.daepiro.domain.user.dto.response.GetUserResponse
import com.numberone.daepiro.domain.user.dto.response.NotificationResponse
import com.numberone.daepiro.domain.user.dto.response.UserAddressResponse
import com.numberone.daepiro.domain.user.service.UserService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) : UserApiV1 {
    override fun getUser(): ApiResult<GetUserResponse> {
        return ApiResult.ok(GetUserResponse.fake(), "/users/v1")
    }

    override fun checkNickname(
        nickname: String
    ): ApiResult<CheckNicknameResponse> {
        return userService.checkNickname(nickname)
    }

    override fun setOnboardingData(
        request: OnboardingRequest
    ): ApiResult<List<UserAddressResponse>> {
        return userService.setOnboardingData(
            request,
            SecurityContextUtils.getUserId()
        )
    }

    override fun updateGps(
        request: UpdateGpsRequest
    ): ApiResult<Unit> {
        return userService.updateGps(
            request,
            SecurityContextUtils.getUserId()
        )
    }

    override fun getRecentDisasters(): ApiResult<List<DisasterWithRegionResponse>> {
        return userService.getRecentDisasters(SecurityContextUtils.getUserId())
    }

    override fun getAddresses(): ApiResult<List<UserAddressResponse>> {
        return userService.getAddresses(SecurityContextUtils.getUserId())
    }

    override fun deleteUser(reason: String,request:DeleteUserRequest): ApiResult<Unit> {
        userService.deleteUser(SecurityContextUtils.getUserId(),reason,request.appleCode)
        return ApiResult.ok()
    }

    override fun updateFcmToken(request: UpdateFcmTokenRequest): ApiResult<Unit> {
        userService.updateFcmToken(
            request,
            SecurityContextUtils.getUserId()
        )
        return ApiResult.ok()
    }

    override fun logout(): ApiResult<Unit> {
        userService.logout(SecurityContextUtils.getUserId())
        return ApiResult.ok()
    }

    override fun getNotifications(): ApiResult<List<NotificationResponse>> {
        return userService.getNotifications(SecurityContextUtils.getUserId())
    }


}
