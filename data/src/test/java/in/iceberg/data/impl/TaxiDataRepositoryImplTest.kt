package `in`.iceberg.data.impl

import `in`.iceberg.data.datafactory.DataFactory
import `in`.iceberg.data.store.TaxiDataStoreFactory
import `in`.iceberg.data.store.TaxiRemoteDataStore
import `in`.iceberg.domain.response.PoiListResponse
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class TaxiDataRepositoryImplTest {

    private val dataStoreFactory = Mockito.mock(TaxiDataStoreFactory::class.java)
    private val remoteDataStore = Mockito.mock(TaxiRemoteDataStore::class.java)

    private val taxiDataRepository = TaxiDataRepositoryImpl(dataStoreFactory)

    @Before
    fun setup() {
        Mockito.`when`(dataStoreFactory.taxiRemoteDataStore).thenReturn(remoteDataStore)
    }

    private fun stubGetNearByTaxiResponse(single: Single<PoiListResponse>, p1Lat: Double,
                                          p1Long: Double, p2Lat: Double, p2Long: Double) {
        Mockito.`when`(remoteDataStore.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long)).thenReturn(single)
    }

    @Test
    fun getNearByTaxiCompletesTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        val testObserver = taxiDataRepository.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long).test()
        testObserver.assertComplete()
    }

    @Test
    fun getNearByTaxiReturnsTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        val testObserver = taxiDataRepository.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long).test()
        testObserver.assertValue(poiListResponse)
    }
}