package com.numberone.daepiro.domain.mypage.dto.response

import com.numberone.daepiro.domain.user.entity.UserEntity
import io.swagger.v3.oas.annotations.media.Schema

data class MyProfileResponse(
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    val profileImgUrl: String,

    @Schema(description = "실명", example = "송승희")
    val realname: String,

    @Schema(description = "닉네임", example = "초코송이")
    val nickname: String,
) {
    companion object {
        fun of(
            user: UserEntity
        ): MyProfileResponse {
            return MyProfileResponse(
                profileImgUrl = user.profileImageUrl!!,
                realname = user.realname!!,
                nickname = user.nickname!!,
            )
        }
    }
}
