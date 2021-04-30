package utils.KalmanFilterKtTest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import models.LocationEntity
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

internal class KalmanFilterTest {

    @Test
    fun initAndTestFilter() {
        val kalmanFilter = KalmanFilterKt()
        val geoJson = "A:\\javaPojects\\LocationAlgoritms\\tempData\\geoJson.json"
        val bufferReader = File(geoJson).bufferedReader()
        val text = bufferReader.readText()
        val dots = stringOfDotsToArray(text)
        val mew = dots[1]
        kalmanFilter.setState(mew.first())
        (1 until mew.size).map {
            val x = mew[it].latitude
            val y = mew[it].longitude
            println("$x $y")
            kalmanFilter.process(mew[it])
        }
        println("-----------------")
        val result = kalmanFilter.getCollection()
        writeToConsole(kalmanFilter.getCollection())
    }

    private fun stringOfDotsToArray(str: String): ArrayList<ArrayList<LocationEntity>> {
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
                        ThreadLocalRandom.current().nextDouble(0.0, 10.0).toFloat(),
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
    private fun writeToConsole(locationEntities: List<LocationEntity>) {
        locationEntities.map {
            val x = it.latitude
            val y = it.longitude
            println("$x; $y")
        }
    }
}