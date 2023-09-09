package vn.techres.supplier.adapter.ordermanageradapterr

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableBiiOrderBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.OnClickBillOrderRequest
import vn.techres.supplier.model.datamodel.DataListOrderBill
import java.text.DecimalFormat

class OrderBillAdapter(var context: Context) :
    RecyclerView.Adapter<OrderBillAdapter.ViewHolder>() {
    private var orderBillList = ArrayList<DataListOrderBill>()
    private var total = 0
    private var onClickView: OnClickBillOrderRequest? = null

    fun setClickView(onClickView: OnClickBillOrderRequest) {
        this.onClickView = onClickView
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListOrderBill(dataListOrderBill: ArrayList<DataListOrderBill>, total: Int) {
        this.orderBillList = dataListOrderBill
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

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderBillOrder = orderBillList[position]
        with(holder) {
            binding.contactOrder.visibility = View.GONE
            binding.viewOrder.visibility = View.GONE
            binding.rltCount.visibility = View.VISIBLE
            binding.idCount.text = (position + 1).toString()//stt

            binding.date.text = orderBillOrder.expected_delivery_time
            binding.date.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.date.marqueeRepeatLimit = -1
            binding.date.isSelected = true
            binding.code.visibility = View.GONE
            binding.linearCode.visibility = View.GONE
            binding.customer.text = orderBillOrder.restaurant_name
            binding.customer.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.customer.marqueeRepeatLimit = -1
            binding.customer.isSelected = true

            binding.branch.text = orderBillOrder.branch_name
            binding.branch.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.branch.marqueeRepeatLimit = -1
            binding.branch.isSelected = true

            binding.linearLayoutDiscount.visibility = View.GONE
            binding.linearLayoutVat.visibility = View.GONE
            binding.txtDiscount.visibility = View.GONE
            binding.txtVat.visibility = View.GONE
            binding.linearLayoutDeliver.visibility = View.GONE
            binding.linearLayoutReceiver.visibility = View.GONE
            binding.txtDeliver.visibility = View.GONE
            binding.txtReceiver.visibility = View.GONE


            binding.statusBill.visibility = View.VISIBLE
            if (orderBillOrder.status == 5) {

                binding.total.text =
                    currencyFormat(orderBillOrder.supplier_total_amount.toString())
                binding.total.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.total.marqueeRepeatLimit = -1
                binding.total.isSelected = true

                binding.material.text = orderBillOrder.supplier_quantity.toString()
                binding.material.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.material.marqueeRepeatLimit = -1
                binding.material.isSelected = true

                binding.txtItems.text =
                    AppUtils.formatMaterial(
                        orderBillOrder.supplier_order_request_detail,
                        orderBillOrder.supplier_quantity!!
                    )
                binding.txtItems.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.txtItems.marqueeRepeatLimit = -1
                binding.txtItems.isSelected = true

                binding.statusBill.setText(R.string.txt_status_order)
                binding.statusBill.setTextColor(Color.RED)
                binding.statusBill.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.statusBill.marqueeRepeatLimit = -1
                binding.statusBill.isSelected = true

            } else {

                binding.statusBill.setText(R.string.txt_status_cf)
                binding.statusBill.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.statusBill.marqueeRepeatLimit = -1
                binding.statusBill.isSelected = true

                binding.total.text = currencyFormat(orderBillOrder.total_amount.toString())
                binding.total.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.total.marqueeRepeatLimit = -1
                binding.total.isSelected = true

                binding.material.text = orderBillOrder.quantity.toString()
                binding.material.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.material.marqueeRepeatLimit = -1
                binding.material.isSelected = true

                binding.txtItems.text = AppUtils.formatMaterial(
                    orderBillOrder.supplier_order_request_detail,
                    orderBillOrder.quantity!!
                )
                binding.txtItems.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.txtItems.marqueeRepeatLimit = -1
                binding.txtItems.isSelected = true

            }

            binding.linearLayoutView.setBackgroundResource(R.drawable.bg_order_request_bill)
            binding.linearLayoutView.setBackgroundResource(R.drawable.bg_click_bill_oder)
            binding.linearLayoutView.setOnClickListener {
                onClickView!!.onClickViewOrderRequest(position, orderBillOrder.id!!)
            }
        }
    }

    override fun getItemCount(): Int = orderBillList.size
}
