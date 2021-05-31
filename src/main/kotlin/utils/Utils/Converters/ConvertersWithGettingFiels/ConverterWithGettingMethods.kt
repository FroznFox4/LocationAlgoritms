package utils.Utils.Converters.ConvertersWithGettingFiels

import utils.Utils.Converters.Converter

interface ConverterWithGettingMethods<T, L, U, I>: Converter<I, T> {
    fun getUniqueLongitudes(): L
    fun setUniqueLongitudes(value: L): L
    fun getUserMatrix(): U
}