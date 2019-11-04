package `in`.iceberg.data.store

import `in`.iceberg.data.repository.TaxiDataStore
import `in`.iceberg.data.repository.TaxiRemote
import `in`.iceberg.domain.response.PoiListResponse
import io.reactivex.Single
import javax.inject.Inject

class TaxiRemoteDataStore @Inject constructor(private val taxiRemote: TaxiRemote): TaxiDataStore {
    override fun getNearByTaxi(
        p1Lat: Double,
        p1Long: Double,
        p2Lat: Double,
        p2Long: Double
    ): Single<PoiListResponse> {
        return taxiRemote.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long)
    }
}