package com.numberone.daepiro.domain.notification.service

import com.numberone.daepiro.domain.notification.entity.Notification
import com.numberone.daepiro.domain.notification.entity.NotificationCategory
import com.numberone.daepiro.domain.notification.repository.NotificationRepository
import com.numberone.daepiro.domain.user.entity.UserEntity
import com.numberone.daepiro.global.utils.FcmUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class NotificationService(
    private val notificationRepository: NotificationRepository
) {
    @Transactional
    fun sendNotification(users: List<UserEntity>, category: NotificationCategory, title: String, body: String) {
        val targets = users.filter { it.deletedAt == null && it.fcmToken != null}
        FcmUtils.sendFcm(targets.mapNotNull { it.fcmToken }, title, body)

        notificationRepository.saveAll(
            targets.map { user ->
                Notification.of(
                    category = category,
                    title = title,
                    body = body,
                    userId = user.id!!
                )
            }
        )
    }
}
