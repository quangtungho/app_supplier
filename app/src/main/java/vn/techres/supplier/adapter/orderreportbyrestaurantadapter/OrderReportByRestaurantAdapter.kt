package vn.techres.supplier.adapter.orderreportbyrestaurantadapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemOrderReportByRestaurantBinding
import vn.techres.supplier.model.response.OrderReportByRestaurantResponse
import java.text.DecimalFormat


class OrderReportByRestaurantAdapter :
    RecyclerView.Adapter<OrderReportByRestaurantAdapter.ViewHolder>() {
    private var dataSource = OrderReportByRestaurantResponse()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: OrderReportByRestaurantResponse) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order_report_by_restaurant, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemOrderReportByRestaurantBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderByRestaurantReport = dataSource.data.report[position]
        with(holder) {
            binding.name.text = orderByRestaurantReport.name
            binding.name.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.name.marqueeRepeatLimit = -1
            binding.name.isSelected = true

            binding.phone.text = orderByRestaurantReport.phone

            binding.address.text = orderByRestaurantReport.address
            binding.address.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.address.marqueeRepeatLimit = -1
            binding.address.isSelected = true

            binding.email.text = orderByRestaurantReport.email

            binding.totalOrder.text = orderByRestaurantReport.total_order.toString()

            binding.totalAmount.text =
                currencyFormat(orderByRestaurantReport.total_amount.toString())
            binding.totalVat.text = currencyFormat(orderByRestaurantReport.total_vat.toString())
            binding.totalDiscount.text =
                currencyFormat(orderByRestaurantReport.total_discount.toString())
        }
    }

    override fun getItemCount() = dataSource.data.report.size
}
