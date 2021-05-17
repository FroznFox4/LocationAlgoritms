package utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import models.LocationEntity
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

@Disabled
@Deprecated("All move to Reader And Writer")
internal class ValidatorTestDepr{

    @Test
    fun readFromFileMew() {
        val bufferReader = File("tempData/geoJson.json").bufferedReader()
        val text = bufferReader.readText()
        val result = stringOfDotsToArray(text)
    }

    private fun stringOfDotsToArray(str: String): ArrayList<ArrayList<LocationEntity>> {
        val arrayOfDots = arrayListOf<ArrayList<LocationEntity>>()
        GsonBuilder().create().fromJson<ArrayList<Map<String, String>>>(
            str,
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
                    val user = it["users"] ?: ""
                    val date = Date(timestamp.toLong())
                    LocationEntity(
                        user,
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