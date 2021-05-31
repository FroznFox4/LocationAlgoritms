package utils.KalmanFilterKt

import models.LocationEntity

interface KalmanFilter {
    fun setState(latitude: Double, longitude: Double, timeStamp: Long, accuracy: Double)
    fun process(newSpeed: Double, newLatitude: Double, newLongitude: Double, newTimeStamp: Long, newAccuracy: Float)
}