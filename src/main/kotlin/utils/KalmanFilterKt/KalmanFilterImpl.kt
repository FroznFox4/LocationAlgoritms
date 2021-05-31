package utils.KalmanFilterKt

import models.GPSSingleDataKt
import models.LocationEntity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

class KalmanFilterImpl : KalmanFilterExtender<LocationEntity> {

    private var timeStamp: Long = 0
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var variance: Double = -1.0
    private val collection: ArrayList<LocationEntity> = arrayListOf()

    override fun getCollection(): ArrayList<LocationEntity> {
        return this.collection
    }


    // Init method (use this after constructor, and before process)
    // if you are using last known data from gps)
    override fun setState(latitude: Double, longitude: Double, timeStamp: Long, accuracy: Double) {
        this.latitude = latitude
        this.longitude = longitude
        this.timeStamp = timeStamp
        variance = accuracy * accuracy
    }

    override fun setState(data: LocationEntity) {
        this.latitude = data.latitude
        this.longitude = data.longitude
        this.timeStamp = data.date.time
        variance = data.accuracy.toDouble().pow(2.0)
    }

    /**
     * Kalman filter processing for latitude and longitude
     *
     * newLatitude - new measurement of latitude
     * newLongitude - new measurement of longitude
     * accuracy - measurement of 1 standard deviation error in meters
     * newTimeStamp - time of measurement in millis
     */
    override fun process(
        newSpeed: Double,
        newLatitude: Double,
        newLongitude: Double,
        newTimeStamp: Long,
        newAccuracy: Float
    ) {
        // Uncomment this, if you are receiving accuracy from your gps
        var localAccuracy = newAccuracy
        if (localAccuracy < ConstantsKt().MIN_ACCURACY) {
            localAccuracy = ConstantsKt().MIN_ACCURACY
        }
        val localSpeed = newSpeed
        if (variance < 0) {
            // if variance < 0, object is unitialised, so initialise with current values
            setState(newLatitude, newLongitude, newTimeStamp, localAccuracy.toDouble())
        } else {
            // else apply Kalman filter
            val duration = newTimeStamp - timeStamp
            if (duration > 0) {
                // time has moved on, so the uncertainty in the current position increases
                variance += duration * localSpeed * localSpeed / 1000
                timeStamp = newTimeStamp
            }
            // Kalman gain matrix 'k' = Covariance * Inverse(Covariance + MeasurementVariance)
            // because 'k' is dimensionless,
            // it doesn't matter that variance has different units to latitude and longitude
            val k = variance / (variance + localAccuracy * localAccuracy - 2)
            latitude += k * (newLatitude - latitude)
            longitude += k * (newLongitude - longitude)
            // new Covariance matrix is (IdentityMatrix - k) * Covariance
            variance *= (1 - k)
            // Export new point
            exportNewPoint(localSpeed, longitude, latitude, duration)
        }
    }

    override fun process(data: LocationEntity) {
        with(data) {
            process(
                speed,
                latitude,
                longitude,
                date.time,
                accuracy
            )
        }
    }

    private fun exportNewPoint(speed: Double, longitude: Double, latitude: Double, timestamp: Long) {
        val newGPData = GPSSingleDataKt(speed, longitude, latitude, timestamp)
        this.collection.add(newGPData.toLocation())
    }

    private fun exportNewPoint(locationEntity: LocationEntity) {
        var newGpsData: GPSSingleDataKt
        with(locationEntity) {
            newGpsData = GPSSingleDataKt(speed, longitude, latitude, date.time)
        }
        this.collection.add(newGpsData.toLocation())
    }

    private fun gpsSingleDataToLocationEntity(gpsSingleDataKt: GPSSingleDataKt): LocationEntity {
        var locationEntity: LocationEntity
        with(gpsSingleDataKt) {
            locationEntity = LocationEntity(
                "",
                latitude,
                longitude,
                accuracy,
                speed,
                Date(timestamp)
            )
        }
        return locationEntity
    }

    private fun GPSSingleDataKt.toLocation(): LocationEntity {
        return gpsSingleDataToLocationEntity(this)
    }
}