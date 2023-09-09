package vn.techres.supplier.adapter.specificationadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableSpecificationBinding
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.interfaces.OnClickTransIdAndModelSpecification
import vn.techres.supplier.model.datamodel.DataSpecification
import java.text.DecimalFormat

class SpecificationActiveAdapter(var context: Context) :
    RecyclerView.Adapter<SpecificationActiveAdapter.ViewHolder>() {

    private var dataList = ArrayList<DataSpecification>()
    private var clickRecyclerDelete: OnClickTransID? = null
    private var clickRecyclerEdit: OnClickTransIdAndModelSpecification? = null

    fun setClickRecyclerDelete(clickRecyclerDelete: OnClickTransID) {
        this.clickRecyclerDelete = clickRecyclerDelete
    }

    fun setClickRecyclerEdit(clickRecyclerEdit: OnClickTransIdAndModelSpecification) {
        this.clickRecyclerEdit = clickRecyclerEdit
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<DataSpecification>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_table_specification, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableSpecificationBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        with(holder) {
            binding.imgDeleteOrRestore.setImageResource(R.drawable.picture_icon_delete)
            binding.txtNumberCount.text = (position + 1).toString()
            binding.txtNameExchange.text = currentItem.name
            binding.txtValueExchange.text = currencyFormat(currentItem.exchange_value.toString())
            binding.txtUnitExchange.text = currentItem.material_unit_specification_exchange_name
            binding.btnDeleteOrRestore.setOnClickListener {
                clickRecyclerDelete!!.onClick(
                    position,
                    currentItem.id
                )
            }
            binding.btnEditAndBack.setOnClickListener {
                clickRecyclerEdit!!.onClick(
                    position,
                    currentItem.id,
                    currentItem
                )
            }
        }
    }

    override fun getItemCount() = dataList.size

}