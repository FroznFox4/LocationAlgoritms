package utils

import KalmanFilter.GPSDataFactory
import KalmanFilter.KalmanFilter
import KalmanFilter.Main
import models.LocationEntity
import models.TypeAlg
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.streams.toList

internal class ValidatorTest {

    private val validator = Validator()

    @Test
    fun validatePath_WithSimpleDots_CordsTwoDotsWillBeChange() {
        val firstPoint = LocationEntity(1.0, 1.0, 0f, Date(Date().time - 4))
        val secondPoint = LocationEntity(2.0, 2.0, 0f, Date(Date().time - 3))
        val threePoint = LocationEntity(2.0, 3.0, 0f, Date(Date().time - 2))
        val fourPoint = LocationEntity(4.0, 4.0, 0f, Date(Date().time - 1))
        val result = validator.validatePath(arrayListOf(firstPoint, secondPoint, threePoint, fourPoint), TypeAlg.FIRST)
        val checkValue = result[1]
        assertTrue(
            (firstPoint.latitude <= checkValue.latitude)
                .and(checkValue.latitude <= threePoint.latitude)
                .and(firstPoint.longitude <= checkValue.longitude)
                .and(checkValue.longitude <= threePoint.longitude)
        )
    }

    @Test
    fun validatePath_WithDataLikeNativeDots() {
        val bufferReader = File("A:\\javaPojects\\LocationAlgoritms\\tempData\\geo.txt").bufferedReader()
        var count = - 1
        val date = Date()
        val inputString = bufferReader.lines().map {
            count++
            val elements = it.split("; ")
            LocationEntity(elements[0].toDouble(), elements[1].toDouble(), 5f, Date(date.time - count))
        }.toList()
        print("[")
        val f: (a: Double) -> Double = { a -> (a * 10.0.pow(6)).roundToInt() / 10.0.pow(6) }
        inputString.forEachIndexed { i, it ->
            val x = f(it.latitude)
            val y = f(it.longitude)
            if (inputString.size - 1 == i)
                println("[$x, $y]")
            else
                println("[$x, $y],")
        }
        println("]")
        return
        val result = validator.validatePath(ArrayList(inputString), TypeAlg.KALMANHARD)
        result.forEach {
            val x = f(it.latitude)
            val y = f(it.longitude)
            println("$x; $y")
        }
    }

    @Test
    fun writeToFile() {
        val bufferReader = File("A:\\javaPojects\\LocationAlgoritms\\tempData\\geo.txt").bufferedReader()
        var count = - 1
        val date = Date()
        val inputString = bufferReader.lines().map {
            count+= 1000
            val elements = it.split("; ")
            LocationEntity(elements[0].toDouble(), elements[1].toDouble(), 1f, Date(date.time + count))
        }.toList()
        count=0
        File("A:\\javaPojects\\LocationAlgoritms\\tempData\\geo2.txt").bufferedWriter().use {
            out ->
            inputString.forEach {
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

    @Test
    fun readFromFile() {
        val bufferReader = File("A:\\javaPojects\\LocationAlgoritms\\tempData\\new_geo.txt").bufferedReader()
        var count = - 1
        val date = Date()
        val inputString = bufferReader.lines().map {
            count++
            val elements = it.split(" ")
            LocationEntity(elements[1].toDouble(), elements[2].toDouble(), 5f, Date(date.time - count))
        }.toList()
        inputString.forEach {
            val x = (it.latitude)
            val y = (it.longitude)
            println("$x; $y")
        }
    }
}