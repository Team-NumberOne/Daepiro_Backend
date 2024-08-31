package com.numberone.daepiro.domain.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class SocialLoginInformation(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val registerType: UserRegisterType?,

    @Column(nullable = false)
    val socialId: String?
)
