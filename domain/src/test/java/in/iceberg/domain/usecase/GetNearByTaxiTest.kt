package `in`.iceberg.domain.usecase

import `in`.iceberg.domain.datafactory.DataFactory
import `in`.iceberg.domain.executor.PostExecutionThread
import `in`.iceberg.domain.repository.TaxiRepository
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
class GetNearByTaxiTest {
    @Mock
    private lateinit var postExecutionThread: PostExecutionThread

    @Mock
    private lateinit var taxiRepository: TaxiRepository

    private lateinit var getNearByTaxi: GetNearByTaxi

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getNearByTaxi = GetNearByTaxi(taxiRepository, postExecutionThread)
    }

    private fun stubGetNearByTaxiResponse(single: Single<PoiListResponse>, p1Lat: Double,
                                          p1Long: Double, p2Lat: Double, p2Long: Double) {
        Mockito.`when`(taxiRepository.getNearByTaxi(p1Lat, p1Long, p2Lat, p2Long)).thenReturn(single)
    }

    @Test
    fun getNearByTaxiCompletesTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        val testObserver = getNearByTaxi.buildUseCaseObservable(GetNearByTaxi.Params.getParams(
            p1Lat, p1Long, p2Lat, p2Long)).test()
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
        val testObserver = getNearByTaxi.buildUseCaseObservable(GetNearByTaxi.Params.getParams(
            p1Lat, p1Long, p2Lat, p2Long)).test()
        testObserver.assertValue(poiListResponse)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getNearByTaxiThrowsErrorIfP1LatitudeIsZeroTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = 0.0
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        getNearByTaxi.buildUseCaseObservable(GetNearByTaxi.Params.getParams(
            p1Lat, p1Long, p2Lat, p2Long)).test()
    }

    @Test(expected = IllegalArgumentException::class)
    fun getNearByTaxiThrowsErrorIfP1LongitudeIsZeroTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = 0.0
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        getNearByTaxi.buildUseCaseObservable(GetNearByTaxi.Params.getParams(
            p1Lat, p1Long, p2Lat, p2Long)).test()
    }

    @Test(expected = IllegalArgumentException::class)
    fun getNearByTaxiThrowsErrorIfP2LatitudeIsZeroTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = 0.0
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        getNearByTaxi.buildUseCaseObservable(GetNearByTaxi.Params.getParams(
            p1Lat, p1Long, p2Lat, p2Long)).test()
    }

    @Test(expected = IllegalArgumentException::class)
    fun getNearByTaxiThrowsErrorIfP2LongitudeIsZeroTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = 0.0
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        getNearByTaxi.buildUseCaseObservable(GetNearByTaxi.Params.getParams(
            p1Lat, p1Long, p2Lat, p2Long)).test()
    }

    @Test(expected = IllegalArgumentException::class)
    fun getNearByTaxiThrowsErrorIfParamsIsNullTest() {
        val poiListResponse = DataFactory.getRandomPoiListResponse()
        val p1Lat = DataFactory.getRandomDouble()
        val p1Long = DataFactory.getRandomDouble()
        val p2Lat = DataFactory.getRandomDouble()
        val p2Long = DataFactory.getRandomDouble()
        stubGetNearByTaxiResponse(Single.just(poiListResponse), p1Lat, p1Long, p2Lat, p2Long)
        getNearByTaxi.buildUseCaseObservable(null).test()
    }

    @Test(expected = IllegalArgumentException::class)
    fun getNearByTaxiThrowsErrorIfObserverIsNullTest() {
        getNearByTaxi = GetNearByTaxi(taxiRepository, postExecutionThread)
        getNearByTaxi.execute(null)
    }
}