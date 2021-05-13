package Services.KalmanFilterService

import Services.Services
import models.LocationEntity

interface KalmanFilterKt: Services {
    //Add first point
    fun setState(data: LocationEntity)
    //Add new point for path
    fun process(locationEntity: LocationEntity)
    //Get all dots in path
    fun getCollection(): ArrayList<LocationEntity>
}