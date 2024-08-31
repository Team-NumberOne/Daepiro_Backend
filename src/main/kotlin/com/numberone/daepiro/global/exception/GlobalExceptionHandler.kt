package com.numberone.daepiro.global.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(exception: CustomException): ResponseEntity<CustomErrorResponse> {
        return handle(exception.context)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnCaughtException(exception: Exception): ResponseEntity<CustomErrorResponse> {
        println(exception.message)
        return handle(CustomErrorContext.UNCAUGHT_ERROR)
    }

    private fun handle(context: CustomErrorContext): ResponseEntity<CustomErrorResponse> {
        val endpoint = getEndpoint()
        val response = CustomErrorResponse.of(context, endpoint)
        return ResponseEntity.status(response.code).body(response)
    }

    private fun getEndpoint(): String {
        // 현재 쓰레드의 요청 정보(HTTP 요청에 대한 메서드, 경로, 헤더 등) 를 가져온 뒤 ServletRequestAttributes 으로 캐스팅한다.
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val httpRequest = requestAttributes?.request
        val path = httpRequest?.requestURI ?: "UNKNOWN_PATH"
        val method = httpRequest?.method ?: "UNKNOWN_METHOD"
        return "$method $path"
    }
}