package utils

import KalmanFilter.GPSDataFactory
import KalmanFilter.KalmanFilter
import KalmanFilter.Main
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
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
        val firstPoint = LocationEntity(1.0, 1.0, 0f, 0.0, Date(Date().time - 4))
        val secondPoint = LocationEntity(2.0, 2.0, 0f, 0.0, Date(Date().time - 3))
        val threePoint = LocationEntity(2.0, 3.0, 0f, 0.0, Date(Date().time - 2))
        val fourPoint = LocationEntity(4.0, 4.0, 0f, 0.0, Date(Date().time - 1))
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
        val bufferReader = File("A:\\javaPojects\\LocationAlgoritms\\tempData\\geoJson.json").bufferedReader()
        var count = - 1
        val date = Date()
        val inputString = stringOfDotsToArray(bufferReader.readText())
        inputString.forEach { el ->
            val result = validator.validatePath(ArrayList(el), TypeAlg.KALMANHARD)
        }
    }

    @Test
    fun writeToFile() {
        val bufferReader = File("A:\\javaPojects\\LocationAlgoritms\\tempData\\geo.txt").bufferedReader()
        var count = - 1
        val date = Date()
        val inputString = readFromFile()
        count=0
        File("A:\\javaPojects\\LocationAlgoritms\\tempData\\geo2.txt").bufferedWriter().use {
            out ->
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

    @Test
    fun readFromFile(): ArrayList<ArrayList<LocationEntity>> {
        val bufferReader = File("A:\\javaPojects\\LocationAlgoritms\\tempData\\geoJson.json").bufferedReader()
        val text = bufferReader.readText()
        return stringOfDotsToArray(text)
    }

    fun stringOfDotsToArray(str: String): ArrayList<ArrayList<LocationEntity>> {
        val arrayOfDots = arrayListOf<ArrayList<LocationEntity>>(arrayListOf())
        GsonBuilder().create().fromJson<ArrayList<Map<String, String>>>(str,
            object: TypeToken<ArrayList<Map<String, String>>>() {}.type)
            .map {
                val idS = it["id"] ?: ""
                val id = idS.toDouble()
                if (it.containsKey("dots")) {
                    val dot =
                        Gson().fromJson<Map<String, Double>>(it["dots"], object : TypeToken<Map<String, Double>>() {}.type)
                    val speed = dot["speed"] ?: 0.0
                    val longitude = dot["longitude"] ?: 0.0
                    val latitude = dot["latitude"] ?: 0.0
                    val timestamp = dot["timestamp"] ?: 0.0
                    val date = Date(timestamp.toLong())
                    LocationEntity(
                        latitude,
                        longitude,
                        5.0F,
                        speed,
                        date
                    )
                } else {
                    LocationEntity()
                }
            }
            .forEach {
                val constDate = 20000
                val lastIndex = arrayOfDots.lastIndex
                val lastAr = arrayOfDots[lastIndex]
                if (lastAr.size != 0) {
                    if (lastAr.last().date.time - it.date.time < constDate)
                        arrayOfDots[lastIndex].add(it)
                    else {
                        arrayOfDots.add(arrayListOf())
                        arrayOfDots[arrayOfDots.lastIndex].add(it)
                    }
                } else
                    arrayOfDots[lastIndex].add(it)
            }
        return arrayOfDots
    }
}