package com.numberone.daepiro.domain.user.service

import com.numberone.daepiro.domain.user.dto.request.CheckNicknameRequest
import com.numberone.daepiro.domain.user.dto.request.OnboardingRequest
import com.numberone.daepiro.domain.user.dto.response.CheckNicknameResponse
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {
    fun checkNickname(
        request: CheckNicknameRequest
    ): ApiResult<CheckNicknameResponse> {
        val user = userRepository.findByNickname(request.nickname)
        return ApiResult.ok(CheckNicknameResponse.of(user == null))
    }

    @Transactional
    fun setOnboardingData(
        request: OnboardingRequest
    ): ApiResult<Unit> {
        val userId = SecurityContextUtils.getUserId()
        val user = userRepository.findByIdOrThrow(userId)
        user.initName(request.realname, request.nickname)
        return ApiResult.ok()
    }
}
