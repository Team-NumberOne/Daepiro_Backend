package com.numberone.daepiro.domain.user.api

import com.numberone.daepiro.domain.user.dto.request.OnboardingRequest
import com.numberone.daepiro.domain.user.dto.request.UpdateGpsRequest
import com.numberone.daepiro.domain.user.dto.response.CheckNicknameResponse
import com.numberone.daepiro.domain.user.dto.response.DisasterWithRegionResponse
import com.numberone.daepiro.domain.user.dto.response.GetUserResponse
import com.numberone.daepiro.domain.user.dto.response.UserAddressResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "User API", description = "회원 관련 API")
@RequestMapping("/v1/users")
interface UserApiV1 {
    @GetMapping
    @Operation(summary = "Get user test", description = "Get user test")
    fun getUser(): ApiResult<GetUserResponse>

    @GetMapping("/nickname/{nickname}")
    @Operation(summary = "닉네임 중복 검사", description = "해당 닉네임으로 가입한 사용자가 이미 있는지 검사합니다.")
    fun checkNickname(
        @Schema(description = "닉네임", example = "초코송이") @PathVariable nickname: String,
    ): ApiResult<CheckNicknameResponse>

    @PutMapping("/onboarding")
    @Operation(summary = "온보딩 정보 입력", description = "사용자의 온보딩 정보를 등록합니다.")
    fun setOnboardingData(
        @RequestBody @Valid request: OnboardingRequest
    ): ApiResult<List<UserAddressResponse>>

    @PostMapping("/gps")
    @Operation(summary = "GPS 정보 업데이트", description = "사용자의 GPS 정보를 업데이트합니다.")
    fun updateGps(
        @RequestBody @Valid request: UpdateGpsRequest
    ): ApiResult<Unit>

    @GetMapping("/disasters")
    @Operation(summary = "최근 재난문자 내역 조회", description = "사용자의 최근 재난문자 내역을 조회합니다.")
    fun getRecentDisasters(): ApiResult<List<DisasterWithRegionResponse>>

    @GetMapping("/addresses")
    @Operation(summary = "사용자 수신 설정 주소 조회", description = """
        사용자가 수신 설정한 주소들을 조회합니다.
        응답 값중 fullAddress는 동네생활 게시글 조회 api 호출시 입력 값으로 사용됩니다.
        shortAddress는 동네생활 프론트 UI에서 사용됩니다.
    """)
    fun getAddresses(): ApiResult<List<UserAddressResponse>>

    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    fun deleteUser(): ApiResult<Unit>
}
