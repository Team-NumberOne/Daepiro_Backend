package com.numberone.daepiro.domain.user.entity

import com.numberone.daepiro.domain.address.entity.KoreaLocation
import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.user.enums.Role
import com.numberone.daepiro.domain.user.enums.SocialPlatform
import com.numberone.daepiro.domain.user.vo.PasswordLoginInformation
import com.numberone.daepiro.domain.user.vo.SocialLoginInformation
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "`users`")
class UserEntity(
    @Enumerated(EnumType.STRING)
    val role: Role,

    @Embedded
    val socialLoginInformation: SocialLoginInformation? = null,

    @Embedded
    val passwordLoginInformation: PasswordLoginInformation? = null,

    var realname: String? = null,

    var nickname: String? = null,

    var isCompletedOnboarding: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var location: KoreaLocation? = null,
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            platform: SocialPlatform,
            socialId: String,
        ): UserEntity {
            return UserEntity(
                role = Role.USER,
                socialLoginInformation = SocialLoginInformation(
                    platform = platform,
                    socialId = socialId
                ),
            )
        }
    }

    fun initName(
        realname: String,
        nickname: String
    ) {
        this.realname = realname
        this.nickname = nickname
    }

    fun completeOnboarding() {
        isCompletedOnboarding = true
    }

    fun updateLocation(location: KoreaLocation) {
        this.location = location
    }
}
