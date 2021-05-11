package Services.PeoplesAroundService.Utils

import models.LocationEntity

class ListConverter(val uniqueLongitudes: MutableSet<Double> = mutableSetOf<Double>()) {
    val matrixWithoutZeros = mutableListOf<List<LocationEntity>>()
    val matrixWithZeros = mutableListOf<List<LocationEntity>>()
    val userMatrix = mutableMapOf<String, ArrayList<LocationEntity>>()

    //In this may be error
    fun addZerosInToMatrix(dots: List<List<LocationEntity>>): List<List<LocationEntity>> {
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
    fun convertListOfDotsToMatrixDots(dots: List<LocationEntity>): List<List<LocationEntity>> {
        val sortedDots = dots.sortedByDescending { it.latitude }
        uniqueLongitudes.clear()
        val result: ArrayList<ArrayList<LocationEntity>> = ArrayList()
        convert(sortedDots, result)
        matrixWithoutZeros.clear()
        matrixWithoutZeros.addAll(result)
        return result
    }

    private fun convert(
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