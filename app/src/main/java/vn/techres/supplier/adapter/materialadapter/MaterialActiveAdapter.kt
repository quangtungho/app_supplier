package vn.techres.supplier.adapter.materialadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableMaterialBinding
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.interfaces.OnClickTransIdAndModelMaterial
import vn.techres.supplier.model.datamodel.MaterialManager
import java.text.DecimalFormat

class MaterialActiveAdapter(var context: Context) :
    RecyclerView.Adapter<MaterialActiveAdapter.ViewHolder>() {

    private var dataList = ArrayList<MaterialManager>()
    private var clickRecyclerDelete: OnClickTransID? = null
    private var clickRecyclerEdit: OnClickTransIdAndModelMaterial? = null

    fun setClickRecyclerDelete(clickRecyclerDelete: OnClickTransID) {
        this.clickRecyclerDelete = clickRecyclerDelete
    }

    fun setClickRecyclerEdit(clickRecyclerEdit: OnClickTransIdAndModelMaterial) {
        this.clickRecyclerEdit = clickRecyclerEdit
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<MaterialManager>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_table_material, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableMaterialBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }
    private fun formatFloatToString(f: Float): String {
        val i = f.toInt()
        return if (f == i.toFloat()) i.toString() else f.toString()
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        with(holder) {
            binding.imgDeleteOrRestore.setImageResource(R.drawable.picture_icon_delete)

            binding.txtNumberCount.text = (position + 1).toString()
            binding.txtNameMaterial.text = currentItem.name
            binding.txtCategory.text = currentItem.material_category_name
            binding.txtUnits.text = currentItem.material_unit_name
            binding.txtSpecification.text = currentItem.material_unit_specification_name
            binding.txtWastageRate.text = formatFloatToString(currentItem.wastage_rate)
            binding.txtRetailPrice.text = currencyFormat(currentItem.retail_price.toString())
            binding.txtWholesalePrice.text =
                currencyFormat(currentItem.wholesale_price.toString())
            binding.txtNumber.text = currencyFormat(currentItem.wholesale_price_quantity.toString())

            binding.btnDeleteOrRestore.setOnClickListener {
                clickRecyclerDelete!!.onClick(position, currentItem.id)
            }
            binding.btnEditAndBack.setOnClickListener {
                clickRecyclerEdit!!.onClick(position, currentItem.id, currentItem)
            }
        }
    }


    override fun getItemCount() = dataList.size
}