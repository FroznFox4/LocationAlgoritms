package models

import java.util.*

class GPSSingleDataKt (
    speed: Double = 0.0,
    longitude: Double = 0.0,
    latitude: Double = 0.0,
    val timestamp: Long = 0,
    accuracy: Float = 0f
): GPSPoint(speed, longitude, latitude, accuracy)