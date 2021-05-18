package Services.PopularPointsService

import Services.PopularPointsService.Models.People
import models.LocationEntity
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PopularPointsServiceImplTest {

    val classForTest: PopularPointsService = PopularPointsServiceImpl()

    @Test
    fun getPopularPointsInRadius_RandomUserNamePointsIsEmptyRadiusIsZero_ReturnEmptyList() {
        val user = People("user")
        val result = classForTest.getPopularPointsInRadius(user, 0)
        assertTrue(result.isEmpty())
    }

    @Test
    fun getPopularPointsInRadius_RandomUserNamePointsIsEmptyRadiusIs1_ReturnEmptyList() {
        val user = People("user")
        val result = classForTest.getPopularPointsInRadius(user, 0)
        assertTrue(result.isEmpty())
    }

    @Test
    fun getPopularPointsInRadius_RandomUserNameRandomPointsRadiusIs0_ReturnNotChangedList() {
        val user = People("user",
            listOf(
                LocationEntity("user", 1.0, 1.0),
                LocationEntity("user", 1.1, 1.1)))
        val result = classForTest.getPopularPointsInRadius(user, 0)
        assertTrue(result.isEmpty())
    }
}