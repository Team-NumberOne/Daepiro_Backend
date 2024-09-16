package com.numberone.daepiro.domain.user.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class PasswordLoginInformation(
    val username: String,
    val password: String,
)
