package com.numberone.daepiro.domain.user.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.user.enums.Role
import com.numberone.daepiro.domain.user.enums.SocialPlatform
import com.numberone.daepiro.domain.user.vo.PasswordLoginInformation
import com.numberone.daepiro.domain.user.vo.SocialLoginInformation
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "`users`")
class UserEntity(
    role: Role,
    socialLoginInformation: SocialLoginInformation? = null,
    passwordLoginInformation: PasswordLoginInformation? = null,
    realname: String? = null,
    nickname: String? = null
) : PrimaryKeyEntity() {
    var role: Role = role
        protected set

    @Embedded
    var socialLoginInformation = socialLoginInformation
        protected set

    @Embedded
    var passwordLoginInformation = passwordLoginInformation
        protected set

    var realname = realname
        protected set

    var nickname = nickname
        protected set

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

        fun adminOf(
            username: String,
            password: String
        ): UserEntity {
            return UserEntity(
                role = Role.ADMIN,
                passwordLoginInformation = PasswordLoginInformation(
                    username = username,
                    password = password
                )
            )
        }
    }
}
