package vn.techres.supplier.adapter.debtadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemDebtBinding
import vn.techres.supplier.model.WareHouse
import java.text.DecimalFormat

class DebtToPayAdapter(var context: Context) :
    RecyclerView.Adapter<DebtToPayAdapter.ViewHolder>() {
    private var dataList = ArrayList<WareHouse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<WareHouse>) {
        this.dataList = dataList
        notifyDataSetChanged()
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
            LayoutInflater.from(context).inflate(R.layout.item_debt, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemDebtBinding.bind(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        with(holder) {
            binding.textDebt.text = "Công nợ chi"
            binding.restaurantName.text = currentItem.code
            binding.create.text = currentItem.created_at
            binding.create.visibility = View.VISIBLE
            binding.totailDebt.text = currencyFormatVND(currentItem.total_amount.toString())

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}