package `in`.iceberg.domain.repository

import `in`.iceberg.domain.response.PoiListResponse
import io.reactivex.Single

interface TaxiRepository {
    fun getNearByTaxi(p1Lat: Double, p1Long: Double, p2Lat: Double, p2Long: Double):
            Single<PoiListResponse>
}