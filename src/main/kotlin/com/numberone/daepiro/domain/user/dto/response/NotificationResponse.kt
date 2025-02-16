package com.numberone.daepiro.domain.user.dto.response

import com.numberone.daepiro.domain.notification.entity.Notification
import java.time.LocalDateTime

data class NotificationResponse(
    val category: String,
    val title: String,
    val body: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(notification: Notification): NotificationResponse {
            return NotificationResponse(
                category = notification.category.description,
                title = notification.title,
                body = notification.body,
                createdAt = notification.createdAt
            )
        }
    }
}
