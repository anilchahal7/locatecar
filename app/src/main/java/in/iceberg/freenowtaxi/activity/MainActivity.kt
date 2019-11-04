package `in`.iceberg.freenowtaxi.activity

import `in`.iceberg.freenowtaxi.R
import `in`.iceberg.freenowtaxi.adapters.TabAdapter
import `in`.iceberg.freenowtaxi.fragments.TaxiListFragments
import `in`.iceberg.freenowtaxi.fragments.TaxiMapFragments
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var tabAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabAdapter = TabAdapter(supportFragmentManager)
        tabAdapter.addFragment(TaxiListFragments(), resources.getString(R.string.tab_one))
        tabAdapter.addFragment(TaxiMapFragments(), resources.getString(R.string.tab_two))
        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}