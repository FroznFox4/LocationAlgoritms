package Services.PeoplesAroundService.Utils

import models.LocationEntity

class MapConverter(val uniqueLongitudes: MutableSet<Double> = mutableSetOf()) {

    val matrixMap = mutableMapOf<Double, MutableMap<Double, ArrayList<LocationEntity>>>()
    val userMatrix = mutableMapOf<String, ArrayList<LocationEntity>>()

    fun convertListOfDotsToMatrixDotsMap(dots: List<LocationEntity>): MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>> {
        val sortedDots = dots.sortedByDescending { it.latitude }
        uniqueLongitudes.clear()
        val result: MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>> = mutableMapOf()
        convertMap(sortedDots, result)
        matrixMap.clear()
        matrixMap.putAll(result.toMap())
        return result
    }

    private fun convertMap(
        sortedDots: List<LocationEntity>,
        result: MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>>
    ) {
        val firstObject = sortedDots.first()
        val dotsInOnLatitude: ArrayList<LocationEntity> = arrayListOf(firstObject)
        sortedDots.forEach { el ->
            inBraceConverterMap(dotsInOnLatitude, el, result)
        }
    }

    private fun inBraceConverterMap(
        dotsInOnLatitude: java.util.ArrayList<LocationEntity>,
        el: LocationEntity,
        result: MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>>
    ) {
        val lastEl = dotsInOnLatitude.last()

        //Add unique longitudes
        if (!uniqueLongitudes.contains(el.longitude)) uniqueLongitudes.add(el.longitude)

        //Create user matrix
        userMatrix.getOrDefault(el.userName, arrayListOf(el))
        val predicate = userMatrix[el.userName]!!.find { it == el } == null
        if (predicate) userMatrix[el.userName]!!.add(el)

        //Create table
        if (lastEl.latitude == el.latitude) dotsInOnLatitude.add(el)
        else {
            val sortedDotsByLatitude = dotsInOnLatitude.sortedByDescending { it.latitude }
            sortedDotsByLatitude.forEach {
                val row = result.getOrDefault(it.longitude, mutableMapOf(it.latitude to listOf(el)))
                val cell = ArrayList(row.getOrDefault(it.latitude, listOf(el)))
                if (cell[0] != el) {
                    cell.add(el)
                    result[it.latitude]!![it.longitude] = cell
                }
            }
            dotsInOnLatitude.clear()
            dotsInOnLatitude.add(el)
        }
    }
}