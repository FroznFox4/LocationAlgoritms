package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.CustomListConverters

import models.LocationEntity
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import utils.Utils.Converters.Converter

internal class ListConverterImplTest {

    private val classForTest: ListConverter = ListConverterImpl()

    @Test
    fun convert() {
        val dots = arrayListOf<LocationEntity>()
        val result = classForTest.convert(dots)
        assertTrue(result.isEmpty())
    }
}