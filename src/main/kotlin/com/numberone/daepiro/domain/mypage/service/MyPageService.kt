package com.numberone.daepiro.domain.mypage.service

import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.mypage.dto.request.EditAddressesRequest
import com.numberone.daepiro.domain.mypage.dto.request.EditDisasterTypesRequest
import com.numberone.daepiro.domain.mypage.dto.request.EditProfileRequest
import com.numberone.daepiro.domain.mypage.dto.request.GetMyArticleRequest
import com.numberone.daepiro.domain.mypage.dto.response.MyAddressesResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyDisasterTypesResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyNotificationResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyProfileResponse
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.domain.user.service.UserService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_NOTIFICATION_TYPE
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MyPageService(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val userService: UserService
) {
    fun getMyProfile(userId: Long): ApiResult<MyProfileResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        return ApiResult.ok(MyProfileResponse.of(user))
    }

    fun getMyNotification(userId: Long): ApiResult<MyNotificationResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        return ApiResult.ok(MyNotificationResponse.of(user))
    }

    fun getMyAddresses(userId: Long): ApiResult<MyAddressesResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        return ApiResult.ok(MyAddressesResponse.of(user))
    }

    fun getMyDisasterTypes(userId: Long): ApiResult<MyDisasterTypesResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        return ApiResult.ok(MyDisasterTypesResponse.of(user))
    }

    fun getMyArticles(userId: Long, request: GetMyArticleRequest): ApiResult<Slice<ArticleListResponse>> {
        return ApiResult.ok(articleRepository.getMyArticles(userId, request))
    }

    @Transactional
    fun updateMyProfile(userId: Long, request: EditProfileRequest) {
        val user = userRepository.findByIdOrThrow(userId)
        user.initName(request.realname, request.nickname)
    }

    @Transactional
    fun updateMyNotification(userId: Long, type: String) {
        val user = userRepository.findByIdOrThrow(userId)
        when (type) {
            "community" -> user.isCommunityNotificationEnabled = !user.isCommunityNotificationEnabled
            "disaster" -> user.isDisasterNotificationEnabled = !user.isDisasterNotificationEnabled
            else -> throw CustomException(NOT_FOUND_NOTIFICATION_TYPE)
        }
    }

    @Transactional
    fun updateMyAddresses(userId: Long, request: EditAddressesRequest) {
        val user = userRepository.findByIdOrThrow(userId)
        userService.handleOnboardingAddress(request.addresses, user)
    }

    @Transactional
    fun updateMyDisasterTypes(userId: Long, request: EditDisasterTypesRequest) {
        val user = userRepository.findByIdOrThrow(userId)
        userService.handleOnboardingDisasterType(request.disasterTypes, user)
    }
}
