package `in`.iceberg.domain.usecase

import `in`.iceberg.common.ApplicationExceptions
import `in`.iceberg.domain.executor.PostExecutionThread
import `in`.iceberg.domain.repository.TaxiRepository
import `in`.iceberg.domain.response.PoiListResponse
import `in`.iceberg.domain.usecase.common.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetNearByTaxi @Inject
    constructor(private val taxiRepository: TaxiRepository,
                postExecutionThread: PostExecutionThread
    ): SingleUseCase<PoiListResponse, GetNearByTaxi.Params>(postExecutionThread) {
    override fun buildUseCaseObservable(params: Params?): Single<PoiListResponse> {
        params?.let {

            require(it.p1Lat != 0.0) { ApplicationExceptions.NO_ZERO_LAT_LONG }

            require(it.p1Long != 0.0) { ApplicationExceptions.NO_ZERO_LAT_LONG }

            require(it.p2Lat != 0.0) { ApplicationExceptions.NO_ZERO_LAT_LONG }

            require(it.p2Long != 0.0) { ApplicationExceptions.NO_ZERO_LAT_LONG }

            return taxiRepository.getNearByTaxi(it.p1Lat, it.p1Long, it.p2Lat, it.p2Long)
        }
        throw IllegalArgumentException()
    }

    data class Params constructor(val p1Lat: Double, val p1Long: Double, val p2Lat: Double,
                                  val p2Long: Double) {
        companion object {
            fun getParams(p1Lat: Double, p1Long: Double, p2Lat: Double, p2Long: Double) =
                Params(p1Lat, p1Long, p2Lat, p2Long)
        }
    }
}