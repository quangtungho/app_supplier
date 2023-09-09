package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemChatOrderBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.OnClickGroupOrder
import vn.techres.supplier.model.datamodel.DataListOrder
import java.text.DecimalFormat

class GroupOrderAdapter(var context: Context) :
    RecyclerView.Adapter<GroupOrderAdapter.ViewHolder>() {
    private var orderList = ArrayList<DataListOrder>()
    private var onClickGroupOrder: OnClickGroupOrder? = null
    private var onDetailOrder: OnClickGroupOrder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_order, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<DataListOrder>) {
        this.orderList = dataSource
        notifyDataSetChanged()
    }

    fun swap(data: DataListOrder?, position: Int) {
        orderList.removeAt(position)
        if (data != null) {
            orderList.add(0, data)
        }
        notifyItemRemoved(position)
        notifyItemInserted(0)
    }

    @JvmName("setRoleClick1")
    fun setGroupOrderClick(onClickGroupOrder: OnClickGroupOrder) {
        this.onClickGroupOrder = onClickGroupOrder
    }

    fun setDetailOrderClick(onDetailOrder: OnClickGroupOrder) {
        this.onDetailOrder = onDetailOrder
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemChatOrderBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = orderList[position]
// Mặt hàng
            binding.txtNameMaterial.text = "Mặt hàng:" + " " + AppUtils.formatMaterial(
                data.supplier_order_detail,
                data.total_material!!
            )
            binding.txtNameMaterial.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameMaterial.marqueeRepeatLimit = -1
            binding.txtNameMaterial.isSelected = true
// thơi gian
            binding.time.text = data.created_at

            // Mã đơn hàng + tong tien
            binding.txtName.text = "Mã ĐH:" + " " + String.format(
                "%s - %s",
                data.code,
                currencyFormat(data.total_amount.toString())
            ) + " VND"
            binding.txtName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtName.marqueeRepeatLimit = -1
            binding.txtName.isSelected = true
            when (data.status) {
                1 -> binding.txtStatus.text =
                    "Trạng thái:" + " " + context.getText(R.string.txt_status_wait)
                3 -> {
                    binding.txtStatus.text =
                        "Trạng thái:" + " " + context.getText(R.string.delivery)
                }
                4, 7 -> {
                    binding.txtStatus.text =
                        "Trạng thái:" + " " + context.getText(R.string.completed)
                }
                5 -> {
                    binding.txtStatus.text =
                        "Trạng thái:" + " " + context.getText(R.string.canceled)
                }
                6 -> {
                    binding.txtStatus.text =
                        "Trạng thái:" + " " + context.getText(R.string.confirm_return_order)
                }

            }
            binding.txtStatus.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtStatus.marqueeRepeatLimit = -1
            binding.txtStatus.isSelected = true

            binding.onClickGroupOrder.setOnClickListener {
                onClickGroupOrder!!.onClick(position, data)
            }
            binding.lnDetail.setOnClickListener {
                onDetailOrder!!.onDetailOrder(
                    position,
                    data.id!!,
                    data.restaurant_avatar.toString()
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}