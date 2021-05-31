package utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.MapConverter

import models.LocationEntity

class MapConverterImpl: MapConverter{

    private var uniqueLongitudes = mutableSetOf<Double>()
    private val matrixMap = mutableMapOf<Double, MutableMap<Double, ArrayList<LocationEntity>>>()
    private val userMatrix = mutableMapOf<String, ArrayList<LocationEntity>>()

    override fun getUniqueLongitudes(): MutableSet<Double> {
        return  uniqueLongitudes
    }

    override fun setUniqueLongitudes(value: MutableSet<Double>): MutableSet<Double> {
        this.uniqueLongitudes = value
        return this.uniqueLongitudes
    }

    override fun getMatrixMap(): MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>> {
        return matrixMap
    }

    override fun getUserMatrix(): MutableMap<String, ArrayList<LocationEntity>> {
        return  userMatrix
    }

    override fun convert(dots: ArrayList<LocationEntity>): MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>> {
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