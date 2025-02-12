package com.numberone.daepiro.domain.community.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Size

data class GetAvailablePositionRequest(
    @Schema(description = "주소", example = "서울특별시 영등포구 신길동")
    @field:Size(min = 1, message = "주소를 입력해주세요.")
    val address: String,

    @Schema(description = "경도", example = "126.921212")
    @field:DecimalMin(value = "124.0", message = "대한민국 주소의 경도를 입력해주세요.")
    @field:DecimalMax(value = "132.0", message = "대한민국 주소의 경도를 입력해주세요.")
    val longitude: Double,

    @Schema(description = "위도", example = "37.508121")
    @field:DecimalMin(value = "33.0", message = "대한민국 주소의 위도를 입력해주세요.")
    @field:DecimalMax(value = "43.0", message = "대한민국 주소의 위도를 입력해주세요.")
    val latitude: Double
)
