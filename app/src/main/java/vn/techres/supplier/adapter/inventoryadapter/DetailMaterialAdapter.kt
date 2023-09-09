package vn.techres.supplier.adapter.inventoryadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemBillWarehouseBinding
import vn.techres.supplier.model.datamodel.DataMaterialSelected
import java.text.DecimalFormat

class DetailMaterialAdapter(var context: Context) :
    RecyclerView.Adapter<DetailMaterialAdapter.ViewHolder>() {
    private var dataList = DataMaterialSelected()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: DataMaterialSelected) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_bill_warehouse, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemBillWarehouseBinding.bind(view)
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
        val data = dataList.list[position]
        with(holder) {
            binding.txtNumberCount.text = (position + 1).toString()
            binding.txtNameMaterial.text = data.supplier_material_name
            binding.txtUnits.text =
                String.format(
                    "%s %s", formatFloatToString(data.quantity.toFloat()),
                    data.unit_name

                )
            binding.txtTotal.text = currencyFormat(data.supplier_input_price.toString())
            binding.txtTotalMoney.text =
                currencyFormat(data.total_price.toString())

        }
    }

    override fun getItemCount() = dataList.list.size
}