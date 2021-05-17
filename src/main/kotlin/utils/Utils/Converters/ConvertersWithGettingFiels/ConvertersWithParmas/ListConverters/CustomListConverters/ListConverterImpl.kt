package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.CustomListConverters

import models.LocationEntity

class ListConverterImpl:
    utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.CustomListConverters.ListConverterFill {

    private var uniqueLongitudes: MutableSet<Double> = mutableSetOf()
    private val userMatrix = mutableMapOf<String, ArrayList<LocationEntity>>()
    private val matrixWithoutZeros = mutableListOf<List<LocationEntity>>()
    private val matrixWithZeros = mutableListOf<List<LocationEntity>>()

    override fun getMatrix(): MutableList<List<LocationEntity>> {
        return matrixWithoutZeros
    }

    override fun getRectangleMatrix(): MutableList<List<LocationEntity>> {
        return matrixWithZeros
    }

    override fun getUniqueLongitudes(): MutableSet<Double> {
        return uniqueLongitudes
    }

    override fun setUniqueLongitudes(value: MutableSet<Double>): MutableSet<Double> {
        this.uniqueLongitudes = value
        return value
    }

    override fun getUserMatrix(): MutableMap<String, ArrayList<LocationEntity>> {
        return userMatrix
    }

    //In this may be error
    override fun convertToRectangle(): List<List<LocationEntity>> {
        convertToRectangle(matrixWithoutZeros)
        return matrixWithoutZeros
    }

    override fun convertToRectangle(dots: List<List<LocationEntity>>): List<List<LocationEntity>> {
        if (matrixWithoutZeros.isEmpty()) convert(dots.flatten())
        val result = dots.map { arrayOfLocations ->
            if (arrayOfLocations.size != uniqueLongitudes.size) {
                uniqueLongitudes.forEach { longitude ->
                    val longitudeInArray = arrayOfLocations.find { el -> el.longitude == longitude }
                    if (longitudeInArray == null)
                        ArrayList(arrayOfLocations).add(
                            LocationEntity(
                                "",
                                arrayOfLocations[0].latitude,
                                longitude
                            )
                        )
                }
            }
            arrayOfLocations
        }
        matrixWithZeros.clear()
        matrixWithZeros.addAll(result)
        return result
    }

    //    o(n^2 + 2n)
    override fun convert(dots: List<LocationEntity>): List<List<LocationEntity>> {
        val sortedDots = dots.sortedByDescending { it.latitude }
        uniqueLongitudes.clear()
        val result: ArrayList<ArrayList<LocationEntity>> = ArrayList()
        convertInConvert(sortedDots, result)
        matrixWithoutZeros.clear()
        matrixWithoutZeros.addAll(result)
        return result
    }

    private fun convertInConvert(
        sortedDots: List<LocationEntity>,
        result: ArrayList<ArrayList<LocationEntity>>
    ) {
        val firstObject = sortedDots.first()
        val dotsInOnLatitude: ArrayList<LocationEntity> = arrayListOf(firstObject)
        sortedDots.forEach { el ->
            inBraceConverter(dotsInOnLatitude, el, result)
        }
    }

    private fun inBraceConverter(
        dotsInOnLatitude: ArrayList<LocationEntity>,
        el: LocationEntity,
        result: ArrayList<ArrayList<LocationEntity>>
    ) {
        val lastEl = dotsInOnLatitude.last()

        //Add unique longitudes
        if (!uniqueLongitudes.contains(el.longitude)) uniqueLongitudes.add(el.longitude)

        userMatrix.getOrDefault(el.userName, arrayListOf(el))
        val predicate = userMatrix[el.userName]!!.find { it == el } == null
        if (predicate) userMatrix[el.userName]!!.add(el)

        if (lastEl.latitude == el.latitude) dotsInOnLatitude.add(el)
        else {
            result.add(
                ArrayList(
                    dotsInOnLatitude.sortedByDescending { it.longitude })
            )
            dotsInOnLatitude.clear()
            dotsInOnLatitude.add(el)
        }
    }

}