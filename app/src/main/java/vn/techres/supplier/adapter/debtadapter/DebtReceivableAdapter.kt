package vn.techres.supplier.adapter.debtadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemDebtBinding
import vn.techres.supplier.interfaces.DetailDebt
import vn.techres.supplier.model.datamodel.DataDebtReceivable
import java.text.DecimalFormat

class DebtReceivableAdapter(var context: Context) :
    RecyclerView.Adapter<DebtReceivableAdapter.ViewHolder>() {
    private var dataList = DataDebtReceivable()
    private var clickDetailDebt: DetailDebt? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: DataDebtReceivable) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_debt, parent, false)
        return ViewHolder(itemView)
    }

    fun setClickDetailDebt(clickDetailDebt: DetailDebt) {
        this.clickDetailDebt = clickDetailDebt
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemDebtBinding.bind(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val debtItem = dataList.list[position]
        with(holder) {
            binding.linearNext.visibility = View.VISIBLE
            binding.textDebt.text = "Công nợ thu"
            binding.restaurantName.text = debtItem.restaurant_name
            binding.restaurantName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.restaurantName.marqueeRepeatLimit = -1
            binding.restaurantName.isSelected = true

            binding.totailDebt.text = currencyFormat(debtItem.debt_amount.toString())
            binding.linearClickDebt.setOnClickListener {
                clickDetailDebt!!.onClickDebt(position, debtItem.restaurant_id)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.list.size
    }
}