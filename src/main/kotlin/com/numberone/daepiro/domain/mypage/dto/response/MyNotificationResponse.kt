package com.numberone.daepiro.domain.mypage.dto.response

import com.numberone.daepiro.domain.user.entity.UserEntity

data class MyNotificationResponse(
    val isCommunityNotificationEnabled: Boolean,
    val isDisasterNotificationEnabled: Boolean,
) {
    companion object {
        fun of(
            userEntity: UserEntity
        ): MyNotificationResponse {
            return MyNotificationResponse(
                isCommunityNotificationEnabled = userEntity.isCommunityNotificationEnabled,
                isDisasterNotificationEnabled = userEntity.isDisasterNotificationEnabled
            )
        }
    }
}
