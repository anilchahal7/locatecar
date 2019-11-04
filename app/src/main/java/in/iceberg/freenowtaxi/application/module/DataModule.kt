package `in`.iceberg.freenowtaxi.application.module

import `in`.iceberg.data.impl.TaxiDataRepositoryImpl
import `in`.iceberg.domain.repository.TaxiRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {
    @Binds
    abstract fun bindsTaxiRepository(taxiDataRepositoryImpl: TaxiDataRepositoryImpl): TaxiRepository
}