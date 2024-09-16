package com.numberone.daepiro.global.exception

import org.springframework.http.HttpStatus

enum class CustomErrorContext(
    val code: Int,
    val message: String,
    val logLevel: LogLevel = LogLevel.DEBUG
) {
    //10xx: 인증 관련 오류
    UNSUPPORTED_PLATFORM(1000, "지원되지 않는 소셜 로그인 플랫폼입니다."),
    INVALID_TOKEN(1001, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(1002, "만료된 토큰입니다."),
    NOT_AUTHENTICATION(1003, "로그인이 필요합니다."),
    NOT_AUTHORIZATION(1004, "권한이 필요합니다."),

    //11xx: 사용자 관련 오류
    NOT_FOUND_USER(1100, "사용자를 찾을 수 없습니다."),


    UNCAUGHT_ERROR(500, "예기치 않은 오류가 발생하였습니다.", LogLevel.ERROR)
}

enum class LogLevel {
    ERROR, DEBUG
}
