package utils

import models.LocationEntity
import models.TypeAlg
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import java.io.File
import java.util.*
import kotlin.random.Random

//TODO("Needed for repairing")
internal class ValidatorTest {

    private val validator = Validator()

    @BeforeAll
    fun setupClass() {
        throw RuntimeException("Sorry dude, you won't find any test!")
    }

    //    @Test
    fun validatePath_WithSimpleDots_CordsTwoDotsWillBeChange() {
        val firstPoint = LocationEntity("", 1.0, 1.0, 0f, 0.0, Date(Date().time - 4))
        val secondPoint = LocationEntity("", 2.0, 2.0, 0f, 0.0, Date(Date().time - 3))
        val threePoint = LocationEntity("", 2.0, 3.0, 0f, 0.0, Date(Date().time - 2))
        val fourPoint = LocationEntity("", 4.0, 4.0, 0f, 0.0, Date(Date().time - 1))
        val result = validator.validatePath(arrayListOf(firstPoint, secondPoint, threePoint, fourPoint), TypeAlg.FIRST)
        val checkValue = result[1]
        assertTrue(
            (firstPoint.latitude <= checkValue.latitude)
                .and(checkValue.latitude <= threePoint.latitude)
                .and(firstPoint.longitude <= checkValue.longitude)
                .and(checkValue.longitude <= threePoint.longitude)
        )
    }

    //    @Test
    fun validatePath_WithDataLikeNativeDots() {
        val bufferReader = File("tempData/geoJson.json").bufferedReader()
        var count = -1
        val date = Date()
        val inputString = ReaderAndWriter("tempData/geoJson.json").readFromFileAndReturnDots()
        inputString.forEach { el ->
            val result = validator.validatePath(ArrayList(el), TypeAlg.KALMANHARD)
        }
    }

    //    @Test
    fun writeToFile() {
        val bufferReader = File("A:\\javaPojects\\LocationAlgoritms\\tempData\\geo.txt").bufferedReader()
        var count = -1
        val date = Date()
        val inputString = ReaderAndWriter("tempData/geoJson.json").readFromFileAndReturnDots()
        count = 0
        File("A:\\javaPojects\\LocationAlgoritms\\tempData\\geo2.txt").bufferedWriter().use { out ->
            inputString.forEach {
                if (it.size in 21..49) {
                    it.forEach {
                        count++
                        val x = it.latitude
                        val y = it.longitude
                        val time = it.date.time
                        val speed = 1
                        val accuracy = Random.nextLong(1, 2)
                        out.write("$count $speed $x $y $time $accuracy\n")
                    }
                }
            }
        }
    }
}