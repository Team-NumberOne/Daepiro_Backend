package com.numberone.daepiro.global.exception

import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_JSON_FORMAT
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_VALIDATION
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_VALUE
import com.numberone.daepiro.global.exception.CustomErrorContext.UNCAUGHT_ERROR
import mu.KotlinLogging
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(
        exception: CustomException
    ): ApiResult<Unit> {
        return handle(exception.context, exception, extractEndpoint())
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleJsonException(
        exception: HttpMessageNotReadableException
    ): ApiResult<Unit> {
        return handle(INVALID_JSON_FORMAT, exception, extractEndpoint())
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        exception: MethodArgumentNotValidException
    ): ApiResult<Unit> {
        return handle(
            INVALID_VALIDATION,
            exception,
            extractEndpoint(),
            " ${exception.message}"
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleArgumentException(
        exception: IllegalArgumentException
    ): ApiResult<Unit> {
        return handle(INVALID_VALUE, exception, extractEndpoint())
    }

    @ExceptionHandler(Exception::class)
    fun handleUnCaughtException(
        exception: Exception
    ): ApiResult<Unit> {
        return handle(UNCAUGHT_ERROR, exception, extractEndpoint())
    }

    private fun handle(
        context: CustomErrorContext,
        exception: Throwable,
        endpoint: String,
        additionalMsg: String = ""
    ): ApiResult<Unit> {
        when (context.logLevel) {
            LogLevel.DEBUG -> logger.debug(exception) { "Error occurs at $endpoint" }
            LogLevel.ERROR -> logger.error(exception) { "Error occurs at $endpoint" }
        }
        return ApiResult.error(context, endpoint, additionalMsg)
    }

    private fun extractEndpoint(): String {
        // 현재 쓰레드의 요청 정보(HTTP 요청에 대한 메서드, 경로, 헤더 등) 를 가져온 뒤 ServletRequestAttributes 으로 캐스팅한다.
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val httpRequest = requestAttributes?.request
        val path = httpRequest?.requestURI ?: "UNKNOWN_PATH"
        val method = httpRequest?.method ?: "UNKNOWN_METHOD"
        return "$method $path"
    }
}
