package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.MapConverter

import models.LocationEntity
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ConverterWithGettingFieldsWithSomeParams

interface MapConverter :
    ConverterWithGettingFieldsWithSomeParams<MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>>> {
    fun getMatrixMap(): MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>>
}