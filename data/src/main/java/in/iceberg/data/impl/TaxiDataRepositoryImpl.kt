package `in`.iceberg.data.impl

import `in`.iceberg.data.store.TaxiDataStoreFactory
import `in`.iceberg.domain.repository.TaxiRepository
import `in`.iceberg.domain.response.PoiListResponse
import io.reactivex.Single
import javax.inject.Inject

class TaxiDataRepositoryImpl @Inject constructor(
        private val taxiDataStoreFactory: TaxiDataStoreFactory
    ): TaxiRepository {
    override fun getNearByTaxi(
        p1Lat: Double,
        p1Long: Double,
        p2Lat: Double,
        p2Long: Double
    ): Single<PoiListResponse> {
        return taxiDataStoreFactory.taxiRemoteDataStore.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long)
    }
}