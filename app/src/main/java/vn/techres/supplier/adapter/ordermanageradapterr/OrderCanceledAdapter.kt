package vn.techres.supplier.adapter.ordermanageradapterr

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableOrderCanceledBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.OnClickBillOrder
import vn.techres.supplier.model.datamodel.DataListOrder
import java.text.DecimalFormat

class OrderCanceledAdapter(var context: Context) :
    RecyclerView.Adapter<OrderCanceledAdapter.ViewHolder>() {
    private var orderList = ArrayList<DataListOrder>()
    private var total = 0
    private var onClickView: OnClickBillOrder? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListOrder(orderList: ArrayList<DataListOrder>, total: Int) {
        this.orderList = orderList
        this.total = total
        notifyDataSetChanged()
    }

    fun setClickView(onClickView: OnClickBillOrder) {
        this.onClickView = onClickView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_table_order_canceled, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableOrderCanceledBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderOrder = orderList[position]
        with(holder) {
            if (orderOrder.employee_complete_full_name == "" && orderOrder.supplier_employee_delivering_name == null && orderOrder.employee_complete_full_name == null && orderOrder.supplier_employee_delivering_name == "") {
                binding.receiver.text = "..."
                binding.deliver.text = "..."
            } else {
                binding.receiver.text = orderOrder.employee_complete_full_name
                binding.receiver.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.receiver.marqueeRepeatLimit = -1
                binding.receiver.isSelected = true

                binding.deliver.text = orderOrder.supplier_employee_delivering_name
                binding.deliver.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.deliver.marqueeRepeatLimit = -1
                binding.deliver.isSelected = true
            }

            binding.code.text = orderOrder.code
            binding.code.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.code.marqueeRepeatLimit = -1
            binding.code.isSelected = true

            binding.txtNumberItems.text =
                orderOrder.supplier_order_detail.size.toString()
            binding.txtNumberItems.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNumberItems.marqueeRepeatLimit = -1
            binding.txtNumberItems.isSelected = true

            binding.txtNameSupplier.text = orderOrder.restaurant_name
            binding.txtNameSupplier.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameSupplier.marqueeRepeatLimit = -1
            binding.txtNameSupplier.isSelected = true

            binding.txtMoney.text = currencyFormat(orderOrder.total_amount_reality.toString())
            binding.txtMoney.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtMoney.marqueeRepeatLimit = -1
            binding.txtMoney.isSelected = true

            binding.txtDateCreate.text = orderOrder.created_at
            binding.txtDateCreate.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtDateCreate.marqueeRepeatLimit = -1
            binding.txtDateCreate.isSelected = true

            binding.txtBranchName.text = orderOrder.branch_name
            binding.txtBranchName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtBranchName.marqueeRepeatLimit = -1
            binding.txtBranchName.isSelected = true

            binding.txtEstimatedDeliveryDate.text = orderOrder.received_at
            binding.txtEstimatedDeliveryDate.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtEstimatedDeliveryDate.marqueeRepeatLimit = -1
            binding.txtEstimatedDeliveryDate.isSelected = true

            binding.txtItems.text = AppUtils.formatMaterial(
                orderOrder.supplier_order_detail,
                orderOrder.total_material!!
            )
            binding.txtItems.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtItems.marqueeRepeatLimit = -1
            binding.txtItems.isSelected = true

            binding.txtDiscount.text = currencyFormat(orderOrder.discount_amount.toString())
            binding.txtDiscount.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtDiscount.marqueeRepeatLimit = -1
            binding.txtDiscount.isSelected = true

            binding.txtVAT.text = currencyFormat(orderOrder.vat_amount.toString())
            binding.txtVAT.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtVAT.marqueeRepeatLimit = -1
            binding.txtVAT.isSelected = true

            binding.statusCanceled.visibility = View.VISIBLE
            binding.statusCanceled.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.statusCanceled.marqueeRepeatLimit = -1
            binding.statusCanceled.isSelected = true

            binding.linearLayoutView.setOnClickListener {
                onClickView!!.onClickViewCompleted(
                    position,
                    orderOrder.id!!,
                    orderOrder.restaurant_avatar.toString()
                )
            }
        }
    }

    override fun getItemCount(): Int = orderList.size

}
