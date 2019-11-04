package `in`.iceberg.remote.impl

import `in`.iceberg.domain.response.PoiListResponse
import `in`.iceberg.remote.datafactory.DataFactory
import `in`.iceberg.remote.service.TaxiApiServiceFactory
import `in`.iceberg.remote.service.TaxiListService
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.verify


@RunWith(JUnit4::class)
class TaxiListImplTest {

    private val serviceFactory = Mockito.mock(TaxiApiServiceFactory::class.java)
    private val taxiListService = Mockito.mock(TaxiListService::class.java)
    private val taxiListRemoteImpl = TaxiListImpl(taxiListService)

    @Before
    fun setup() {
        stubGetService()
    }

    private fun stubGetService() {
        Mockito.`when`(serviceFactory.geTaxiListApiService()).thenReturn(taxiListService)
    }

    private fun stubGetNearByTaxiResponse(single: Single<PoiListResponse>, p1Lat: Double,
                                          p1Long: Double, p2Lat: Double, p2Long: Double) {
        Mockito.`when`(taxiListService.getListOfNearByTaxi(p1Lat, p1Long, p2Lat, p2Long)).thenReturn(single)
    }

    @Test
    fun getNearByTaxiCompletesTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        val testObserver = taxiListRemoteImpl.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long).test()
        testObserver.assertComplete()
    }

    @Test
    fun getNearTaxiListApiCalled() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        taxiListRemoteImpl.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long).test()
        verify(taxiListService).getListOfNearByTaxi(p1Lat, p1Long, p2Lat, p2Long)
    }

    @Test
    fun getNearByTaxiReturnsDataTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        val testObserver = taxiListRemoteImpl.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long).test()
        testObserver.assertValue(poiListResponse)
    }
}