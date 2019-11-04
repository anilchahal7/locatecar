package `in`.iceberg.freenowtaxi.fragments

import `in`.iceberg.freenowtaxi.R
import `in`.iceberg.freenowtaxi.adapters.SortingAdapter
import `in`.iceberg.freenowtaxi.interfaces.GetSortingType
import `in`.iceberg.freenowtaxi.interfaces.OnCloseClick
import `in`.iceberg.freenowtaxi.interfaces.OnSortTextClick
import `in`.iceberg.freenowtaxi.interfaces.OnSwitchSelect
import `in`.iceberg.freenowtaxi.model.SortingType
import `in`.iceberg.freenowtaxi.util.Util
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.select_taxi_fragment.view.*

class SelectTaxiFragment(listener: GetSortingType,
                         closeClick: OnCloseClick,
                         switchClick: OnSwitchSelect,
                         switchStatus: Boolean,
                         list:MutableList<SortingType>): BottomSheetDialogFragment(), OnSortTextClick {

    private var getSortingType: GetSortingType = listener
    private var onCloseClick: OnCloseClick = closeClick
    private var onSwitchSelect: OnSwitchSelect = switchClick
    private var currentSwitchStatus: Boolean = switchStatus
    private val sortingTypeList: MutableList<SortingType> = list
    private lateinit var sortTextAdapter: SortingAdapter

    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var plpSortBottomSheetDialog: BottomSheetDialog? = null

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        try {
            bottomSheetDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            bottomSheetDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

            bottomSheetDialog.setOnShowListener { dialog ->
                plpSortBottomSheetDialog = dialog as BottomSheetDialog
                val bottomSheet = plpSortBottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                if (bottomSheet != null) {
                    bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                }
                bottomSheetBehavior?.setBottomSheetCallback(bottomSheetCallback)
                if (context != null && bottomSheet != null) {
                    bottomSheetBehavior?.peekHeight = Util.convertDpToPixel(activity as Context)
                    bottomSheet.setBackgroundColor(Color.parseColor("#00000000"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bottomSheetDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.select_taxi_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
    }

    private fun initUI(view: View) {
        sortTextAdapter = SortingAdapter(sortingTypeList, context!!, this)
        view.sort_by_recycler_view.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        view.sort_by_recycler_view.setHasFixedSize(false)
        view.sort_by_recycler_view.adapter = sortTextAdapter
        view.close_text.setOnClickListener {
            onCloseClick.onCloseClick()
        }
        view.simple_switch.isChecked = currentSwitchStatus
        view.simple_switch.setOnCheckedChangeListener { _, checked ->
            view.simple_switch.isChecked = checked
            onSwitchSelect.onSimpleSwitchSelect(checked)
            view.simple_switch.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
        }
    }

    override fun onSortTextClick(type: String, position: Int) {
        getSortingType.getSortTextType(type, position)
    }
}