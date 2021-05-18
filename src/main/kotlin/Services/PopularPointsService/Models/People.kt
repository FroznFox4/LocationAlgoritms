package Services.PopularPointsService.Models

import models.LocationEntity

data class People(
    var userName: String = "",
    var dots: List<LocationEntity> = emptyList()
)