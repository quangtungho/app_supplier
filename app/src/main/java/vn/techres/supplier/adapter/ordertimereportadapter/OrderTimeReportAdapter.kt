package vn.techres.supplier.adapter.ordertimereportadapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemOrderTimeReportBinding
import vn.techres.supplier.model.response.OrderTimeReportResponse
import java.text.DecimalFormat


class OrderTimeReportAdapter :
    RecyclerView.Adapter<OrderTimeReportAdapter.ViewHolder>() {
    private var dataSourceOrderTimeReport = OrderTimeReportResponse()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSourceOrderTimeReport: OrderTimeReportResponse) {
        this.dataSourceOrderTimeReport = dataSourceOrderTimeReport
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order_time_report, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemOrderTimeReportBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderTimeReport = dataSourceOrderTimeReport.data[position]
        with(holder) {
            binding.nameRestaurant.text = orderTimeReport.name.toString()
            binding.nameRestaurant.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.nameRestaurant.marqueeRepeatLimit = -1
            binding.nameRestaurant.isSelected = true

            binding.totalOrder.text = orderTimeReport.total_order.toString()
            binding.totalDeliveryCompleted.text =
                orderTimeReport.total_delivery_completed.toString()
            binding.totalUnfinishedDelivery.text =
                orderTimeReport.total_unfinished_delivery.toString()
            binding.totalAmount.text =
                currencyFormat(orderTimeReport.total_amount.toString())
        }
    }

    override fun getItemCount() = dataSourceOrderTimeReport.data.size
}
