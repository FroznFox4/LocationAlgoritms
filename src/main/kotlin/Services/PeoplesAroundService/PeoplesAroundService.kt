package Services.PeoplesAroundService

import Services.PeoplesAroundService.Models.IntersectionsPeoples
import Services.Services
import models.LocationEntity

interface PeoplesAroundService : Services {
    fun getPeoplesAround(dots: List<LocationEntity>): List<IntersectionsPeoples>
    fun getPeoplesInRadius(dots: List<LocationEntity>, radius: Double): List<IntersectionsPeoples>
    fun getPeoplesInRadiusForUsers(
        dots: List<LocationEntity>,
        radius: Double,
        user: List<String>
    ): List<IntersectionsPeoples>
    fun getPeoplesInRadiusForUser(
        dots: List<LocationEntity>,
        radius: Double,
        user: String
    ): IntersectionsPeoples
}