package Services.PeoplesAroundService.Utils

import models.LocationEntity

class MapConverter(val uniqueLongitudes: MutableSet<Double> = mutableSetOf()) {

    val matrixMap = mutableMapOf<Double, MutableMap<Double, ArrayList<LocationEntity>>>()
    val userMatrix = mutableMapOf<String, ArrayList<LocationEntity>>()

    fun convertListOfDotsToMatrixDotsMap(dots: List<LocationEntity>): MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>> {
        val sortedDots = dots.sortedByDescending { it.latitude }
        uniqueLongitudes.clear()
        var result: MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>> = mutableMapOf()
        result = convertMap(sortedDots, result)
        matrixMap.clear()
        matrixMap.putAll(result.toMap())
        val wtf = sortedDots.filter { !result.containsKey(it.latitude) }
        return result
    }

    private fun convertMap(
        sortedDots: List<LocationEntity>,
        result: MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>>
    ): MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>> {
        val firstObject = sortedDots.first()
        var dotsInOnLatitude: ArrayList<LocationEntity> = arrayListOf(firstObject)
        sortedDots.forEach { el ->
            dotsInOnLatitude = inBraceConverterMap(dotsInOnLatitude, el, result)
        }
        dotsInOnLatitude = inBraceConverterMap(arrayListOf(sortedDots.last()), LocationEntity(), result)
        return result
    }

    private fun inBraceConverterMap(
        dotsInOnLatitude: ArrayList<LocationEntity>,
        el: LocationEntity,
        result: MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>>
    ): java.util.ArrayList<LocationEntity> {
        val lastEl = dotsInOnLatitude.last()

        //Add unique longitudes
        if (!uniqueLongitudes.contains(el.longitude)) uniqueLongitudes.add(el.longitude)

        //Create user matrix
        userMatrix.getOrPut(el.userName) { arrayListOf(el) }
        val predicate = userMatrix[el.userName]!!.find { it == el } == null
        if (predicate) userMatrix[el.userName]!!.add(el)

        //Create table
        if (lastEl.latitude == el.latitude) dotsInOnLatitude.add(el)
        else {
            val sortedDotsByLatitude = dotsInOnLatitude.sortedByDescending { it.latitude }
            sortedDotsByLatitude.forEach {
                val row = result.getOrPut(it.latitude) { mutableMapOf(it.longitude to arrayListOf(it)) }
                val cell = row.getOrPut(it.longitude) { arrayListOf(it) }
                if (cell[0] != it) {
                    cell.add(it)
                    result[it.latitude]!![it.longitude] = cell
                }
            }
            dotsInOnLatitude.clear()
            dotsInOnLatitude.add(el)
        }
        return dotsInOnLatitude
    }
}