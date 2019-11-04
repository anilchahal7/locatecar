package `in`.iceberg.freenowtaxi.application.module

import `in`.iceberg.domain.executor.PostExecutionThread
import `in`.iceberg.freenowtaxi.activity.MainActivity
import `in`.iceberg.freenowtaxi.application.UIThread
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIModule {
    @Binds
    abstract fun bindsPostExecutionThread(uiThread: UIThread): PostExecutionThread

    @ContributesAndroidInjector(modules = [TaxiFragmentModule::class])
    abstract fun contributesTaxiMainActivity(): MainActivity
}