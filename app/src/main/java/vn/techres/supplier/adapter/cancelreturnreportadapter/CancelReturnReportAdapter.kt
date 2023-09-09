package vn.techres.supplier.adapter.cancelreturnreportadapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemOrderCancelReturnBinding
import vn.techres.supplier.model.datamodel.DataReportCancelReturn
import java.text.DecimalFormat


class CancelReturnReportAdapter :
    RecyclerView.Adapter<CancelReturnReportAdapter.ViewHolder>() {
    private var dataSource = ArrayList<DataReportCancelReturn>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<DataReportCancelReturn>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order_cancel_return, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemOrderCancelReturnBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    private fun formatFloatToString(f: Float): String {
        val i = f.toInt()
        return if (f == i.toFloat()) i.toString() else f.toString()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cancelReturnReport = dataSource[position]
        with(holder) {
            binding.materialCategoryName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.materialCategoryName.marqueeRepeatLimit = -1
            binding.materialCategoryName.isSelected = true
            binding.materialCategoryName.text = cancelReturnReport.material_category_name

            binding.supplierMaterialName.text = cancelReturnReport.supplier_material_name
            binding.quantity.text = formatFloatToString(cancelReturnReport.quantity)

            binding.totalAmount.text =
                currencyFormat(cancelReturnReport.total_amount.toString())
        }
    }

    override fun getItemCount() = dataSource.size
}
