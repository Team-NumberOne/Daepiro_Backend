package com.numberone.daepiro.domain.shelter.utils

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

object DistanceUtils {
    fun calculateDistance(lon1: Double, lat1: Double, lon2: Double, lat2: Double): Long {
        return (
            6371 * acos(
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    cos(Math.toRadians(lon2) - Math.toRadians(lon1)) +
                    sin(Math.toRadians(lat1)) * sin(Math.toRadians(lat2))
            )
            ).toLong()
    }
}
