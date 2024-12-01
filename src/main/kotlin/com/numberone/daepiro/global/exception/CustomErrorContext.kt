package com.numberone.daepiro.global.exception

import org.springframework.http.HttpStatus

enum class CustomErrorContext(
    val httpStatus: HttpStatus,
    val code: Int,
    val message: String,
    val logLevel: LogLevel = LogLevel.DEBUG
) {
    // 20xx: 인증 관련 오류
    UNSUPPORTED_PLATFORM(HttpStatus.BAD_REQUEST, 2000, "지원되지 않는 소셜 로그인 플랫폼입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, 2001, "유효하지 않은 토큰입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, 2002, "비밀번호가 잘못되었습니다."),
    INVALID_SOCIAL_TOKEN(HttpStatus.BAD_REQUEST, 2003, "유효하지 않은 소셜 토큰입니다."),
    NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, 2004, "인증이 필요합니다."),
    NOT_AUTHORIZED(HttpStatus.FORBIDDEN, 2005, "관리자 권한이 필요합니다."),

    // 21xx: 사용자 관련 오류
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 2100, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_USER_LOCATION(HttpStatus.BAD_REQUEST, 2101, "사용자의 위치 정보를 찾을 수 없습니다."),

    // 22xx: 재난 관련 오류
    NOT_FOUND_DISASTER_TYPE(HttpStatus.NOT_FOUND, 2200, "재난 유형을 찾을 수 없습니다."),
    NOT_FOUND_OCCURRED_DISASTER(HttpStatus.NOT_FOUND, 2201, "발생한 재난을 찾을 수 없습니다."),

    // 23xx: 주소 관련 오류
    INVALID_ADDRESS_FORMAT(HttpStatus.BAD_REQUEST, 2300, "주소 형식이 잘못되었습니다."),
    INVALID_COORDINATES_CONVERTER(HttpStatus.INTERNAL_SERVER_ERROR, 2301, "위도, 경도를 주소로 변환하는데 실패하였습니다."),

    // 24xx: 재난콘텐츠 관련 오류
    INVALID_DISASTER_CONTENT_SORT_TYPE(HttpStatus.BAD_REQUEST, 2400, "재난 콘텐츠 정렬 형식이 잘못되었습니다."),

    // 99xx: 공통 오류
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, 9001, "JSON 형식이 잘못되었습니다."),
    INVALID_VALUE(HttpStatus.INTERNAL_SERVER_ERROR, 9002, "유효하지 않은 값이 발견되었습니다."),
    INVALID_VALIDATION(HttpStatus.BAD_REQUEST, 9003, "값의 유효성 검사에 실패하였습니다."),
    UNCAUGHT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 9999, "예기치 않은 오류가 발생하였습니다.", LogLevel.ERROR)
}

enum class LogLevel {
    ERROR, DEBUG
}
