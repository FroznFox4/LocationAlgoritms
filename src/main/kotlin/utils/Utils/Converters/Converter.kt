package utils.Utils.Converters

import models.LocationEntity

interface Converter<T> {
    fun convert(dots: List<LocationEntity>): T
}