package `in`.iceberg.freenowtaxi.fragments

import `in`.iceberg.domain.response.PoiListResponse
import `in`.iceberg.freenowtaxi.R
import `in`.iceberg.freenowtaxi.constants.Constant.hamburgP1Lat
import `in`.iceberg.freenowtaxi.constants.Constant.hamburgP1Long
import `in`.iceberg.freenowtaxi.constants.Constant.hamburgP2Lat
import `in`.iceberg.freenowtaxi.constants.Constant.hamburgP2Long
import `in`.iceberg.freenowtaxi.dependencies.ViewModelFactory
import `in`.iceberg.freenowtaxi.interfaces.GetSortingType
import `in`.iceberg.freenowtaxi.interfaces.OnCloseClick
import `in`.iceberg.freenowtaxi.interfaces.OnSwitchSelect
import `in`.iceberg.freenowtaxi.model.SortingType
import `in`.iceberg.freenowtaxi.util.Util
import `in`.iceberg.presentation.state.ResourceState
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.droidnet.DroidListener
import com.droidnet.DroidNet
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.taxi_sort_view_container.*
import kotlinx.android.synthetic.main.taxi_sort_view_container.view.*
import viewmodel.GetNearByTaxiViewModel
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

class TaxiMapFragments : DaggerFragment(), OnMapReadyCallback,
        GetSortingType,
        OnCloseClick,
        OnSwitchSelect,
        DroidListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener {

    @Inject
    lateinit var viewModel: GetNearByTaxiViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mMap: GoogleMap
    private lateinit var droidNet: DroidNet

    private lateinit var selectTaxiFragment: SelectTaxiFragment
    private var sortingTypeList: MutableList<SortingType> = mutableListOf()

    private var poiListResponse: PoiListResponse? = null

    private var isDragged: Boolean = false
    private var isNetConnected: Boolean = true
    private var makeAPICallOnEveryMapMove: Boolean = true
    private var isViewCreated: Boolean = false

    private var p1Lat: Double = hamburgP1Lat
    private var p1Long: Double = hamburgP1Long
    private var p2Lat: Double = hamburgP2Lat
    private var p2Long: Double = hamburgP2Long

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.taxi_map_fragment_layout, container, false)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GetNearByTaxiViewModel::class.java)
        observeGetNearByTaxiResponse()
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        view.sort_view_container.setOnClickListener {
            showSortingBottomSheet()
        }
        isViewCreated = true
        droidNet = DroidNet.getInstance()
        droidNet.addInternetConnectivityListener(this)
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

    private fun showSortingBottomSheet() {
        try {
            if (sortingTypeList.size > 0) {
                selectTaxiFragment = SelectTaxiFragment(this, this, this,
                    makeAPICallOnEveryMapMove, sortingTypeList)
            } else {
                sortingTypeList = Util.sortingTypeList
                selectTaxiFragment = SelectTaxiFragment(this, this, this,
                    makeAPICallOnEveryMapMove, Util.sortingTypeList)
            }
            selectTaxiFragment.show(fragmentManager!!, context?.getString(R.string.bottom_sheet))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun observeGetNearByTaxiResponse() {
        viewModel.observeGetNearByTaxiResponse().observe(this, Observer {
            when (it?.status) {
                ResourceState.SUCCESS -> {
                    val data = it.data
                    if (data != null) {
                        it.data.let { response ->
                            poiListResponse = response!!
                            sortingTypeList = Util.sortingTypeList
                            sort_type_text.text = Util.sortingTypeList[0].type
                            for (item in poiListResponse!!.poiList) {
                                makeApiRequest(false)
                                setMapLatLongForTaxi(
                                    item.coordinate.latitude, item.coordinate.longitude, item.fleetType,
                                    item.heading, true, null)
                            }
                        }
                    }
                }
                ResourceState.ERROR -> {
                    makeApiRequest(false)
                    showToast()
                }
                else -> {
                }
            }
        })
    }

    private fun fetchGetNearByTaxiResponse() {
        makeApiRequest(true)
        setMapLatLongForTaxi(p1Lat, p1Long, null, 0.0,
            false, "Point P1")
        setMapLatLongForTaxi(p2Lat, p2Long, null, 0.0,
            false, "Point P2")
        viewModel.getNearByTaxiResponse(p1Lat, p1Long, p2Lat, p2Long)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMarkerClickListener(this)
        fetchGetNearByTaxiResponse()
    }

    private fun setMapLatLongForTaxi(
        latitude: Double, longitude: Double, fleetType: String?,
        heading: Double, isTaxiType: Boolean, title: String?) {
        val latLng = LatLng(latitude, longitude)
        if (isTaxiType) {
            mMap.addMarker(
                MarkerOptions().position(latLng).title(fleetType).icon(
                    bitmapDescriptorFromVector(
                        activity, if (isTypeTaxi(fleetType!!)) R.drawable.taxi else R.drawable.pooling_taxi
                    )
                ).rotation(getRotationAngle(heading))
            )
        } else {
            mMap.addMarker(MarkerOptions().position(latLng).title(title))
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f))
    }

    private fun getRotationAngle(heading: Double): Float {
        return heading.toBigDecimal().setScale(1,
            RoundingMode.UP).toDouble().toFloat()
    }

    private fun isTypeTaxi(fleetType: String): Boolean {
        return fleetType.equals("Taxi", ignoreCase = true)
    }

    private fun showToast() {
        Snackbar.make(activity?.window?.decorView?.rootView!!,
            context!!.getString(R.string.error_in_request),
            Snackbar.LENGTH_SHORT).show()
    }

    private fun bitmapDescriptorFromVector(context: FragmentActivity?, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context!!, vectorResId)
        Objects.requireNonNull<Drawable>(vectorDrawable)
            .setBounds(0, 0, vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun getSortTextType(type: String, position: Int) {
        if (poiListResponse != null && poiListResponse?.poiList != null &&
            poiListResponse?.poiList!!.size > 0) {
            refreshSortingList(type, position)
            sort_type_text.text = type
        }
        selectTaxiFragment.dismiss()
    }

    private fun refreshSortingList(type: String?, position: Int) {
        for (i in sortingTypeList.indices) {
            sortingTypeList[i].isSelected =
                sortingTypeList[i].type.equals(type, ignoreCase = true) && sortingTypeList[i].position == position
        }
        mMap.clear()
        for (i in 0 until poiListResponse?.poiList!!.size) {
            if (type.equals("All", ignoreCase = true)) {
                setMapLatLongForTaxi(
                    poiListResponse?.poiList!![i].coordinate.latitude,
                    poiListResponse?.poiList!![i].coordinate.longitude,
                    poiListResponse?.poiList!![i].fleetType,
                    poiListResponse?.poiList!![i].heading,
                    true, null
                )
                continue
            }
            if (poiListResponse?.poiList!![i].fleetType.equals(type, ignoreCase = true)) {
                setMapLatLongForTaxi(
                    poiListResponse?.poiList!![i].coordinate.latitude,
                    poiListResponse?.poiList!![i].coordinate.longitude,
                    poiListResponse?.poiList!![i].fleetType,
                    poiListResponse?.poiList!![i].heading,
                    true, null
                )
            }
        }
        setMapLatLongForTaxi(p1Lat, p1Long, null, 0.0,false, "Point P1")
        setMapLatLongForTaxi(p2Lat, p2Long, null, 0.0, false, "Point P2")
    }

    override fun onCameraMoveStarted(p0: Int) {
        if (GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE == p0) {
            isDragged = true
        }
    }

    override fun onCameraIdle() {
        if (isDragged && makeAPICallOnEveryMapMove && isNetConnected) {
            mMap.clear()
            p1Lat = p2Lat
            p1Long = p2Long
            p2Lat = mMap.cameraPosition.target.latitude
            p2Long = mMap.cameraPosition.target.longitude
            fetchGetNearByTaxiResponse()
            isDragged = false
        } else if (isDragged && !isNetConnected) {
            Snackbar.make(activity?.window?.decorView?.rootView!!,
                context!!.getString(R.string.check_your_internet),
                Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 14f))
        return false
    }


    override fun onCloseClick() {
        selectTaxiFragment.dismiss()
    }

    override fun onSimpleSwitchSelect(status: Boolean) {
        makeAPICallOnEveryMapMove = status
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        isNetConnected = isConnected
    }

    private fun makeApiRequest(status: Boolean) {
        if (status) {
            sort_text.text = context?.getString(R.string.finding_near_taxi)
            sort_type_text.visibility = View.GONE
            sort_type_arrow.visibility = View.GONE
            fetch_taxi_progress_bar.visibility = View.VISIBLE
        } else {
            sort_text.text = context?.getString(R.string.show)
            sort_type_text.visibility = View.VISIBLE
            sort_type_arrow.visibility = View.VISIBLE
            fetch_taxi_progress_bar.visibility = View.GONE
        }
    }
}