package vn.techres.supplier.adapter.revenueadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemBillBinding
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.datamodel.DataSupplierOrder
import java.text.DecimalFormat

class ReceptsItemAdapter(var context: Context) :
    RecyclerView.Adapter<ReceptsItemAdapter.ViewHolder>() {
    private var receiptsItem = ArrayList<DataSupplierOrder>()
    private var onClickView: OnClickTransID? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataReceiptsItem(dataListReceipts: ArrayList<DataSupplierOrder>) {
        this.receiptsItem = dataListReceipts
        notifyDataSetChanged()
    }

    fun setClickView(onClickView: OnClickTransID) {
        this.onClickView = onClickView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bill, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemBillBinding.bind(view)
    }


    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataList = receiptsItem[position]
        with(holder) {
            binding.code.text = dataList.code
           binding.totail.text = currencyFormat(dataList.restaurant_debt_amount.toString())
            binding.time.text = dataList.received_at
            binding.linearLayoutView.setOnClickListener {
                onClickView!!.onClick(
                    position,
                    dataList.id
                )
            }
        }
    }

    override fun getItemCount(): Int = receiptsItem.size
}

