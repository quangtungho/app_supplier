package vn.techres.supplier.adapter.categoryreportadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemReportCategoryBinding
import vn.techres.supplier.model.datamodel.DataCategoryReport
import java.text.DecimalFormat


class CategoryReportAdapter :
    RecyclerView.Adapter<CategoryReportAdapter.ViewHolder>() {
    private var dataSource = ArrayList<DataCategoryReport>()


    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<DataCategoryReport>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_report_category, parent, false)
        return ViewHolder(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemReportCategoryBinding.bind(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryReport = dataSource[position]
        with(holder) {
            binding.categoryName.text = categoryReport.name
            binding.totalImport.text = currencyFormat(categoryReport.total_import.toString())
            binding.totalExport.text = currencyFormat(categoryReport.total_export.toString())
            binding.quantityImport.text = categoryReport.quantity_import.toString()
            binding.quantityExport.text = categoryReport.quantity_export.toString()
            binding.totalRemaining.text =
                currencyFormat(categoryReport.total_remaining.toString())
            binding.quantityRemaining.text = categoryReport.quantity_remaining.toString()
        }

    }

    override fun getItemCount() = dataSource.size
}
