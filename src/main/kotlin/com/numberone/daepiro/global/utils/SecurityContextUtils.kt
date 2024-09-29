package com.numberone.daepiro.global.utils

import org.springframework.security.core.context.SecurityContextHolder

object SecurityContextUtils {
    fun getUserId(): Long {
        val auth = SecurityContextHolder.getContext().authentication
        return auth.principal as Long
    }
}
