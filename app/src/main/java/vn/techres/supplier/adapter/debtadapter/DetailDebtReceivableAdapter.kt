package vn.techres.supplier.adapter.debtadapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemDetailDebtBinding
import vn.techres.supplier.interfaces.OnClickDetailDebt
import vn.techres.supplier.model.datamodel.DataListOrder
import java.math.BigDecimal
import java.text.DecimalFormat

class DetailDebtReceivableAdapter(var context: Context) :
    RecyclerView.Adapter<DetailDebtReceivableAdapter.ViewHolder>() {
    private var dataList = ArrayList<DataListOrder>()
    private var onClickDebt: OnClickDetailDebt? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<DataListOrder>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_detail_debt, parent, false)
        return ViewHolder(itemView)
    }

    fun setClickDetailDebt(onClickDebt: OnClickDetailDebt) {
        this.onClickDebt = onClickDebt
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemDetailDebtBinding.bind(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val debtItem = dataList[position]
        with(holder) {
            binding.code.text = debtItem.code
            binding.time.text = debtItem.delivery_at
            binding.txtBranchName.text = debtItem.branch_name
            binding.totail.text = currencyFormat(debtItem.restaurant_debt_amount.toString())

            when (debtItem.payment_status ) {
                0 -> {
                    binding.status.text = " Chưa thanh toán"
                    binding.status.setTextColor(R.color.red)
                    binding.payStatus.setBackgroundResource(R.drawable.chothanhtoan)
                }
                1 -> {
                    binding.status.text = " Chờ thanh toán"
                    binding.status.setTextColor(R.color.main_bg)
                    binding.payStatus.setBackgroundResource(R.drawable.debt_money)
                }
                2 -> {
                    binding.status.text = "Đã thanh toán"
                    binding.status.setTextColor(Color.parseColor("#0072CB"))
                    binding.payStatus.setBackgroundResource(R.drawable.choduyet)
                }

            }
            binding.linearDetailDebt.setOnClickListener {
                onClickDebt!!.onClick(position, debtItem.id!!)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}