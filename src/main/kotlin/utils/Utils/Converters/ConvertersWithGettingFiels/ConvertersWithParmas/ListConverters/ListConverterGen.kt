package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters

import models.LocationEntity
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ConverterWithGettingFieldsWithSomeParams

interface ListConverterGen<T, R, D>: ConverterWithGettingFieldsWithSomeParams<List<List<LocationEntity>>> {
    fun getMatrix(): T
    fun getRectangleMatrix(): T
    fun convertToRectangleFromMatrix(dots: R): R
    fun convertToRectangleFromLocalMatrix(): R
    fun convertToRectangleFromDots(dots: D): R
}