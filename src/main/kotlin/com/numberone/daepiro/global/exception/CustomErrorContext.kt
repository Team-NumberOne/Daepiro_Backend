package com.numberone.daepiro.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*

enum class CustomErrorContext(
    val httpStatus: HttpStatus,
    val code: Int,
    val message: String,
    val logLevel: LogLevel = LogLevel.DEBUG
) {
    //20xx: 인증 관련 오류
    UNSUPPORTED_PLATFORM(BAD_REQUEST, 2000, "지원되지 않는 소셜 로그인 플랫폼입니다."),
    INVALID_TOKEN(BAD_REQUEST, 2001, "유효하지 않은 토큰입니다."),

    //21xx: 사용자 관련 오류
    NOT_FOUND_USER(NOT_FOUND, 2100, "사용자를 찾을 수 없습니다."),

    //99xx: 공통 오류
    INVALID_JSON_FORMAT(BAD_REQUEST, 9001, "JSON 형식이 잘못되었습니다."),
    INVALID_VALUE(INTERNAL_SERVER_ERROR, 9002, "유효하지 않은 값이 발견되었습니다."),
    UNCAUGHT_ERROR(INTERNAL_SERVER_ERROR, 9999, "예기치 않은 오류가 발생하였습니다.", LogLevel.ERROR)
}

enum class LogLevel {
    ERROR, DEBUG
}
