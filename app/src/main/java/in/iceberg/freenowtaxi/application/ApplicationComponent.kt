package `in`.iceberg.freenowtaxi.application

import `in`.iceberg.freenowtaxi.application.module.*
import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        UIModule::class,
        PresentationModule::class,
        DataModule::class,
        RemoteModule::class
    ]
)
interface ApplicationComponent {
    fun inject(app: TaxiApplication)
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}