package vn.techres.supplier.adapter.inventoryadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableMaterialWarehouseBinding
import vn.techres.supplier.interfaces.OnClickTransTypeAndString
import vn.techres.supplier.model.datamodel.RecyclerViewMaterialWareHouse
import java.text.DecimalFormat

class MaterialWareHouseAdapter(var context: Context) :
    RecyclerView.Adapter<MaterialWareHouseAdapter.ViewHolder>() {
    private var dataList = ArrayList<RecyclerViewMaterialWareHouse>()
    private var clickRecyclerView: OnClickTransTypeAndString? = null

    private var type = 0

    fun setDataList(dataList: ArrayList<RecyclerViewMaterialWareHouse>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun setClickRecyclerView(clickRecyclerView: OnClickTransTypeAndString) {
        this.clickRecyclerView = clickRecyclerView
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context)
                .inflate(R.layout.item_table_material_warehouse, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableMaterialWarehouseBinding.bind(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        with(holder) {
            binding.txtNumberCount.text = (position + 1).toString()
            binding.txtNameMaterial.text = currentItem.nameMaterial
            binding.txtNumber.text = currentItem.number.toString()
            binding.txtUnits.text = currentItem.nameUnits
            binding.txtPrice.text =
                currencyFormat(currentItem.price.toString()) + " " + context.getString(R.string.txt_vnd)

            binding.btnDelete.setOnClickListener {
                type = 0
                clickRecyclerView!!.onClick(position, type, currentItem.nameMaterial)
            }

            binding.btnEditAndBack.setOnClickListener {
                type = 1
                clickRecyclerView!!.onClick(position, type, currentItem.nameMaterial)
            }
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }
}