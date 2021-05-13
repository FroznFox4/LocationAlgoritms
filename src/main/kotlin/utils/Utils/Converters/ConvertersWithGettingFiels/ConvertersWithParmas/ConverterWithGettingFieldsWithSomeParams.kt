package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas

import models.LocationEntity
import utils.Utils.Converters.ConvertersWithGettingFiels.ConverterWithGettingFields

interface ConverterWithGettingFieldsWithSomeParams<T>:
    ConverterWithGettingFields<T, MutableSet<Double>, MutableMap<String, ArrayList<LocationEntity>>> {
}