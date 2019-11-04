package `in`.iceberg.freenowtaxi.application.module

import `in`.iceberg.freenowtaxi.fragments.TaxiListFragments
import `in`.iceberg.freenowtaxi.fragments.TaxiMapFragments
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class TaxiFragmentModule {
    @ContributesAndroidInjector
    @FragmentScope
    abstract fun taxiListFragment(): TaxiListFragments

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun taxiMapFragment(): TaxiMapFragments
}