package `in`.iceberg.data.repository

import `in`.iceberg.domain.response.PoiListResponse
import io.reactivex.Single

interface TaxiDataStore {
    fun getNearByTaxi(p1Lat: Double, p1Long: Double, p2Lat: Double, p2Long: Double):
            Single<PoiListResponse>
}