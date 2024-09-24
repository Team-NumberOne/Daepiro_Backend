package com.numberone.daepiro.domain.user.repository

import com.numberone.daepiro.domain.user.entity.UserEntity
import com.numberone.daepiro.domain.user.enums.SocialPlatform
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull

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

fun UserRepository.findByIdOrThrow(id: Long): UserEntity {
    return this.findByIdOrNull(id) ?: throw CustomException(CustomErrorContext.NOT_FOUND_USER)
}
