package com.numberone.daepiro.domain.auth.dto.request

data class AdminLoginRequest(
    val username: String,
    val password: String,
)
