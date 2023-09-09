package vn.techres.supplier.adapter.ordermanageradapterr.summaterialorder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemMaterialOrderBinding
import vn.techres.supplier.model.datamodel.DataSumMaterialOrder

class SumMaterialOrderAdapter(var context: Context) :
    RecyclerView.Adapter<SumMaterialOrderAdapter.ViewHolder>() {
    private var dataList = ArrayList<DataSumMaterialOrder>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<DataSumMaterialOrder>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_material_order, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMaterialOrderBinding.bind(view)
    }

    private fun formatFloatToString(f: Float): String {
        val i = f.toInt()
        return if (f == i.toFloat()) i.toString() else f.toString()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        with(holder) {
            binding.txtNumberCount.text = (position + 1).toString()
            binding.txtNameMaterial.text = data.supplier_material_name
            binding.quantity.text = formatFloatToString(data.quantity)
            binding.quantityLoss.text = formatFloatToString(data.total_quantity)
            binding.supplierUnitName.text = data.supplier_unit_name
            binding.supplierUnitFullName.text = data.supplier_unit_full_name
        }
    }

    override fun getItemCount() = dataList.size
}