package utils

import KalmanFilter.GPSDataFactory
import KalmanFilter.KalmanFilter
import models.LocationEntity
import models.TypeAlg
import kotlin.math.pow
import kotlin.math.sqrt

class Validator {

    fun validatePath(coordinates: ArrayList<LocationEntity>, algorithm: TypeAlg): List<LocationEntity> {
        return coordinates
            .sortedBy { it.date }
            .run {
                when(algorithm) {
                    TypeAlg.FIRST -> firstSolution(ArrayList(this))
                    TypeAlg.SECOND -> secondSolution(ArrayList(this))
                    TypeAlg.KALMAN -> kalmanFilterSimple(ArrayList(this))
                    TypeAlg.KALMANHARD -> kalmanFilterHard(ArrayList(this))
                }
            }
    }

    fun validatePath2(coordinates: ArrayList<LocationEntity>): List<LocationEntity> {
        return coordinates
            .sortedBy { it.date }
            .run {
                firstSolution(ArrayList(this))
            }
    }

    val distance: (firstEl: LocationEntity, secondEl: LocationEntity) -> Double = { firstEl, secondEl ->
        sqrt((firstEl.latitude - secondEl.latitude).pow(2) + (firstEl.longitude - secondEl.longitude).pow(2))
    }

    fun firstSolution(dots: ArrayList<LocationEntity>): ArrayList<LocationEntity> {
        val result = arrayListOf(dots[0])
        for (el in 1 until dots.size - 1) {
            val prevEl = dots[el - 1]
            val curEl = dots[el]
            val nextEl = dots[el + 1]

            val coefficient = 2
            val distanceFirstToThree = distance(prevEl, nextEl)
            val distanceFirstToSecond = distance(prevEl, curEl)
            val distanceSecondToThree = distance(curEl, nextEl)
            val coefDistanceBetweenTwoPoints = distanceFirstToThree / coefficient
            if ((distanceFirstToSecond <= coefDistanceBetweenTwoPoints)
                    .and(distanceSecondToThree <= coefDistanceBetweenTwoPoints)
            )
                result.add(curEl)
            else
                result.add(
                    LocationEntity(
                        "",
                        (prevEl.latitude + nextEl.latitude) / 2,
                        (prevEl.longitude + nextEl.longitude) / 2,
                        0.0f,
                        0.0,
                        curEl.date
                    )
                )
        }
        result.add(dots.last())
        return result
    }

    fun secondSolution(dots: ArrayList<LocationEntity>): ArrayList<LocationEntity> {
        val result = arrayListOf(dots[0], dots[1])
        val speedFunction: (dot2: LocationEntity, dot1: LocationEntity) -> Double = { dot2, dot1 ->
            distance(dot2, dot1) / (dot2.date.time - dot1.date.time)
        }
        val speed = 0.0015
        var varSpeed = 1
        for (el in 2 until dots.size) {
            val prevEl = dots[el - varSpeed]
            val curEl = dots[el]
            val curSpeed = speedFunction(curEl, prevEl)
            val maxSpeed = speed * varSpeed
            if (curSpeed < maxSpeed) {
                varSpeed = 1
                result.add(curEl)
            } else {
                varSpeed += 1
            }
        }
        return result
    }

    fun kalmanFilterSimple(dots: ArrayList<LocationEntity>): ArrayList<LocationEntity> {
        val varVolt = 1.25 // Среднее откл
        val varProcess = 1 //Скорость реакции
        var pc = 0.0
        var g = 0.0
        var p = 1.0
        var xp = 0.0
        var zp = 0.0
        var xe = 0.0

        var pc1 = 0.0
        var g1 = 0.0
        var p1 = 1.0
        var xp1 = 0.0
        var zp1 = 0.0
        var xe1 = 0.0

        val filter: (locationEntity: LocationEntity) -> LocationEntity = { el ->
            pc = p + varProcess
            g = pc / (pc + varVolt)
            p = (1-g)*pc
            xp = xe
            zp = xp
            xe = g*(el.latitude - zp) + xp

            pc1 = p1 + varProcess
            g1 = pc1 / (pc1 + varVolt)
            p1 = (1-g1)*pc1
            xp1 = xe1
            zp1 = xp1
            xe1 = g1*(el.longitude - zp1) + xp1

            LocationEntity(el.userName, xe1, xe, 0.0f, 0.0, el.date)
        }

        return ArrayList(dots.map {
            filter(it)
        })
    }

    fun kalmanFilterHard(dots: ArrayList<LocationEntity>): ArrayList<LocationEntity> {
        KalmanFilter()
        GPSDataFactory()
        return arrayListOf()
    }
}