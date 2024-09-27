package com.numberone.daepiro.domain.user.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.user.enums.Role
import com.numberone.daepiro.domain.user.enums.SocialPlatform
import com.numberone.daepiro.domain.user.vo.PasswordLoginInformation
import com.numberone.daepiro.domain.user.vo.SocialLoginInformation
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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

    var nickname: String? = null
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
}
