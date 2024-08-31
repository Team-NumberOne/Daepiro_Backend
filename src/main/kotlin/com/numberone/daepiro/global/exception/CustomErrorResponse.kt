package com.numberone.daepiro.global.exception

import java.time.LocalDateTime

data class CustomErrorResponse(
    val code: Int,
    val message: String,
    val endpoint: String?,
    val timestamp: LocalDateTime
) {
    companion object {
        fun of(context: CustomErrorContext, path: String?): CustomErrorResponse {
            return CustomErrorResponse(context.code, context.message, path, LocalDateTime.now())
        }
    }
}