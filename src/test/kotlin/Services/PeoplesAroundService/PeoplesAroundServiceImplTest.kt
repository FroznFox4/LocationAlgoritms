package Services.PeoplesAroundService

import models.LocationEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import utils.ReaderAndWriter

internal class PeoplesAroundServiceImplTest {
    private val str = "tempData/geoJson.json"
    private val readerAndWriter = ReaderAndWriter(str)
    private val peoplesAroundServiceImpl: PeoplesAroundService = PeoplesAroundServiceImpl()

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
            0.5,
            user
        )
        assertEquals(result.users.size, 1)
    }
    @Test
    fun getPeoplesInRadiusForUser_DotsForTwoUsersHeHaveIntersectionAndUserIsOneOfTwoAndRadiusIsZero_TwoExistIntersectionItsSelfAndOneAnother() {
        val arrayOfDots = readerAndWriter.readFromFileAndReturnDots()
        val dotsFirst = arrayOfDots[0]
        val dotsSecond = arrayOfDots[1]
        val index = 1
        dotsSecond[index] = LocationEntity(
            dotsSecond[index].userName,
            dotsFirst[index].latitude,
            dotsFirst[index].longitude,
            dotsSecond[index].accuracy,
            dotsSecond[index].speed,
            dotsSecond[index].date)
        val dots = arrayListOf(dotsFirst, dotsSecond).flatten()
        val user = dotsFirst[index].userName
        val result = peoplesAroundServiceImpl.getPeoplesInRadiusForUser(
            dots,
            0.0,
            user
        )
        assertEquals(result.users.size, 2)
    }

    @Test
    fun getPeoplesInRadiusForUser_DotsForTwoUsersHeHaveIntersectionAndUserIsOneOfTwoAndRadiusIsZero_ALotOfExistIntersectionItsAllAnother() {
        val arrayOfDots = readerAndWriter.readFromFileAndReturnDots()
        val dotsFirst = arrayOfDots[0]
        val dotsSecond = arrayOfDots[1]
        val index = 1
        dotsSecond[index] = LocationEntity(
            dotsSecond[index].userName,
            dotsFirst[index].latitude,
            dotsFirst[index].longitude,
            dotsSecond[index].accuracy,
            dotsSecond[index].speed,
            dotsSecond[index].date)
        val dots = arrayListOf(dotsFirst, dotsSecond).flatten()
        val user = dotsSecond[index].userName
        val result = peoplesAroundServiceImpl.getPeoplesInRadiusForUser(
            dots,
            1.0,
            user
        )
        assertEquals(result.users.size, 2)
    }
}