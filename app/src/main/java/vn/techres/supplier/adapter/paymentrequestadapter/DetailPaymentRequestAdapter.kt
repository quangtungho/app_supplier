package vn.techres.supplier.adapter.paymentrequestadapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication.Companion.context
import vn.techres.supplier.databinding.ItemDetailPaymentRequestBinding
import vn.techres.supplier.databinding.ItemRequestPaymentBinding
import vn.techres.supplier.model.datamodel.DataListOrder
import vn.techres.supplier.model.datamodel.DataListOrderBill
import vn.techres.supplier.model.datamodel.DetailPayment
import vn.techres.supplier.model.datamodel.PaymentRequest
import java.text.DecimalFormat

class DetailPaymentRequestAdapter :
    RecyclerView.Adapter<DetailPaymentRequestAdapter.ViewHolder>() {
    private var dataList = ArrayList<DataListOrder>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<DataListOrder>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context)
                .inflate(R.layout.item_detail_payment_request, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemDetailPaymentRequestBinding.bind(view)
    }

    private fun currencyFormatVND(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        with(holder) {
            binding.code.text = currentItem.code
            binding.restaurantDebtAmount.text =
                currencyFormatVND(currentItem.restaurant_debt_amount.toString())

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}