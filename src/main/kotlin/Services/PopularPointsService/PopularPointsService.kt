package Services.PopularPointsService

import Services.PopularPointsService.Models.People
import models.LocationEntity

interface PopularPointsService: PopularPointsServiceGen<List<LocationEntity>, People> {
    fun getRadius(): Int
    fun setRadius(value: Int): Int
}