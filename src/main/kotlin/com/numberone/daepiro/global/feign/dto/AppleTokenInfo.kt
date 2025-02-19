package com.numberone.daepiro.global.feign.dto

data class AppleTokenInfo(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String,
    val id_token: String
)
