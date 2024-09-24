package com.numberone.daepiro.global.feign.dto

data class NaverUserInfo(
    val response: Response
) {
    data class Response(
        val id: String
    )
}
