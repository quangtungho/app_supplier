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

class DialogBillMaterialOrderDeliveringAdapter(var context: Context) :
    RecyclerView.Adapter<DialogBillMaterialOrderDeliveringAdapter.ViewHolder>() {
    private var billOrderList = ArrayList<DataListMatialOrder>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListOrder(dataListBillOrder: ArrayList<DataListMatialOrder>) {
        this.billOrderList = dataListBillOrder
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
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

    private fun formatFloatToString(f: Float): String {
        val i = f.toInt()
        return if (f == i.toFloat()) i.toString() else f.toString()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderBillOrder = billOrderList[position]
        with(holder) {
            binding.viewtotal.visibility = View.GONE
            binding.returnOrder.visibility = View.GONE
            binding.viewNumberCount.visibility = View.GONE
            binding.viewNumberCountOnSupplier.visibility = View.VISIBLE
            binding.txtNumberCountOnSupplier.visibility = View.VISIBLE

            binding.txtNumberCount.text = (position + 1).toString()//stt

            binding.txtNumberCountOnSupplier.text =
                orderBillOrder.number_count_on_supplier.toString()
            binding.txtNameMaterial.text = orderBillOrder.supplier_material_name//ten nguyen lieue
            binding.txtNameMaterial.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameMaterial.marqueeRepeatLimit = -1
            binding.txtNameMaterial.isSelected = true


            binding.txtUnit.text = orderBillOrder.supplier_unit_name// don vi
            binding.txtUnit.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtUnit.marqueeRepeatLimit = -1
            binding.txtUnit.isSelected = true

            binding.txtNumberOrders.text =
                formatFloatToString(orderBillOrder.total_quantity!!.toFloat())//sl dat


            binding.txtNumberOfOrders.text =
                formatFloatToString(orderBillOrder.supplier_rate_quantity!!.toFloat())//sl dap ung


            binding.priceTotal.text =
                currencyFormat(orderBillOrder.price_reality.toString())//don gia
            binding.priceTotal.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.priceTotal.marqueeRepeatLimit = -1
            binding.priceTotal.isSelected = true

            binding.total.text =
                currencyFormat(orderBillOrder.total_price_reality.toString())// thanh tien
            binding.total.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.total.marqueeRepeatLimit = -1
            binding.total.isSelected = true

            binding.dateCreate.text = orderBillOrder.created_at// thoi gian tao


        }
    }

    override fun getItemCount(): Int = billOrderList.size

}
