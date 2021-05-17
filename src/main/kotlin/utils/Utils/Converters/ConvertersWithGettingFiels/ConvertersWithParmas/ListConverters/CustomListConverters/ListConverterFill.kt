package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.CustomListConverters

import models.LocationEntity
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.ListConverter

interface ListConverterFill:
    ListConverter<MutableList<List<LocationEntity>>, List<List<LocationEntity>>> {
}