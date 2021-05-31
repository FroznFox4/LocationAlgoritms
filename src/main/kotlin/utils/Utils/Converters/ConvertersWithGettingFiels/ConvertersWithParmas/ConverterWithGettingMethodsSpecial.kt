package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas

import utils.Utils.Converters.ConvertersWithGettingFiels.ConverterWithGettingMethods

interface ConverterWithGettingMethodsSpecial<I, T>:
    ConverterWithGettingMethods<T, MutableSet<Double>, MutableMap<String, I>, I> {
}