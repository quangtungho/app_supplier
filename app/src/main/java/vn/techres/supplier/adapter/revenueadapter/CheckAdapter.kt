package vn.techres.supplier.adapter.revenueadapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemReceiptsBinding
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.datamodel.DataListReceipts
import java.text.DecimalFormat

class CheckAdapter(var context: Context) :
    RecyclerView.Adapter<CheckAdapter.ViewHolder>() {
    private var receiptsList = DataListReceipts()
    private var onClickView: OnClickTransID? = null

    fun setClickView(onClickView: OnClickTransID) {
        this.onClickView = onClickView
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListReceipts(dataListReceipts: DataListReceipts) {
        this.receiptsList = dataListReceipts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_receipts, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemReceiptsBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataList = receiptsList.list[position]
        with(holder) {
            binding.code.text = dataList.code
            binding.feeMonth.text = dataList.fee_month
            binding.nameCreate.text = dataList.supplier_employee_create_full_name
            binding.amount.text = currencyFormat(dataList.amount.toString())
            binding.supplierAdditionFeeReasonName.text = dataList.supplier_addition_fee_reason_name
            when (dataList.object_type) {
                0 -> binding.objectType.text = "Khác"
                1 -> binding.objectType.text = "Đơn hàng"
                2 -> binding.objectType.text = "Phiếu nhập kho"
            }
            binding.note.text = dataList.note
            when (dataList.status) {
                0 -> {
                    binding.status.text = "Chờ xác nhận"
                    binding.status.setTextColor(R.color.main_bg)
                }
                1 -> {
                    binding.status.text = "Đã xác nhận"
                    binding.status.setTextColor(Color.parseColor("#0072CB"))
                    binding.viewStatus.setBackgroundResource(R.drawable.bg_order_delivering)
                    binding.linearLayoutView.setBackgroundResource(R.drawable.bg_click_delivering)
                }

                2 -> {
                    binding.status.text = "Đã hoàn tất"
                    binding.status.setTextColor(Color.parseColor("#49c94f"))
                    binding.viewStatus.setBackgroundResource(R.drawable.bg_order_completed)
                    binding.linearLayoutView.setBackgroundResource(R.drawable.bg_click_completed)
                }
                3 -> {
                    binding.status.text = "Đã hủy"
                    binding.status.setTextColor(Color.parseColor("#FF0000"))
                    binding.viewStatus.setBackgroundResource(R.drawable.bg_order_cancel)
                    binding.linearLayoutView.setBackgroundResource(R.drawable.bg_click_cancel)
                }
            }
            binding.onClick.setOnClickListener {
                onClickView!!.onClick(position, dataList.id)
            }

        }
    }

    override fun getItemCount(): Int = receiptsList.list.size
}
