package vn.techres.supplier.adapter.ordermanageradapterr.supplierorderadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableDetailMaterialOrderDeliveringBinding
import vn.techres.supplier.model.datamodel.DataListMatialOrder
import java.text.DecimalFormat

class DialogBillMaterialOrderCanceledAdapter(var context: Context) :
    RecyclerView.Adapter<DialogBillMaterialOrderCanceledAdapter.ViewHolder>() {
    private var billOrderList = ArrayList<DataListMatialOrder>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListOrder(dataListBillOrder: ArrayList<DataListMatialOrder>) {
        this.billOrderList = dataListBillOrder
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table_detail_material_order_delivering, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableDetailMaterialOrderDeliveringBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderBillOrder = billOrderList[position]
        with(holder) {

            binding.txtReceive.visibility = View.VISIBLE
            binding.viewNumberCount.visibility = View.GONE
            binding.viewReceive.visibility = View.VISIBLE
            binding.viewNumberCountOnSupplier.visibility = View.VISIBLE
            binding.txtNumberCountOnSupplier.visibility = View.VISIBLE
            binding.txtNumberCount.text = (position + 1).toString()//stt
            binding.txtNumberCount.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNumberCount.marqueeRepeatLimit = -1
            binding.txtNumberCount.isSelected = true
            binding.txtNumberCountOnSupplier.text =
                orderBillOrder.number_count_on_supplier.toString()
            binding.txtNameMaterial.text = orderBillOrder.supplier_material_name//ten nguyen lieu
            binding.txtNameMaterial.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameMaterial.marqueeRepeatLimit = -1
            binding.txtNameMaterial.isSelected = true
            binding.txtUnit.text = orderBillOrder.supplier_unit_name//don vi
            binding.txtUnit.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtUnit.marqueeRepeatLimit = -1
            binding.txtUnit.isSelected = true
            binding.txtNumberOrders.text = orderBillOrder.request_quantity.toString()//sl dat'
            binding.txtNumberOrders.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNumberOrders.marqueeRepeatLimit = -1
            binding.txtNumberOrders.isSelected = true
            binding.txtNumberOfOrders.text = orderBillOrder.response_quantity.toString()//sl giao
            binding.txtNumberOfOrders.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNumberOfOrders.marqueeRepeatLimit = -1
            binding.txtNumberOfOrders.isSelected = true
            binding.txtReceive.text = orderBillOrder.supplier_rate_quantity.toString()//sl nhận
            binding.txtReceive.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtReceive.marqueeRepeatLimit = -1
            binding.txtReceive.isSelected = true
            binding.returnOrder.text = orderBillOrder.return_quantity.toString() // sl tra
            binding.returnOrder.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.returnOrder.marqueeRepeatLimit = -1
            binding.returnOrder.isSelected = true
            binding.total.text =
                currencyFormat(orderBillOrder.total_price_reality.toString()) // don gia
            binding.dateCreate.text = orderBillOrder.created_at// thoi gian tao
        }
    }

    override fun getItemCount(): Int = billOrderList.size
}
