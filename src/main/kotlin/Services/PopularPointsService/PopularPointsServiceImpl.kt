package Services.PopularPointsService

import Services.PopularPointsService.Models.People
import models.IntPoint
import models.LocationEntity
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.CustomListConverters.ListConverter
import utils.Utils.Converters.ConvertersWithGettingFiels.ConvertersWithParmas.ListConverters.CustomListConverters.ListConverterImpl

class PopularPointsServiceImpl : PopularPointsService {
    private var radius: Int

    constructor() {
        this.radius = 1
    }

    constructor(radius: Int) {
        this.radius = radius
    }

    override fun getRadius(): Int {
        return radius
    }

    override fun setRadius(value: Int): Int {
        radius = value
        return radius
    }

    override fun getPopularPointsForUser(dots: List<LocationEntity>): People {
        val f = People(dots[0].userName, dots)
        return getPopularPointsForUserForPeople(f)
    }

    override fun getPopularPointsForUser(dots: Map<String, List<LocationEntity>>): People {
        return getPopularPointsForUserForPeople(People(dots.keys.first(), dots.values.first()))
    }

    override fun getPopularPointsForUser(user: String, dots: List<LocationEntity>): People {
        return getPopularPointsForUserForPeople(People(user, dots))
    }

    override fun getPopularPointsForUsers(dots: Map<String, List<LocationEntity>>): Map<String, People> {
        return dots.map {
            it.key to getPopularPointsForUserForPeople(People(it.key, it.value))
        }.toMap()
    }

    override fun getPopularPointsForUserForPeople(user: People): People {
        val rectangle = getPopularPointsInRadius(user, radius)
        return People(user.userName).apply {
            dots = rectangle.flatten()
        }
    }

    override fun getPopularPointsInRadius(
        user: People,
        radius: Int
    ): MutableList<MutableList<LocationEntity>> {
        val listConverter: ListConverter = ListConverterImpl()
        listConverter.convertToRectangleFromDots(user.dots)
        val rectangle = listConverter.getRectangleMatrix() as MutableList<MutableList<LocationEntity>>
        val setOfDots = setOf<IntPoint>()
        rectangle.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { cellIndex, locationEntity ->
                if (cellIndex > radius && cellIndex < row.size - radius
                    && rowIndex > radius && rowIndex < rectangle.size - radius
                ) {
                    val subArray = rectangle.subList(rowIndex - radius, rowIndex + radius)
                        .map { rows ->
                            rows.subList(cellIndex - radius, cellIndex + radius)
                        }
                    val count = subArray.sumBy {
                        it.sumBy { el -> if (el.latitude != 0.0) 1 else 0 }
                    }
                    if (count / (subArray.size * subArray[0].size) >= 2 / 3) {
                        (rowIndex - radius..rowIndex + radius).forEach { itRow ->
                            (cellIndex - radius..cellIndex + radius).forEach { itCell ->
                                if (itRow != rowIndex && itCell != cellIndex)
                                    rectangle[itRow][itCell] = LocationEntity()
                            }
                        }
                    }
                }
            }
        }
        return rectangle
    }
}