package com.numberone.daepiro.domain.notification.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class Notification(
    @Enumerated(EnumType.STRING)
    var category: NotificationCategory,

    var title: String,

    var body: String,

    var userId: Long
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            category: NotificationCategory,
            title: String,
            body: String,
            userId: Long
        ): Notification {
            return Notification(
                category = category,
                title = title,
                body = body,
                userId = userId
            )
        }
    }
}

enum class NotificationCategory(
    val description: String
) {
    DISASTER("재난문자"),
    COMMUNITY("커뮤니티"),
    SPONSOR("후원"),
    ANNOUNCEMENT("대피로 공지사항")
}
