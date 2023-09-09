package vn.techres.supplier.adapter.unitsadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableUnitsBinding
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.interfaces.OnClickTransIdAndModelUnits
import vn.techres.supplier.model.datamodel.DataUnits

class UnitsActiveAdapter(var context: Context) :
    RecyclerView.Adapter<UnitsActiveAdapter.ViewHolder>() {

    private var dataListUnits = ArrayList<DataUnits>()
    private var clickRecyclerDelete: OnClickTransID? = null
    private var clickRecyclerEdit: OnClickTransIdAndModelUnits? = null

    fun setClickRecyclerDelete(clickRecyclerDelete: OnClickTransID) {
        this.clickRecyclerDelete = clickRecyclerDelete
    }

    fun setClickRecyclerEdit(clickRecyclerEdit: OnClickTransIdAndModelUnits) {
        this.clickRecyclerEdit = clickRecyclerEdit
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListUnits(dataListUnits: ArrayList<DataUnits>) {
        this.dataListUnits = dataListUnits
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_table_units, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableUnitsBinding.bind(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataListUnits[position]
        with(holder) {
            binding.txtNumberCount.text = (position + 1).toString()
            binding.txtNameUnits.text = currentItem.name
            binding.txtDescribe.text = currentItem.description

            binding.imgDeleteOrRestore.setImageResource(R.drawable.picture_icon_delete)

            val specificationsText = StringBuilder()

            for (item in currentItem.material_unit_specification.indices) {
                if (item == currentItem.material_unit_specification.size - 1) {
                    specificationsText.append(currentItem.material_unit_specification[item].material_unit_specification_exchange_name)
                } else {
                    specificationsText.append(currentItem.material_unit_specification[item].material_unit_specification_exchange_name)
                    specificationsText.append(", ")
                }
                binding.txtSpecification.text = specificationsText.toString()

                binding.btnDeleteOrRestore.setOnClickListener {
                    clickRecyclerDelete!!.onClick(position, currentItem.id)
                }

                binding.btnEditAndBack.setOnClickListener {
                    clickRecyclerEdit!!.onClick(
                        position,
                        currentItem.id,
                        currentItem.material_unit_specification
                    )
                }
            }
        }
    }


        override fun getItemCount() = dataListUnits.size

    }