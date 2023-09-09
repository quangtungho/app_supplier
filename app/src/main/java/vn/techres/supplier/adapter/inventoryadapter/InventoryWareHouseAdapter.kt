package vn.techres.supplier.adapter.inventoryadapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemListWarehouseBinding
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.WareHouse
import java.text.DecimalFormat

class InventoryWareHouseAdapter(var context: Context) :
    RecyclerView.Adapter<InventoryWareHouseAdapter.ViewHolder>() {
    private var dataList = ArrayList<WareHouse>()
    private var clickDetail: OnClickTransID? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<WareHouse>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun setClickDetail(clickDetail: OnClickTransID) {
        this.clickDetail = clickDetail

    }

    private fun currencyFormatVND(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    private fun currencyFormatPercent(amount: String): String? {
        val formatter = DecimalFormat("")
        return formatter.format(amount.toDouble())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_list_warehouse, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemListWarehouseBinding.bind(view)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        with(holder) {
            binding.txtCode.text = currentItem.code
            binding.txtMoney.text =
                currencyFormatVND(currentItem.total_amount.toString()) + " " + context.getString(R.string.txt_vnd)
            binding.txtDateCreate.text = currentItem.created_at
            binding.txtNameCreate.text = currentItem.supplier_employee_name
            binding.txtDateDelivery.text = currentItem.delivery_date
            binding.txtNote.text = currentItem.note
            binding.txtNote.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNote.marqueeRepeatLimit = -1
            binding.txtNote.isSelected = true
            binding.txtDiscount.text =
                currencyFormatPercent(currentItem.discount_percent.toString()) + " %"
            binding.txtVAT.text = currencyFormatPercent(currentItem.vat.toString()) + " %"

            when (currentItem.payment_status) {
                0 -> {
                    binding.txtTotalStatus.text = "Chờ thanh toán"
                    binding.txtTotalStatus.setTextColor(Color.parseColor("#0072BC"))
                }
                1 -> {
                    binding.txtTotalStatus.text = "Chờ xác nhận thanh toán"
                    binding.txtTotalStatus.setTextColor(Color.parseColor("#FFA233"))
                }
                2 -> {
                    binding.txtTotalStatus.text = "Đã thanh toán"
                    binding.txtTotalStatus.setTextColor(Color.parseColor("#35AC02"))
                }
                4 -> {
                    binding.txtTotalStatus.text = "Đã hủy"
                    binding.txtTotalStatus.setTextColor(Color.parseColor("#FF0000"))
                }
            }
            when (currentItem.status) {
                0 -> {
                    binding.txtStatus.text = "Đang xử lý"
                    binding.txtStatus.setTextColor(Color.parseColor("#FFA233"))
                    binding.zigzag.zigzagBackgroundColor = context.getColor(R.color.blue)
                }
                1, 2 -> {
                    binding.txtStatus.text = "Đã hoàn tất"
                    binding.txtStatus.setTextColor(Color.parseColor("#35AC02"))
                    binding.zigzag.zigzagBackgroundColor = context.getColor(R.color.txt_sp)
                }
                4 -> {
                    binding.txtStatus.text = "Đã hủy"
                    binding.txtStatus.setTextColor(Color.parseColor("#ff6d6d6d"))

                }
            }
            if (currentItem.type == 3){
                binding.txtStatus.text = "Trả hàng đã hủy"
                binding.zigzag.zigzagBackgroundColor = context.getColor(R.color.red)
            }

            binding.linearLayoutView.setOnClickListener {
                clickDetail!!.onClick(position, currentItem.id!!)
            }
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }
}
