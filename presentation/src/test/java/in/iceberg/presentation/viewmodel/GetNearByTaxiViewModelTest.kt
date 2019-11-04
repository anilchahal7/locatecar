package `in`.iceberg.presentation.viewmodel

import `in`.iceberg.domain.response.PoiListResponse
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import `in`.iceberg.domain.usecase.GetNearByTaxi
import `in`.iceberg.presentation.datafactory.DataFactory
import `in`.iceberg.presentation.state.ResourceState
import com.nhaarman.mockito_kotlin.*
import io.reactivex.observers.DisposableSingleObserver
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Captor
import org.mockito.Mockito
import viewmodel.GetNearByTaxiViewModel
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class GetNearByTaxiViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Captor
    private val captor = argumentCaptor<DisposableSingleObserver<PoiListResponse>>()

    private val getNearByTaxi = mock<GetNearByTaxi>()
    private val getNearByTaxiViewModel = GetNearByTaxiViewModel(getNearByTaxi)

    @Test
    fun testGetNearByTaxiUseCase() {
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        getNearByTaxiViewModel.getNearByTaxiResponse(p1Lat, p1Long, p2Lat, p2Long)
        verify(getNearByTaxi, times(1)).execute(any(),
            eq(GetNearByTaxi.Params(p1Lat, p1Long, p2Lat, p2Long)))
    }

    @Test
    fun testGetNearByTaxiReturnsData() {
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        getNearByTaxiViewModel.getNearByTaxiResponse(p1Lat, p1Long, p2Lat, p2Long)
        verify(getNearByTaxi, times(1)).execute(captor.capture(),
            eq(GetNearByTaxi.Params(p1Lat, p1Long, p2Lat, p2Long)))
        captor.firstValue.onSuccess(poiListResponse)
        assertEquals(poiListResponse, getNearByTaxiViewModel.observeGetNearByTaxiResponse().value?.data)
        assertEquals(ResourceState.SUCCESS, getNearByTaxiViewModel.observeGetNearByTaxiResponse().value?.status)
    }

    @Test
    fun testGetNearByTaxiReturnsError() {
        val message = DataFactory.getRandomString()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        getNearByTaxiViewModel.getNearByTaxiResponse(p1Lat, p1Long, p2Lat, p2Long)
        verify(getNearByTaxi).execute(captor.capture(), eq(GetNearByTaxi.Params(p1Lat, p1Long, p2Lat, p2Long)))
        captor.firstValue.onError(RuntimeException(message))
        assertEquals(ResourceState.ERROR, getNearByTaxiViewModel.observeGetNearByTaxiResponse().value?.status)
        assertEquals(message, getNearByTaxiViewModel.observeGetNearByTaxiResponse().value?.error)
    }

    @Test
    fun getNearByTaxiCompletesTest() {
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        getNearByTaxiViewModel.getNearByTaxiResponse(p1Lat, p1Long, p2Lat, p2Long)
        verify(getNearByTaxi).execute(Mockito.any<DisposableSingleObserver<PoiListResponse>>(),
            eq(GetNearByTaxi.Params.getParams(p1Lat, p1Long, p2Lat, p2Long)))
    }
}