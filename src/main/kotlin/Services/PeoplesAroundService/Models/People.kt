package Services.PeoplesAroundService.Models

import models.LocationEntity

data class People(
    val userName: String = "",
    val dots: List<LocationEntity> = emptyList()
)