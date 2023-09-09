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
import vn.techres.supplier.model.datamodel.DataReturnsOrder
import java.text.DecimalFormat

class DialogBillMaterialOrderReturnAdapter(var context: Context) :
    RecyclerView.Adapter<DialogBillMaterialOrderReturnAdapter.ViewHolder>() {
    private var billOrderList = DataReturnsOrder()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListOrder(dataListBillOrder: DataReturnsOrder) {
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
        val orderBillOrder =
            billOrderList.supplier_material_return_request_details[position]
        with(holder) {
            binding.view1.visibility =View.GONE
            binding.viewReceive.visibility =View.GONE

            binding.viewNumberCount.visibility = View.GONE
            binding.txtNumberCount.text = (position + 1).toString() // stt
            binding.txtNumberCount.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNumberCount.marqueeRepeatLimit = -1
            binding.txtNumberCount.isSelected = true

            binding.txtNameMaterial.text = orderBillOrder.supplier_material_name // ten nguyen lieu
            binding.txtNameMaterial.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameMaterial.marqueeRepeatLimit = -1
            binding.txtNameMaterial.isSelected = true


            binding.txtUnit.text = orderBillOrder.supplier_unit_name // don vi
            binding.txtUnit.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtUnit.marqueeRepeatLimit = -1
            binding.txtUnit.isSelected = true

            binding.txtNumberOrders.visibility = View.GONE
//                formatFloatToString(orderBillOrder.quantity!!.toFloat()) // sl dat hang
//            binding.txtNumberOrders.ellipsize = TextUtils.TruncateAt.MARQUEE
//            binding.txtNumberOrders.marqueeRepeatLimit = -1
//            binding.txtNumberOrders.isSelected = true

            binding.txtNumberOfOrders.visibility = View.GONE
            //=
//                formatFloatToString(orderBillOrder.accept_quantity!!.toFloat())  // so luong giao
//            binding.txtNumberOfOrders.ellipsize = TextUtils.TruncateAt.MARQUEE
//            binding.txtNumberOfOrders.marqueeRepeatLimit = -1
//            binding.txtNumberOfOrders.isSelected = true
//
            binding.returnOrder.text =
                formatFloatToString(orderBillOrder.quantity!!.toFloat()) // tra hang
            binding.returnOrder.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.returnOrder.marqueeRepeatLimit = -1
            binding.returnOrder.isSelected = true
//
            binding.priceTotal.text =
                currencyFormat(orderBillOrder.price.toString()) // don gia
            binding.priceTotal.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.priceTotal.marqueeRepeatLimit = -1
            binding.priceTotal.isSelected = true

            binding.total.text =
                currencyFormat(orderBillOrder.total_price.toString()) // thanhf tien
            binding.total.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.total.marqueeRepeatLimit = -1
            binding.total.isSelected = true

            binding.dateCreate.text = orderBillOrder.created_at // ngayf tao
            binding.dateCreate.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.dateCreate.marqueeRepeatLimit = -1
            binding.dateCreate.isSelected = true

        }
    }

    override fun getItemCount(): Int = billOrderList.supplier_material_return_request_details.size

}
