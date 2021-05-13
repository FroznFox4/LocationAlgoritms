package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters

import models.LocationEntity
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ConverterWithGettingFieldsWithSomeParams

interface ListConverter<T, R>: ConverterWithGettingFieldsWithSomeParams<List<List<LocationEntity>>> {
    fun getMatrix(): T
    fun getRectangleMatrix(): T
    fun convertToRectangle(dots: R): R
    fun convertToRectangle(): R
}