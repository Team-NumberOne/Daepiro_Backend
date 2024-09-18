package com.numberone.daepiro.domain.user.repository

import com.numberone.daepiro.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    //todo 가독성 문제때문에 스프링데이터jpa 네이밍이 아니라 다른 방식으로 전환할지 고민
    fun findByPasswordLoginInformation_Username(username: String): UserEntity?
    fun findBySocialLoginInformation_SocialId(socialId: String): UserEntity?
}
