package vn.techres.supplier.adapter.materialreportdapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemReportMaterialBinding
import vn.techres.supplier.model.response.MaterialsReportResponse
import java.text.DecimalFormat


class MaterialReportAdapter :
    RecyclerView.Adapter<MaterialReportAdapter.ViewHolder>() {
    private var dataSource = MaterialsReportResponse()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: MaterialsReportResponse) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_report_material, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemReportMaterialBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val materialReport = dataSource.data.list_material[position]
        with(holder) {
            binding.categoryName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.categoryName.marqueeRepeatLimit = -1
            binding.categoryName.isSelected = true
            binding.categoryName.text = materialReport.material_category_name

            binding.materialName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.materialName.marqueeRepeatLimit = -1
            binding.materialName.isSelected = true
            binding.materialName.text = materialReport.supplier_material_name
            binding.totalImport.text = currencyFormat(materialReport.total_import.toString())
            binding.totalExport.text = currencyFormat(materialReport.total_export.toString())
            binding.quantityImport.text = currencyFormat(materialReport.quantity_import.toString())
            binding.quantityExport.text = currencyFormat(materialReport.quantity_export.toString())
            binding.totalRemaining.text =
                currencyFormat(materialReport.total_remaining.toString())
            binding.quantityRemaining.text =
                currencyFormat(materialReport.quantity_remaining.toString())

        }
    }

    override fun getItemCount() = dataSource.data.list_material.size
}
