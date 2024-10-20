package com.numberone.daepiro.global.feign.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoLocalAddress(
    val meta: KakaoLocalMeta,
    val documents: List<KakaoLocalDocument>
)

data class KakaoLocalDocument(
    @JsonProperty("region_type")
    val regionType: String,

    val code: String,

    @JsonProperty("address_name")
    val address: String,

    @JsonProperty("region_1depth_name")
    val siDo: String,

    @JsonProperty("region_2depth_name")
    val siGunGu: String,

    @JsonProperty("region_3depth_name")
    val eupMyeonDong: String,

    @JsonProperty("region_4depth_name")
    val additionalAddress: String,

    val x: Double,

    val y: Double
)

data class KakaoLocalMeta(
    @JsonProperty("total_count")
    val totalCount: Int
)
