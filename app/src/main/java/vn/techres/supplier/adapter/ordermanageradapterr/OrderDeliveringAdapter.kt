package vn.techres.supplier.adapter.ordermanageradapterr

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableBiiOrderBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.OnClickBillOrder
import vn.techres.supplier.model.datamodel.DataListOrder
import java.text.DecimalFormat

class OrderDeliveringAdapter(var context: Context) :
    RecyclerView.Adapter<OrderDeliveringAdapter.ViewHolder>() {
    private var orderList = ArrayList<DataListOrder>()
    private var total = 0
    private var onClickView: OnClickBillOrder? = null
    fun setClickView(onClickView: OnClickBillOrder) {
        this.onClickView = onClickView
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListOrder(orderList: ArrayList<DataListOrder>, total: Int) {
        this.orderList = orderList
        this.total = total
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_table_bii_order, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableBiiOrderBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderOrder = orderList[position]
        with(holder) {
            if (orderOrder.supplier_employee_delivering_name == "" && orderOrder.employee_complete_full_name == "") {
                binding.txtDeliver.text = "..."
                binding.txtReceiver.text = "..."
            } else {
                binding.txtDeliver.text = orderOrder.supplier_employee_delivering_name
                binding.txtReceiver.text = orderOrder.employee_complete_full_name
            }

            binding.date.text = orderOrder.delivery_at
            binding.date.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.date.marqueeRepeatLimit = -1
            binding.date.isSelected = true
            binding.txtDiscount.text = orderOrder.discount_percent.toString()
            binding.txtVat.text = orderOrder.vat.toString()
            binding.code.text = orderOrder.code
            binding.code.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.code.marqueeRepeatLimit = -1
            binding.code.isSelected = true
            binding.customer.text = orderOrder.restaurant_name
            binding.customer.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.customer.marqueeRepeatLimit = -1
            binding.customer.isSelected = true
            binding.branch.text = orderOrder.branch_name
            binding.branch.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.branch.marqueeRepeatLimit = -1
            binding.branch.isSelected = true
            binding.total.text = currencyFormat(orderOrder.total_amount_reality.toString())
            binding.total.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.total.marqueeRepeatLimit = -1
            binding.total.isSelected = true
            binding.material.text = orderOrder.total_material.toString()
            binding.material.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.material.marqueeRepeatLimit = -1
            binding.material.isSelected = true

            binding.txtItems.text = AppUtils.formatMaterial(
                orderOrder.supplier_order_detail,
                orderOrder.total_material!!
            )
            binding.txtItems.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtItems.marqueeRepeatLimit = -1
            binding.txtItems.isSelected = true


            binding.viewStatus.setBackgroundResource(R.drawable.bg_order_delivering)
            binding.linearLayoutView.setBackgroundResource(R.drawable.bg_click_delivering)
            binding.statusDelivering.setText(R.string.txt_status_delivering)
            binding.statusDelivering.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.statusDelivering.marqueeRepeatLimit = -1
            binding.statusDelivering.isSelected = true

            binding.statusDelivering.visibility = View.VISIBLE
            binding.linearLayoutView.setOnClickListener {
                onClickView!!.onClickViewDelivery(
                    position,
                    orderOrder.id!!,
                    orderOrder.restaurant_avatar.toString()
                )
            }
        }
    }

    override fun getItemCount(): Int = orderList.size

}
