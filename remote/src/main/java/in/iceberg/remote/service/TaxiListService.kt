package `in`.iceberg.remote.service

import `in`.iceberg.domain.response.PoiListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TaxiListService {
    @GET("/")
    fun getListOfNearByTaxi(
        @Query("p1Lat") p1Lat: Double,
        @Query("p1Lon") p1Long: Double,
        @Query("p2Lat") p2Lat: Double,
        @Query("p2Lon") p2Long: Double
    ): Single<PoiListResponse>
}