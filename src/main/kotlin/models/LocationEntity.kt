package models

import lombok.Data
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

@Data
data class LocationEntity(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var accuracy: Float = 0.0f,
    var date: Date = Date()
) {
    fun distanceTo(secondEl: LocationEntity): Float =
        sqrt((this.latitude - secondEl.latitude).pow(2) + (this.longitude - secondEl.longitude).pow(2)).toFloat()
}