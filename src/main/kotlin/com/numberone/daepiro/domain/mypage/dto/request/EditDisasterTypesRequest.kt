package com.numberone.daepiro.domain.mypage.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class EditDisasterTypesRequest(
    @Schema(
        description = "재난 유형",
        example = "[\"지진\", \"화재\", \"태풍\"]",
    )
    val disasterTypes: List<String>,
)
