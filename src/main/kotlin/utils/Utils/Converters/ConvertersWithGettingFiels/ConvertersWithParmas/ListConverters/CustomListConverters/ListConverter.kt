package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.CustomListConverters

import models.LocationEntity
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.ListConverterGen

interface ListConverter:
    ListConverterGen<MutableList<List<LocationEntity>>, List<List<LocationEntity>>, List<LocationEntity>> {
}