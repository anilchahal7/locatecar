package `in`.iceberg.freenowtaxi.adapters

import `in`.iceberg.freenowtaxi.R
import `in`.iceberg.freenowtaxi.interfaces.OnSortTextClick
import `in`.iceberg.freenowtaxi.model.SortingType
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.taxi_sort_item.view.*

class SortingAdapter(private val items: List<SortingType>,
                     private val context: Context,
                     private val onSortTextClick: OnSortTextClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortingTextViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.taxi_sort_item, parent, false)
        return SortingTextViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SortingTextViewHolder) {
            holder.sortByText.text = items[position].type
            if (items[position].isSelected) {
                holder.itemSortContainer.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.black_08)
                )
                holder.selectionBox.visibility = View.VISIBLE
            } else {
                holder.itemSortContainer.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.white)
                )
                holder.selectionBox.visibility = View.GONE
            }
            holder.itemSortContainer.setOnClickListener {
                onSortTextClick.onSortTextClick(
                    items[position].type!!,
                    items[position].position
                )
            }
        }
    }

    class SortingTextViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val sortByText: TextView = view.sort_by_text
        val selectionBox: ImageView = view.selection_box
        val itemSortContainer: RelativeLayout = view.item_sort_container
    }
}