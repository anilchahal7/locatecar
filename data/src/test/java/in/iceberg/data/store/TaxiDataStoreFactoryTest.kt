package `in`.iceberg.data.store

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class TaxiDataStoreFactoryTest {

    @Mock
    private lateinit var taxiRemoteDataStore: TaxiRemoteDataStore

    private lateinit var taxiDataStoreFactory: TaxiDataStoreFactory

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        taxiDataStoreFactory = TaxiDataStoreFactory(taxiRemoteDataStore)
    }

    @Test
    fun getsRemoteDataStoreCorrectlyTest() {
        Assert.assertEquals(taxiRemoteDataStore, taxiDataStoreFactory.taxiRemoteDataStore)
    }
}