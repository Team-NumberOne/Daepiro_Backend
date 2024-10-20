package com.numberone.daepiro.domain.auth.vo

import com.numberone.daepiro.domain.auth.enums.TokenType
import com.numberone.daepiro.domain.user.enums.Role

data class TokenInfo(
    val id: Long,
    val role: Role,
    val type: TokenType
)
