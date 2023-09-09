package vn.techres.supplier.adapter.materialadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemMaterialCreateBinding
import vn.techres.supplier.interfaces.MaterialHandler
import vn.techres.supplier.model.datamodel.MaterialManager
import java.text.DecimalFormat

class MaterialCreateAdapter(var context: Context) :
    RecyclerView.Adapter<MaterialCreateAdapter.ViewHolder>() {

    private var dataList = ArrayList<MaterialManager>()
    private var materialHandler: MaterialHandler? = null

    fun setMaterialHandler(materialHandler: MaterialHandler) {
        this.materialHandler = materialHandler
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<MaterialManager>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_material_create, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMaterialCreateBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        with(holder) {
            binding.txtNumberCount.text = (position + 1).toString()
            binding.txtNameMaterial.text = data.name
            binding.txtNote.text = data.note
            val spinnerTextUnits =
                arrayListOf(data.material_unit_name, data.material_unit_specification_name)

            binding.spinnerUnits.adapter = ArrayAdapter(
                context,
                R.layout.color_spinner_table_create_ware_house,
                spinnerTextUnits
            )

            binding.txtQuantity.text = data.quantity.toString()
            binding.txtPrice.text = currencyFormat(data.retail_price.toString())
            binding.txtTotalMoney.text =
                currencyFormat((data.retail_price * data.quantity).toString())


            binding.txtQuantity.setOnClickListener {
                materialHandler!!.inputMaterial(position, data, "quantity")
            }

            binding.txtPrice.setOnClickListener {
                materialHandler!!.inputMaterial(position, data, "price")
            }

            binding.txtNote.setOnClickListener {
                materialHandler!!.inputMaterial(position, data, "note")
            }

            binding.lnDeleteOrRestore.setOnClickListener {
                materialHandler!!.onClick(position, data.id, data)
            }
        }
    }


    override fun getItemCount() = dataList.size
}