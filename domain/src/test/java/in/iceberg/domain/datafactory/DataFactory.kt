package `in`.iceberg.domain.datafactory

import `in`.iceberg.domain.model.Coordinate
import `in`.iceberg.domain.model.PoiData
import `in`.iceberg.domain.response.PoiListResponse
import java.util.UUID

object DataFactory {
    private fun getRandomString(): String {
        return UUID.randomUUID().toString()
    }

    private fun getRandomInt(): Int {
        return (Math.random() * 1000).toInt()
    }

    fun getRandomDouble(): Double {
        return (10) * Math.random()
    }

    private fun getRandomCoordinate(): Coordinate {
        return Coordinate(getRandomDouble(), getRandomDouble())
    }

    private fun getRandomPoiData(): PoiData {
        return PoiData(getRandomInt(), getRandomCoordinate(), getRandomString(), getRandomDouble())
    }

    private fun getPoiList(): MutableList<PoiData> {
        val results = mutableListOf<PoiData>()
        repeat(20) {
            results.add(getRandomPoiData())
        }
        return results
    }

    fun getRandomPoiListResponse(): PoiListResponse {
        return PoiListResponse(getPoiList())
    }
}