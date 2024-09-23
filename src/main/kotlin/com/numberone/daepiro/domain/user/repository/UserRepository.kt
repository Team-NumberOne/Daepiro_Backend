package com.numberone.daepiro.domain.user.repository

import com.numberone.daepiro.domain.user.entity.UserEntity
import com.numberone.daepiro.domain.user.enums.SocialPlatform
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, Long> {
    @Query(
        "select u " +
            "from UserEntity u " +
            "where u.passwordLoginInformation.username = :username"
    )
    fun findByUsername(username: String): UserEntity?

    @Query(
        "select u " +
            "from UserEntity u " +
            "where u.socialLoginInformation.socialId = :socialId " +
            "and u.socialLoginInformation.platform = :platform"
    )
    fun findBySocialIdAndPlatform(socialId: String, platform: SocialPlatform): UserEntity?
}
