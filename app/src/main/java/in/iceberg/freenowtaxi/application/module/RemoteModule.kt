package `in`.iceberg.freenowtaxi.application.module

import `in`.iceberg.data.repository.TaxiRemote
import `in`.iceberg.remote.impl.TaxiListImpl
import `in`.iceberg.remote.service.TaxiApiServiceFactory
import `in`.iceberg.remote.service.TaxiListService
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class RemoteModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        fun providesTaxiListService() : TaxiListService {
            return TaxiApiServiceFactory.geTaxiListApiService()
        }
    }
    @Binds
    abstract fun bindsTaxiRemote(taxiListImpl: TaxiListImpl): TaxiRemote
}