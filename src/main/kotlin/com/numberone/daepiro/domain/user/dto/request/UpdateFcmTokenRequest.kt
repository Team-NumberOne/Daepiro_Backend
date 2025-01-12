package com.numberone.daepiro.domain.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class UpdateFcmTokenRequest(
    @Schema(description = "FCM 토큰", example = "cT9YaTTeTEeGeohmih6qWf:APA91bGoybhRF2sB6JC7mPNaqJi3XjPhwbTyy91iexup7QDB_AXxI5lug_l-4o0y9T4uYKoysdf12Wde5X01fv-dqWck1kk33d4O5TN3oz_4nzuUVa2ffE3S9ELAcZm2a308W7NGD3Sc")
    val fcmToken: String
)
