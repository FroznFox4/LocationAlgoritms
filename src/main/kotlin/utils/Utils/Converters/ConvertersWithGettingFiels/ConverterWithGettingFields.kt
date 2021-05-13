package utils.Utils.Converters.ConvertersWithGettingFiels

import utils.Utils.Converters.Converter

interface ConverterWithGettingFields<T, L, U>: Converter<T> {
    fun getUniqueLongitudes(): L
    fun setUniqueLongitudes(value: L): L
    fun getUserMatrix(): U
}