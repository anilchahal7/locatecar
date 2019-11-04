package `in`.iceberg.freenowtaxi.application

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.droidnet.DroidNet
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class TaxiApplication: Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> {
        return androidInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return mFragmentInjector
    }

    override fun onCreate() {
        super.onCreate()
        DroidNet.init(this)
        DaggerApplicationComponent.builder().application(this).build().inject(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners()
    }
}