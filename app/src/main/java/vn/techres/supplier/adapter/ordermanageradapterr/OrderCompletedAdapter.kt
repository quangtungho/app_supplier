package vn.techres.supplier.adapter.ordermanageradapterr

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
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.OnClickBillOrder
import vn.techres.supplier.model.datamodel.DataListOrder
import java.text.DecimalFormat

class OrderCompletedAdapter(var context: Context) :
    RecyclerView.Adapter<OrderCompletedAdapter.ViewHolder>() {
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


            if (orderOrder.supplier_employee_delivering_avatar == "") {
                binding.imgChuKy.visibility = View.GONE
            } else {
                binding.imgChuKy.visibility = View.VISIBLE
                AppUtils.callPhotoAvatar(
                    orderOrder.supplier_employee_delivering_avatar,
                    binding.imgChuKy
                )

            }


            binding.txtNumberItems.text =
                orderOrder.total_material.toString()
//            binding.txtNumberItems.ellipsize = TextUtils.TruncateAt.MARQUEE
//            binding.txtNumberItems.marqueeRepeatLimit = -1
//            binding.txtNumberItems.isSelected = true



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


            binding.txtDiscount.text = currencyFormat(orderOrder.discount_amount.toString())
            binding.txtDiscount.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtDiscount.marqueeRepeatLimit = -1
            binding.txtDiscount.isSelected = true

            binding.txtVAT.text = currencyFormat(orderOrder.vat_amount.toString())
            binding.txtVAT.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtVAT.marqueeRepeatLimit = -1
            binding.txtVAT.isSelected = true


            binding.txtItems.text = AppUtils.formatMaterial(
                orderOrder.supplier_order_detail,
                orderOrder.total_material!!
            )
            binding.txtItems.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtItems.marqueeRepeatLimit = -1
            binding.txtItems.isSelected = true


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
                    orderOrder.id!!,
                    orderOrder.restaurant_avatar.toString()
                )
            }
        }
    }

    override fun getItemCount(): Int = orderList.size

}
