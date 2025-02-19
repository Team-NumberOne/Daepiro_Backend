package com.numberone.daepiro.global.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.numberone.daepiro.global.exception.CustomErrorContext
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ApiResult<T>(
    @JsonIgnore
    val httpStatus: HttpStatus = HttpStatus.OK,
    val code: Int = HttpStatus.OK.value(),
    val message: String = "",
    val data: T? = null,
    val path: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun <T> ok(
            data: T? = null,
            path: String = "",
            message: String = "Success"
        ) =
            ApiResult(
                httpStatus = HttpStatus.OK,
                code = 1000,
                message = message,
                data = data,
                path = path
            )

        fun noContent(
            path: String = "",
            message: String = "Success"
        ) =
            ApiResult<Unit>(
                httpStatus = HttpStatus.OK,
                code = 1000,
                message = message,
                path = path
            )

        fun error(
            context: CustomErrorContext,
            path: String = "",
            additionalMsg: String = ""
        ) =
            ApiResult<Unit>(
                httpStatus = context.httpStatus,
                code = context.code,
                message = context.message + additionalMsg,
                path = path
            )
    }
}
