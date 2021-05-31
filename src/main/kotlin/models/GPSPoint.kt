package models

open class GPSPoint(
    var speed: Double = 0.0,
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val accuracy: Float = 0f
)