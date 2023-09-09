package vn.techres.supplier.adapter.revenueadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableGenusBinding
import vn.techres.supplier.interfaces.ClickGensItemsID
import vn.techres.supplier.model.datamodel.DataGenusItems

class GenusItemsAdapter(var context: Context) :
    RecyclerView.Adapter<GenusItemsAdapter.ViewHolder>() {

    private var dataListGenusItems = ArrayList<DataGenusItems>()
    private var clickRecyclerDelete: ClickGensItemsID? = null
    private var clickRecyclerEdit: ClickGensItemsID? = null

    fun setClickRecyclerDelete(clickRecyclerDelete: ClickGensItemsID) {
        this.clickRecyclerDelete = clickRecyclerDelete
    }

    fun setClickRecyclerEdit(clickRecyclerEdit: ClickGensItemsID) {
        this.clickRecyclerEdit = clickRecyclerEdit
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListGenusItems(dataListGenusItems: ArrayList<DataGenusItems>) {
        this.dataListGenusItems = dataListGenusItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_table_genus, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableGenusBinding.bind(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataListGenusItems = dataListGenusItems[position]
        with(holder) {
            binding.txtNumberCount.text = (position + 1).toString()
            binding.txtName.text = dataListGenusItems.name
            binding.txtDescribe.text = dataListGenusItems.description
            binding.btnDeleteOrRestore.setOnClickListener {
                clickRecyclerDelete!!.clickDelete(position, dataListGenusItems.id)
            }
            binding.btnEditAndBack.setOnClickListener {
                clickRecyclerEdit!!.clickEdit(position, dataListGenusItems.id, dataListGenusItems)
            }
            when (dataListGenusItems.supplier_addition_fee_reason_category_id) {
                1, 2 -> {
                    binding.btnEditAndBack.visibility = View.GONE
                    binding.btnDeleteOrRestore.visibility = View.GONE
                }
            }
            when (dataListGenusItems.supplier_addition_fee_type) {
                0 -> binding.couponType.text = "Phiếu thu"
                1 -> binding.couponType.text = "Phiếu chi"
            }
            when (dataListGenusItems.supplier_addition_fee_reason_category_id) {
                1 -> binding.categoryType.text = "Thu tự động từ đơn hàng"
                2 -> binding.categoryType.text = "Chi phiếu từ đơn hàng"
                0 -> binding.categoryType.text = "Chi phiếu khác"
            }
        }

    }

    override fun getItemCount() = dataListGenusItems.size

}