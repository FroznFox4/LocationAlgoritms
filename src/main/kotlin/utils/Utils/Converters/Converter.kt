package utils.Utils.Converters

import models.LocationEntity

interface Converter<I, T> {
    fun convert(dots: I): T
}