package com.numberone.daepiro.global.exception

import org.springframework.http.HttpStatus

enum class CustomErrorContext(
    val code: Int,
    val message: String,
) {
    USER_NAME_DUPLICATED(HttpStatus.BAD_REQUEST.value(), "이미 중복된 이름의 회원이 존재합니다."),

    UNCAUGHT_ERROR(999, "예기치 않은 오류가 발생하였습니다.")
}