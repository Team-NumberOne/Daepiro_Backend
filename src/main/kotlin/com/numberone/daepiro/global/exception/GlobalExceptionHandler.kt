package com.numberone.daepiro.global.exception

import com.numberone.daepiro.global.DprApiResponse
import mu.KotlinLogging
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun <T> handleCustomException(exception: CustomException): DprApiResponse<T> {
        return handle(exception.context, exception, extractEndpoint())
    }

    @ExceptionHandler(Exception::class)
    fun <T> handleUnCaughtException(exception: Exception): DprApiResponse<T> {
        return handle(CustomErrorContext.UNCAUGHT_ERROR, exception, extractEndpoint())
    }

    private fun <T> handle(
        context: CustomErrorContext,
        exception: Throwable,
        endpoint: String
    ): DprApiResponse<T> {
        when (context.logLevel) {
            LogLevel.DEBUG -> logger.debug(exception) { "Error occurs at $endpoint" }
            LogLevel.ERROR -> logger.error(exception) { "Error occurs at $endpoint" }
        }
        return DprApiResponse.error(context, endpoint)
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
