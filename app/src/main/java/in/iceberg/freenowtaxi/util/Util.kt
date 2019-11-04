package `in`.iceberg.freenowtaxi.util

import android.content.Context
import android.util.TypedValue

import `in`.iceberg.freenowtaxi.model.SortingType

object Util {
    val sortingTypeList: MutableList<SortingType>
        get() {
            val sortingTypeList: MutableList<SortingType> = mutableListOf()
            val sortingTypeAll = SortingType()
            sortingTypeAll.type = "All"
            sortingTypeAll.isSelected = true
            sortingTypeAll.position = 0
            sortingTypeList.add(sortingTypeAll)
            val sortingTypeTaxi = SortingType()
            sortingTypeTaxi.type = "Taxi"
            sortingTypeTaxi.isSelected = false
            sortingTypeTaxi.position = 1
            sortingTypeList.add(sortingTypeTaxi)
            val sortingTypePool = SortingType()
            sortingTypePool.type = "Pooling"
            sortingTypePool.isSelected = false
            sortingTypePool.position = 2
            sortingTypeList.add(sortingTypePool)
            return sortingTypeList
        }

    fun convertDpToPixel(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 306f, metrics).toInt()
    }
}
