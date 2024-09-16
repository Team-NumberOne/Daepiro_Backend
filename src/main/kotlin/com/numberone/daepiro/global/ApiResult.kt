package com.numberone.daepiro.global

import com.numberone.daepiro.global.exception.CustomErrorContext
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ApiResult<T>(
    val code: Int = HttpStatus.OK.value(),
    val message: String = "",
    val data: T? = null,
    val path: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun <T> ok(data: T?, path: String = "", message: String = "Success") =
            ApiResult(message = message, data = data, path = path)

        fun <T> body(data: T?, path: String, code: Int) =
            ApiResult(code = code, data = data, path = path)

        fun <T> error(path: String, message: String, status: HttpStatus = HttpStatus.BAD_REQUEST) =
            ApiResult<T>(code = status.value(), message = message, path = path)

        fun <T> error(context: CustomErrorContext, path: String = "") =
            ApiResult<T>(code = context.code, message = context.message, path = path)
    }
}
