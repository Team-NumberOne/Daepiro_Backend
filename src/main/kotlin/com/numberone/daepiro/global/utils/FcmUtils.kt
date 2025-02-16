package com.numberone.daepiro.global.utils

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import org.bouncycastle.asn1.x500.style.RFC4519Style
import org.slf4j.LoggerFactory


object FcmUtils {
    private val logger = LoggerFactory.getLogger(FcmUtils::class.java)

    fun sendFcm(tokens: List<String>, title: String, body: String) {
        if (tokens.isEmpty()) return

        val message = MulticastMessage.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build()
            )
            .addAllTokens(tokens)
            .build()

        try {
            val response = FirebaseMessaging.getInstance().sendEachForMulticast(message)
            logger.info("FCM messages sent successfully: ${response.successCount} successful, ${response.failureCount} failed")

            response.responses.forEachIndexed { index, sendResponse ->
                if (!sendResponse.isSuccessful) {
                    val error = sendResponse.exception
                    if (error is FirebaseMessagingException) {
                        val unregisteredToken = tokens[index]
                        logger.warn("Token $unregisteredToken is unregistered and will be removed")
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("Error sending FCM messages", e)
        }
    }
}
