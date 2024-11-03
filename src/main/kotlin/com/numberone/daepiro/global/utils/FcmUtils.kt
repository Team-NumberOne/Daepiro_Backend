package com.numberone.daepiro.global.utils

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification

object FcmUtils {
    fun sendFcm(tokens: List<String>, title: String, body: String) {
        val messages = tokens.map { token ->
            Message.builder()
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .setToken(token)
                .build()
        }
        if (messages.isNotEmpty())
            FirebaseMessaging.getInstance().sendAll(messages)
    }
}
