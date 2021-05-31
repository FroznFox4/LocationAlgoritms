package Services.KalmanFilterService

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import models.LocationEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import utils.KalmanFilterKt.KalmanFilterImpl
import utils.ReaderAndWriter
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

internal class KalmanFilterImplTest {

    @Test
    fun initAndTestFilter() {
        val kalmanFilter = KalmanFilterImpl()
        val geoJson = "tempData/geoJson.json"
        val text = ReaderAndWriter(geoJson).readFromFile()
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
        writeToConsole(result)
        val kalmanFilterTwo = KalmanFilterImpl()
        kalmanFilterTwo.setState(result.first())
        (1 until mew.size).map {
            kalmanFilterTwo.process(mew[it])
        }
        val result2 = kalmanFilterTwo.getCollection()
        println("-----------------")
        writeToConsole(result2)
        assertTrue(result.size > 0)
    }

    private fun stringOfDotsToArray(str: String): ArrayList<ArrayList<LocationEntity>> {
        val arrayOfDots = arrayListOf<ArrayList<LocationEntity>>(arrayListOf())
        GsonBuilder().create().fromJson<ArrayList<Map<String, String>>>(str,
            object : TypeToken<ArrayList<Map<String, String>>>() {}.type
        )
            .map {
                val idS = it["id"] ?: ""
                val id = idS.toDouble()
                if (it.containsKey("dots")) {
                    val dot =
                        Gson().fromJson<Map<String, Double>>(
                            it["dots"],
                            object : TypeToken<Map<String, Double>>() {}.type
                        )
                    val speed = dot["speed"] ?: 0.0
                    val longitude = dot["longitude"] ?: 0.0
                    val latitude = dot["latitude"] ?: 0.0
                    val timestamp = dot["timestamp"] ?: 0.0
                    val date = Date(timestamp.toLong())

                    LocationEntity(
                        "",
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