package com.numberone.daepiro.domain.notification.repository

import com.numberone.daepiro.domain.notification.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository: JpaRepository<Notification, Long> {
}
