package vn.techres.supplier.adapter.orderreportadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemOrderReportBinding
import vn.techres.supplier.model.datamodel.DataOrderReport
import java.text.DecimalFormat


class OrderReportAdapter :
    RecyclerView.Adapter<OrderReportAdapter.ViewHolder>() {
    private var dataSource = DataOrderReport()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: DataOrderReport) {
        this.dataSource = dataSource

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_report, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemOrderReportBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderReport = dataSource.report[position]
        with(holder) {
            binding.totalOrder.text = orderReport.number_order.toString()
            binding.totalVat.text = currencyFormat(orderReport.vat_amount.toString())
            binding.totalDiscount.text = currencyFormat(orderReport.discount_amount.toString())
            binding.totalAmount.text =
                currencyFormat(orderReport.amount.toString())

            binding.time.text = String.format("%s - %s", orderReport.time, orderReport.date)
        }
    }

    override fun getItemCount() = dataSource.report.size
}
