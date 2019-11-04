package `in`.iceberg.freenowtaxi.application.module

import `in`.iceberg.freenowtaxi.dependencies.ViewModelFactory
import `in`.iceberg.freenowtaxi.dependencies.ViewModelKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import viewmodel.GetNearByTaxiViewModel

@Module
abstract class PresentationModule {
    @Binds
    @IntoMap
    @ViewModelKey(GetNearByTaxiViewModel::class)
    abstract fun bindsGetNearByTaxiViewModel(getNearByTaxiViewModel: GetNearByTaxiViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}