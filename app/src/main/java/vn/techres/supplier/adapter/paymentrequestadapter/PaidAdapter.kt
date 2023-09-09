package vn.techres.supplier.adapter.paymentrequestadapter

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication.Companion.context
import vn.techres.supplier.databinding.ItemRequestPaymentBinding
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.ClickIdDebt
import vn.techres.supplier.interfaces.DetailDebt
import vn.techres.supplier.model.datamodel.PaymentRequest
import java.text.DecimalFormat

class PaidAdapter :
    RecyclerView.Adapter<PaidAdapter.ViewHolder>() {
    private var dataList = ArrayList<PaymentRequest>()
    private var clickDetailPayment: ClickIdDebt? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<PaymentRequest>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun setClickDetailPayment(clickDetailPayment: ClickIdDebt) {
        this.clickDetailPayment = clickDetailPayment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_request_payment, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRequestPaymentBinding.bind(view)
    }

    private fun currencyFormatVND(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        with(holder) {
            binding.txtDateCreate.text = currentItem.created_at
            binding.txtNameRestaurant.text = currentItem.restaurant_name
            binding.txtNameRestaurant.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameRestaurant.marqueeRepeatLimit = -1
            binding.txtNameRestaurant.isSelected = true
            binding.txtBranchName.text = currentItem.branch_name
            binding.txtNumberOrder.text = currentItem.supplier_order_ids.size.toString()
            binding.txtAmount.text = currencyFormatVND(currentItem.total_amount.toString())
            binding.txtStatus.text = " Đã thanh toán"
            binding.lnViewOrder.setBackgroundResource(R.drawable.bg_approved_start_view_module)
            binding.lnClickDetail.setOnClickListener {
                clickDetailPayment!!.clickID(currentItem.id, currentItem.status)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}