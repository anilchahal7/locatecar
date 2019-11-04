package `in`.iceberg.data.store

import javax.inject.Inject

class TaxiDataStoreFactory @Inject constructor(val taxiRemoteDataStore: TaxiRemoteDataStore)