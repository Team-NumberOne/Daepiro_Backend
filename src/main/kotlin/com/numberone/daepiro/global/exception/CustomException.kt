package com.numberone.daepiro.global.exception

class CustomException(
    val context: CustomErrorContext,
    val additionalDetail: String? = null
) : RuntimeException()
