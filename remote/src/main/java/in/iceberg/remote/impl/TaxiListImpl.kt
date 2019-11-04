package `in`.iceberg.remote.impl

import `in`.iceberg.data.repository.TaxiRemote
import `in`.iceberg.domain.response.PoiListResponse
import `in`.iceberg.remote.service.TaxiListService
import io.reactivex.Single
import javax.inject.Inject

class TaxiListImpl @Inject constructor(private val taxiListService: TaxiListService): TaxiRemote {
    override fun getNearByTaxi(p1Lat: Double, p1Long: Double, p2Lat: Double, p2Long: Double):
            Single<PoiListResponse> {
        return taxiListService.getListOfNearByTaxi(p1Lat, p1Long, p2Lat, p2Long)
    }
}