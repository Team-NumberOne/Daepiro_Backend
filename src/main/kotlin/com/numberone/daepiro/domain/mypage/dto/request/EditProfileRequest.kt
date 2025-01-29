package com.numberone.daepiro.domain.mypage.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class EditProfileRequest(
    @Schema(description = "변경할 실명", example = "송승희")
    val realname: String,

    @Schema(description = "변경할 닉네임 (중복체크 필요)", example = "초코송이")
    val nickname: String,
)
