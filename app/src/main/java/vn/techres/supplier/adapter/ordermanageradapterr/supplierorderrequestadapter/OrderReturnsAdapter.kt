package vn.techres.supplier.adapter.ordermanageradapterr.supplierorderrequestadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableOrderBinding
import vn.techres.supplier.interfaces.OnClickBillOrder
import vn.techres.supplier.model.datamodel.DataReturnsOrder
import java.text.DecimalFormat

class OrderReturnsAdapter(var context: Context) :
    RecyclerView.Adapter<OrderReturnsAdapter.ViewHolder>() {
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
            LayoutInflater.from(parent.context).inflate(R.layout.item_table_order, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableOrderBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderOrder = orderList[position]
        with(holder) {
            binding.code.text = orderOrder.code
            binding.code.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.code.marqueeRepeatLimit = -1
            binding.code.isSelected = true
            if (orderOrder.supplier_employee_confirm_full_name == "" && orderOrder.employee_created_full_name == "") {
                binding.receiver.text = "..."
                binding.deliver.text = "..."
            } else {
                binding.receiver.text = orderOrder.supplier_employee_confirm_full_name
                binding.receiver.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.receiver.marqueeRepeatLimit = -1
                binding.receiver.isSelected = true

                binding.deliver.text = orderOrder.employee_created_full_name
                binding.deliver.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.deliver.marqueeRepeatLimit = -1
                binding.deliver.isSelected = true
            }


            binding.txtNumberItems.text =
                orderOrder.total_material.toString()
            binding.txtNumberItems.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNumberItems.marqueeRepeatLimit = -1
            binding.txtNumberItems.isSelected = true



            binding.txtNameSupplier.text = orderOrder.restaurant_name
            binding.txtNameSupplier.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameSupplier.marqueeRepeatLimit = -1
            binding.txtNameSupplier.isSelected = true

            binding.txtMoney.text = currencyFormat(orderOrder.amount.toString())
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

            binding.txtEstimatedDeliveryDate.text = orderOrder.updated_at
            binding.txtEstimatedDeliveryDate.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtEstimatedDeliveryDate.marqueeRepeatLimit = -1
            binding.txtEstimatedDeliveryDate.isSelected = true


            binding.txtDiscount.text = orderOrder.discount_amount.toString()
            binding.txtDiscount.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtDiscount.marqueeRepeatLimit = -1
            binding.txtDiscount.isSelected = true

            binding.txtVAT.text = orderOrder.vat_amount.toString()
            binding.txtVAT.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtVAT.marqueeRepeatLimit = -1
            binding.txtVAT.isSelected = true


//            binding.txtItems.text = AppUtils.formatMaterial(
//                orderOrder.supplier_order_detail,
//                orderOrder.total_material!!
//            )
//            binding.txtItems.ellipsize = TextUtils.TruncateAt.MARQUEE
//            binding.txtItems.marqueeRepeatLimit = -1
//            binding.txtItems.isSelected = true


            if (orderOrder.status == 4) {
                binding.statusCompleted.visibility = View.VISIBLE
                binding.statusCompleted.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.statusCompleted.marqueeRepeatLimit = -1
                binding.statusCompleted.isSelected = true
            } else {
                binding.statusCompleted.visibility = View.VISIBLE
                binding.statustReturn.visibility = View.VISIBLE
                val styledText = " (<font color='red'>có trả hàng</font>)"
                binding.statustReturn.setText(
                    Html.fromHtml(styledText),
                    TextView.BufferType.SPANNABLE
                )
                binding.statusCompleted.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.statusCompleted.marqueeRepeatLimit = -1
                binding.statusCompleted.isSelected = true
            }

            binding.linearLayoutView.setOnClickListener {
                onClickView!!.onClickViewCompleted(
                    position,
                    orderOrder.id,
                    orderOrder.restaurant_name
                )
            }
        }
    }

    override fun getItemCount(): Int = orderList.size

}
