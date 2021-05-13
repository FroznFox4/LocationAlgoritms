package Services.PeoplesAroundService

import Services.PeoplesAroundService.Utils.MapConverter
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import utils.ReaderAndWriter

internal class PeoplesAroundServiceImplTest {
    val str = "tempData/geoJson.json"
    val readerAndWriter = ReaderAndWriter(str)
    val peoplesAroundServiceImpl: PeoplesAroundService = PeoplesAroundServiceImpl()

    @Test
    fun getPeoplesInRadiusForUser_dotsForOneUserAndUserIsSelfAndRadiusIsZero_oneExistIntersection() {
        val arrayOfDots = readerAndWriter.readFromFileAndReturnDots()
        val dots = arrayOfDots[1]
        val user = arrayOfDots[1][0].userName
        val result = peoplesAroundServiceImpl.getPeoplesInRadiusForUser(
            dots,
            0.0,
            user
        )
        assertEquals(result.users.size, 1)
    }

    @Test
    fun getPeoplesInRadiusForUser_dotsForOneUserAndUserIsSelfAndRadiusIsNotZero_oneExistIntersection() {
        val arrayOfDots = readerAndWriter.readFromFileAndReturnDots()
        val dots = arrayOfDots[1]
        val user = arrayOfDots[1][0].userName
        val result = peoplesAroundServiceImpl.getPeoplesInRadiusForUser(
            dots,
            1.0,
            user
        )
        assertEquals(result.users.size, 1)
    }
}