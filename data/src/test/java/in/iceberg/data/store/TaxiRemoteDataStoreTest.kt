package `in`.iceberg.data.store

import `in`.iceberg.data.datafactory.DataFactory
import `in`.iceberg.data.repository.TaxiRemote
import `in`.iceberg.domain.response.PoiListResponse
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class TaxiRemoteDataStoreTest {

    @Mock
    private lateinit var taxiRemote: TaxiRemote

    private lateinit var taxiRemoteDataStore: TaxiRemoteDataStore

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        taxiRemoteDataStore = TaxiRemoteDataStore(taxiRemote)
    }

    private fun stubGetNearByTaxiResponse(single: Single<PoiListResponse>, p1Lat: Double,
                                          p1Long: Double, p2Lat: Double, p2Long: Double) {
        Mockito.`when`(taxiRemote.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long)).thenReturn(single)
    }

    @Test
    fun getNearByTaxiCompletesTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        val testObserver = taxiRemoteDataStore.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long).test()
        testObserver.assertComplete()
    }

    @Test
    fun getNearByTaxiReturnsDataTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        val testObserver = taxiRemoteDataStore.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long).test()
        testObserver.assertValue(poiListResponse)
    }
}