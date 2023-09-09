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
import vn.techres.supplier.interfaces.OnClickBillOrder
import vn.techres.supplier.model.datamodel.DataReturnsOrder
import java.text.DecimalFormat

class OrderReturnAdapter(var context: Context) :
    RecyclerView.Adapter<OrderReturnAdapter.ViewHolder>() {
    private var orderList = ArrayList<DataReturnsOrder>()
    private var onClickView: OnClickBillOrder? = null

    fun setClickView(onClickView: OnClickBillOrder) {
        this.onClickView = onClickView
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setDataListOrder(orderList: ArrayList<DataReturnsOrder>) {
        this.orderList = orderList
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

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderOrder = orderList[position]
        with(holder) {
            binding.deliver.text = "Người tạo phiếu"
            if (orderOrder.supplier_employee_confirm_full_name == "" && orderOrder.supplier_employee_confirm_full_name == null && orderOrder.employee_created_full_name == "" && orderOrder.employee_created_full_name == null) {
                binding.txtDeliver.text = "..."
                binding.txtReceiver.text = "..."
            } else {
                binding.txtDeliver.text = orderOrder.employee_created_full_name
                binding.txtReceiver.text = orderOrder.supplier_employee_confirm_full_name
            }
            binding.date.text = orderOrder.created_at
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

            binding.total.text = currencyFormat(orderOrder.total_amount.toString())
            binding.total.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.total.marqueeRepeatLimit = -1
            binding.total.isSelected = true

            binding.material.text = orderOrder.total_material.toString()
            binding.material.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.material.marqueeRepeatLimit = -1
            binding.material.isSelected = true

            if (orderOrder.status == 0) {
                binding.viewStatus.setBackgroundResource(R.drawable.bg_order_return)
                binding.linearLayoutView.setBackgroundResource(R.drawable.bg_click_return)
                binding.statusReturn.text = context.getString(R.string.confirm_return_order)
                binding.statusReturn.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.statusReturn.marqueeRepeatLimit = -1
                binding.statusReturn.isSelected = true
                binding.statusReturn.visibility = View.VISIBLE
                binding.statusReturnDone.visibility = View.GONE
            } else {
                binding.viewStatus.setBackgroundResource(R.drawable.bg_order_return_done)
                binding.linearLayoutView.setBackgroundResource(R.drawable.bg_click_return_done)
                binding.statusReturnDone.text = context.getString(R.string.status_completed)
                binding.statusReturnDone.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.statusReturnDone.marqueeRepeatLimit = -1
                binding.statusReturnDone.isSelected = true
                binding.statusReturnDone.visibility = View.VISIBLE
                binding.statusReturn.visibility = View.GONE
            }

            binding.linearLayoutView.setOnClickListener {
                onClickView!!.onClickViewReturn(
                    position,
                    orderOrder.id,
                    orderOrder.restaurant_name
                )
            }
        }
    }

    override fun getItemCount(): Int = orderList.size

}
