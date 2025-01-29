package com.numberone.daepiro.domain.mypage.service

import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.mypage.dto.request.GetMyArticleRequest
import com.numberone.daepiro.domain.mypage.dto.response.MyAddressesResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyDisasterTypesResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyNotificationResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyProfileResponse
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MyPageService(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository
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
        return ApiResult.ok(articleRepository.getMyArticles(userId,request))
    }
}
