package `in`.iceberg.freenowtaxi.adapters

import `in`.iceberg.domain.model.PoiData
import `in`.iceberg.freenowtaxi.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.taxi_list_item.view.*
import javax.inject.Inject

class TaxiListAdapter @Inject constructor() : RecyclerView.Adapter<TaxiListAdapter.ViewHolder>() {

    var poiDataList: MutableList<PoiData> = mutableListOf()
    lateinit var context: Context
    private val fleetType: String = "Taxi"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.taxi_list_item,
            parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return poiDataList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = poiDataList[position]
        holder.bind(data)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer  {
        fun bind(data: PoiData) {
            containerView.taxi_id.text = data.id.toString()
            if (data.fleetType.equals(fleetType, ignoreCase = true)) {
                containerView.taxi_list_item_layout.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.black_08))
            } else {
                containerView.taxi_list_item_layout.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.white))
            }
            containerView.taxi_fleet_type.text = data.fleetType
            containerView.longitude.text = context.getString(R.string.long_text, data.coordinate.longitude.toString())
            containerView.latitude.text = context.getString(R.string.lat_text, data.coordinate.latitude.toString())
            containerView.heading.text = data.heading.toString()
        }
    }
}