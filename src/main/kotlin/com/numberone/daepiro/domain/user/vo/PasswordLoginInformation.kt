package com.numberone.daepiro.domain.user.vo

import jakarta.persistence.Embeddable

@Embeddable
data class PasswordLoginInformation(
    val username: String,
    val password: String,
)
