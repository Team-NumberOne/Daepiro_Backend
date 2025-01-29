package com.numberone.daepiro.domain.mypage.api

import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.mypage.dto.request.EditAddressesRequest
import com.numberone.daepiro.domain.mypage.dto.request.EditDisasterTypesRequest
import com.numberone.daepiro.domain.mypage.dto.request.EditProfileRequest
import com.numberone.daepiro.domain.mypage.dto.request.GetMyArticleRequest
import com.numberone.daepiro.domain.mypage.dto.response.MyAddressesResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyDisasterTypesResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyNotificationResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyProfileResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "MyPage API", description = "마이페이지 API")
@RequestMapping("/v1/my-page")
interface MyPageApiV1 {
    @GetMapping("/profiles")
    @Operation(summary = "마이페이지 프로필 조회", description = "마이페이지 프로필을 조회합니다.")
    fun getMyProfile(): ApiResult<MyProfileResponse>

    @GetMapping("/notifications")
    @Operation(summary = "마이페이지 알림 설정 조회", description = "마이페이지 알림 설정을 조회합니다.")
    fun getMyNotification(): ApiResult<MyNotificationResponse>

    @GetMapping("/addresses")
    @Operation(summary = "마이페이지 재난알림 지역 설정 조회", description = "마이페이지 재난알림 지역 설정을 조회합니다.")
    fun getMyAddresses(): ApiResult<MyAddressesResponse>

    @GetMapping("/disaster-types")
    @Operation(summary = "마이페이지 재난유형 설정 조회", description = "마이페이지 재난유형 설정을 조회합니다.")
    fun getMyDisasterTypes(): ApiResult<MyDisasterTypesResponse>

    @GetMapping("/articles")
    @Operation(summary = "마이페이지 내가 쓴 글 조회", description = "마이페이지 내가 쓴 글을 조회합니다. page는 1부터 시작합니다.")
    fun getMyArticles(
        @ModelAttribute request: GetMyArticleRequest
    ): ApiResult<Slice<ArticleListResponse>>

//    @GetMapping("/announcement")
//    @Operation(summary = "마이페이지 공지사항 조회", description = "마이페이지 공지사항을 조회합니다.")
//    fun getAnnouncement():ApiResult<>

    @PutMapping("/profiles")
    @Operation(summary = "마이페이지 프로필 수정", description = "프로필을 수정합니다.")
    fun updateMyProfile(
        @RequestBody request: EditProfileRequest
    ): ApiResult<Unit>

    @PutMapping("/notifications/{type}")
    @Operation(
        summary = "마이페이지 알림 설정 수정", description = """
        커뮤니티 또는 재난 알림 수신 유무를 변경합니다.
        on이면 off로, off면 on으로 변경됩니다.
        """
    )
    fun updateMyNotification(
        @Schema(description = "알림 타입 (community 또는 disaster)", example = "community") @PathVariable type: String
    ): ApiResult<Unit>

    @PutMapping("/addresses")
    @Operation(summary = "마이페이지 재난알림 지역 설정 수정", description = "재난알림 지역 설정을 수정합니다.")
    fun updateMyAddresses(
        @RequestBody request: EditAddressesRequest
    ): ApiResult<Unit>

    @PutMapping("/disaster-types")
    @Operation(summary = "마이페이지 재난유형 설정 수정", description = "재난유형 설정을 수정합니다.")
    fun updateMyDisasterTypes(
        @RequestBody request: EditDisasterTypesRequest
    ): ApiResult<Unit>
}
