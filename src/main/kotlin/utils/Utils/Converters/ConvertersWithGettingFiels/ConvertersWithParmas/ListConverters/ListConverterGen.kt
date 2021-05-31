package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters

import models.LocationEntity
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ConverterWithGettingMethodsSpecial

interface ListConverterGen<T, R, D>: ConverterWithGettingMethodsSpecial<D, R> {
    fun getMatrix(): T
    fun getRectangleMatrix(): T
    fun convertToRectangleFromMatrix(dots: R): R
    fun convertToRectangleFromLocalMatrix(): R
    fun convertToRectangleFromDots(dots: D): R
}