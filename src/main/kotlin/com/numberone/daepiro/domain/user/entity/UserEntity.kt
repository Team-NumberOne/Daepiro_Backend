package com.numberone.daepiro.domain.user.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "`users`")
class UserEntity(
    socialLoginInformation: SocialLoginInformation,
    realname: String,
    nickname: String
) : PrimaryKeyEntity() {
    @Embedded
    var socialLoginInformation = socialLoginInformation
        protected set

    @Column(nullable = false)
    var realname: String = realname
        protected set

    @Column(nullable = false)
    var nickname: String = nickname
        protected set
}