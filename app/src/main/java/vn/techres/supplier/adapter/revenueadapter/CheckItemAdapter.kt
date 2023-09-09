package vn.techres.supplier.adapter.revenueadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemBillCheckBinding
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.WareHouse
import java.text.DecimalFormat

class CheckItemAdapter(var context: Context) :
    RecyclerView.Adapter<CheckItemAdapter.ViewHolder>() {
    private var checkItem = ArrayList<WareHouse>()
    private var clickDetail: OnClickTransID? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataCheckItem(dataCheckItem: ArrayList<WareHouse>) {
        this.checkItem = dataCheckItem
        notifyDataSetChanged()
    }

    fun setClickDetail(clickDetail: OnClickTransID) {
        this.clickDetail = clickDetail

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bill_check, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemBillCheckBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataList = checkItem[position]
        with(holder) {
            binding.code.text = dataList.code
            binding.totail.text = currencyFormat(dataList.total_amount.toString())
            binding.time.text = dataList.created_at
            binding.lnDetailCheckItem.setOnClickListener {
                clickDetail!!.onClick(position, dataList.id!!)
            }
        }
    }

    override fun getItemCount(): Int = checkItem.size
}
