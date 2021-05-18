package Services.PeoplesAroundService

import Services.PeoplesAroundService.Models.IntersectionsPeoples
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.CustomListConverters.ListConverterImpl
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.MapConverter.MapConverterImpl
import models.IntPoint
import models.LocationEntity
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.CustomListConverters.ListConverter
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.MapConverter.MapConverter

class PeoplesAroundServiceImpl : PeoplesAroundService {

    private val simpleEntity = arrayListOf<LocationEntity>()
    private val listConverter: ListConverter = ListConverterImpl()
    private val mapConverter: MapConverter = MapConverterImpl()

    private val uniqueLongitudes: MutableSet<Double>
        get() {
            var result = mutableSetOf<Double>()
            if (mapConverter.getUniqueLongitudes().isNotEmpty())
                result = mapConverter.getUniqueLongitudes()
            else if (listConverter.getUniqueLongitudes().isNotEmpty())
                result = listConverter.getUniqueLongitudes()
            return result
        }

    private val userMatrix: MutableMap<String, ArrayList<LocationEntity>>
        get() {
            var result = mutableMapOf<String, ArrayList<LocationEntity>>()
            if (mapConverter.getUserMatrix().isNotEmpty())
                result = mapConverter.getUserMatrix()
            else if (listConverter.getUserMatrix().isNotEmpty())
                result = listConverter.getUserMatrix()
            return result
        }

    private val matrixWithoutZeros = listConverter.getMatrix()
    private val matrixWithZeros = listConverter.getRectangleMatrix()
    private val matrixMap = mapConverter.getMatrixMap()

    constructor()

    /**
     *  @param strategy {
     *      1 - create list with dots without zeros
     *      2 - add zeros to list of dots (user only with 1)
     *      3 - convert dots in map
     *  }
     */
    constructor(strategy: List<Int>, simpleEntity: List<LocationEntity>) {
        this.simpleEntity.clear()
        this.simpleEntity.addAll(simpleEntity)
        strategy(strategy)
    }

    override fun getPeoplesAround(dots: List<LocationEntity>): List<IntersectionsPeoples> {
        return getPeoplesInRadius(dots, 0.0)
    }

    override fun getPeoplesInRadius(dots: List<LocationEntity>, radius: Double): List<IntersectionsPeoples> {
        return getPeoplesInRadiusForUsers(dots, radius, userMatrix.keys.toList())
    }

    override fun getPeoplesInRadiusForUsers(
        dots: List<LocationEntity>,
        radius: Double,
        user: List<String>
    ): List<IntersectionsPeoples> {
        return user.map { getPeoplesInRadiusForUser(dots, radius, it) }
    }

    override fun getPeoplesInRadiusForUser(
        dots: List<LocationEntity>,
        radius: Double,
        user: String
    ): IntersectionsPeoples {
        val result = IntersectionsPeoples(user)
        val usersMap = mutableMapOf<String, Boolean>()
        if (userMatrix.isEmpty())
            mapConverter.convert(dots)
        val localUserMatrix = userMatrix
        localUserMatrix[user]?.forEach {
            val dotInCord = matrixMap[it.latitude]!![it.longitude]!!
            dotInCord.forEach { simpleDotInCord ->
                usersMap.getOrPut(simpleDotInCord.userName) { true }
                if (radius > 0.0) {
                    val colKeys = ArrayList(matrixMap.keys)
                    val rowKeys = ArrayList(uniqueLongitudes)
                    val curIndexColEl = colKeys.indexOf(it.latitude)
                    val curIndexRowEl = rowKeys.indexOf(it.longitude)
                    val colPeriodX = IntPoint(0, 0)
                    val rowPeriodY = IntPoint(0, 0)
                    var index = 0
                    while (true) {
                        val firstPredicate = (curIndexColEl - index) > -1
                                && colKeys[curIndexColEl] - colKeys[curIndexColEl - index] < radius
                        if (firstPredicate) {
                            colPeriodX.X = curIndexColEl - index
                        }
                        val secondPredicate = (curIndexColEl + index) < colKeys.size
                                && colKeys[curIndexColEl + index] - colKeys[curIndexColEl] < radius
                        if (secondPredicate) {
                            colPeriodX.Y = curIndexColEl + index
                        }
                        if (!firstPredicate && !secondPredicate) {
                            index = 0
                            break
                        }
                        index += 1
                    }
                    while (true) {
                        val firstPredicate = (curIndexRowEl - index) > -1
                                && rowKeys[curIndexRowEl] - rowKeys[curIndexRowEl - index] < radius
                        if (firstPredicate) {
                            rowPeriodY.X = curIndexRowEl - index
                        }
                        val secondPredicate = (curIndexRowEl + index) < colKeys.size
                                && colKeys[curIndexRowEl + index] - colKeys[curIndexRowEl] < radius
                        if (secondPredicate) {
                            rowPeriodY.Y = curIndexRowEl + index
                        }
                        if (!firstPredicate && !secondPredicate) {
                            index = 0
                            break
                        }
                        index += 1
                    }
                    val colArray = arrayListOf<Double>()
                    val rowArray = arrayListOf<Double>()
                    for (el in colPeriodX.X until colPeriodX.Y)
                        colArray.add(colKeys[el])
                    for (el in rowPeriodY.X until rowPeriodY.Y)
                        rowArray.add(rowKeys[el])
                    colArray.forEach { col ->
                        rowArray.forEach { row ->
                            val rows = matrixMap[col]!!
                            rows[row]?.forEach { dotInCell ->
                                usersMap.getOrPut(dotInCell.userName ) { true }
                            }
                        }
                    }
                }
            }
        } ?: return IntersectionsPeoples("user not found")
        result.users.addAll(usersMap.keys)
        return result
    }


    private fun strategy(version: List<Int>) {
        val f1: () -> List<List<LocationEntity>> =
            { listConverter.convert(simpleEntity) }
        val f2: () -> List<List<LocationEntity>> =
            { listConverter.convertToRectangleFromMatrix(matrixWithZeros) }
        val f3: () -> MutableMap<Double, MutableMap<Double, ArrayList<LocationEntity>>> =
            { mapConverter.convert(simpleEntity) }
        version.forEach {
            when (it) {
                1 -> f1()
                2 -> f2()
                3 -> f3()
            }
        }
    }
}