package viewmodel

import `in`.iceberg.domain.response.PoiListResponse
import `in`.iceberg.domain.usecase.GetNearByTaxi
import `in`.iceberg.presentation.state.Resource
import `in`.iceberg.presentation.state.ResourceState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class GetNearByTaxiViewModel @Inject
    constructor(
        private val getNearByTaxi: GetNearByTaxi
    ): ViewModel() {

    private val getNearByTaxiListLiveData = MutableLiveData<Resource<PoiListResponse>>()

    override fun onCleared() {
        getNearByTaxi.disposeAll()
    }

    // Observe Get Near Bt Taxi List Response ...
    fun observeGetNearByTaxiResponse() : LiveData<Resource<PoiListResponse>> {
        return getNearByTaxiListLiveData
    }

    // Get Near Bt Taxi List Response ...
    fun getNearByTaxiResponse(p1Lat: Double, p1Long: Double, p2Lat: Double, p2Long: Double) {
        getNearByTaxiListLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        getNearByTaxi.execute(GetNearByTaxiResponseSubscriber(), GetNearByTaxi.Params.
            getParams(p1Lat, p1Long, p2Lat, p2Long))
    }

    // Get Near Bt Taxi List Response Subscriber ...
    private inner class GetNearByTaxiResponseSubscriber : DisposableSingleObserver<PoiListResponse>() {
        override fun onSuccess(t: PoiListResponse) {
            getNearByTaxiListLiveData.postValue(Resource(ResourceState.SUCCESS, t, null))
        }
        override fun onError(e: Throwable) {
            getNearByTaxiListLiveData.postValue(Resource(ResourceState.ERROR, null, e.message))
        }
    }
}