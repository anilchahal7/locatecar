package `in`.iceberg.freenowtaxi.fragments

import `in`.iceberg.domain.response.PoiListResponse
import `in`.iceberg.freenowtaxi.R
import `in`.iceberg.freenowtaxi.adapters.TaxiListAdapter
import `in`.iceberg.freenowtaxi.constants.Constant.hamburgP1Lat
import `in`.iceberg.freenowtaxi.constants.Constant.hamburgP1Long
import `in`.iceberg.freenowtaxi.constants.Constant.hamburgP2Lat
import `in`.iceberg.freenowtaxi.constants.Constant.hamburgP2Long
import `in`.iceberg.freenowtaxi.dependencies.ViewModelFactory
import `in`.iceberg.presentation.state.ResourceState
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.taxi_list_fragment_layout.view.*
import viewmodel.GetNearByTaxiViewModel
import javax.inject.Inject

class TaxiListFragments : DaggerFragment() {

    @Inject
    lateinit var viewModel: GetNearByTaxiViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var progressBar: ProgressBar

    private var poiListResponse: PoiListResponse? = null
    private var isViewCreated: Boolean = false


    @Inject
    lateinit var listAdapter: TaxiListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.taxi_list_fragment_layout, container, false)
        initUI(view)
        isViewCreated = true
        return view
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && poiListResponse == null &&
            poiListResponse?.poiList == null && isViewCreated) {
            fetchGetNearByTaxiResponse()
        }
        super.setUserVisibleHint(isVisibleToUser)
    }

    private fun initUI(view: View) {
        listAdapter = TaxiListAdapter()
        view.taxi_list_recycler_view.layoutManager = LinearLayoutManager(activity)
        view.taxi_list_recycler_view.adapter = listAdapter
        progressBar = view.progress_circular
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
            GetNearByTaxiViewModel::class.java)
        observeGetNearByTaxiResponse()
        fetchGetNearByTaxiResponse()
    }

    private fun fetchGetNearByTaxiResponse() {
        progressBar.visibility = View.VISIBLE
        showToast()
        viewModel.getNearByTaxiResponse(
            hamburgP1Lat,
            hamburgP1Long,
            hamburgP2Lat,
            hamburgP2Long
        )
    }

    private fun observeGetNearByTaxiResponse() {
        viewModel.observeGetNearByTaxiResponse().observe(this, Observer {
            when (it?.status) {
                ResourceState.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    poiListResponse = it.data
                    it.data?.poiList.let { response ->
                        listAdapter.poiDataList = response!!
                        listAdapter.context = activity as Context
                        listAdapter.notifyDataSetChanged()
                    }
                }
                ResourceState.ERROR -> {
                    progressBar.visibility = View.GONE
                    Snackbar.make(activity?.window?.decorView?.rootView!!,
                        context!!.getString(R.string.error_in_request),
                        Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        })
    }

    private fun showToast() {
        Toast.makeText(activity, context!!.getString(R.string.loading_taxi_list), Toast.LENGTH_SHORT).show()
    }
}