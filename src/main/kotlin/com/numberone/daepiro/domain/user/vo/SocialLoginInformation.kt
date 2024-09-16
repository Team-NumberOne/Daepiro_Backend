package com.numberone.daepiro.domain.user.vo

import com.numberone.daepiro.domain.user.enums.SocialPlatform
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class SocialLoginInformation(
    @Enumerated(EnumType.STRING)
    val platform: SocialPlatform,
    val socialId: String
)
