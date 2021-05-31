package models

import lombok.Data
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

open class LocationEntity(
    var userName: String = "",
    latitude: Double = 0.0,
    longitude: Double = 0.0,
    accuracy: Float = 0.0f,
    speed: Double = 0.0,
    var date: Date = Date()
): GPSPoint(speed, longitude, latitude, accuracy)
{
    fun distanceTo(secondEl: LocationEntity): Float =
        sqrt((this.latitude - secondEl.latitude).pow(2) + (this.longitude - secondEl.longitude).pow(2)).toFloat()

    fun toCords(): String {
        return listOf(latitude.toString(), longitude.toString()).joinToString("; ")
    }

    override fun toString(): String {
        return listOf(
            userName, latitude.toString(), longitude.toString(), accuracy.toString(),
            speed.toString(), date.toString()
        ).joinToString(" ")
    }
}