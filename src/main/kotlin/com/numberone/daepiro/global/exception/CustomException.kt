package com.numberone.daepiro.global.exception

class CustomException(
    val context: CustomErrorContext
) : RuntimeException()