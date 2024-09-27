package com.numberone.daepiro.domain.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

data class OnboardingRequest(
    @Schema(description = "실명", example = "송승희")
    @field:Size(min = 1, max = 6, message = "실명은 1자 이상 6자 이하로 입력해주세요.")
    val realname: String,

    @Schema(description = "닉네임", example = "초코송이")
    @field:Size(min = 1, max = 10, message = "닉네임은 1자 이상 10자 이하로 입력해주세요.")
    val nickname: String,

    @field:Size(min = 1, max = 3, message = "주소는 1개 이상 3개 이하로 입력해주세요.")
    val addresses: List<AddressRequest>,

    @Schema(
        description = "재난 유형",
        example = "[\"지진\", \"화재\", \"태풍\"]",
    )
    @field:Size(min = 1, message = "재난 유형은 1개 이상 선택해주세요.")
    val disasterTypes: List<String>
)

data class AddressRequest(
    @Schema(description = "이름", example = "집")
    @field:Size(min = 1, message = "이름을 입력해주세요.")
    val name: String,

    @Schema(description = "주소", example = "서울특별시 영등포구 신길동")
    @field:Size(min = 1, message = "주소를 입력해주세요.")
    val address: String
)
