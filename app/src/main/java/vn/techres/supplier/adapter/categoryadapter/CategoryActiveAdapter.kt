package vn.techres.supplier.adapter.categoryadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableCategoryBinding
import vn.techres.supplier.model.datamodel.DataCategory

class CategoryActiveAdapter(var context: Context) :
    RecyclerView.Adapter<CategoryActiveAdapter.ViewHolder>() {
    private var dataList = ArrayList<DataCategory>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<DataCategory>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableCategoryBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_table_category, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        with(holder) {
            binding.txtNumberCount.text = (position + 1).toString()
            binding.txtNameCategory.text = currentItem.name
            binding.txtNameCategory.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameCategory.marqueeRepeatLimit = -1
            binding.txtNameCategory.isSelected = true
            binding.txtDescribe.text = currentItem.description
            binding.txtDescribe.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtDescribe.marqueeRepeatLimit = -1
            binding.txtDescribe.isSelected = true
        }
    }


    override fun getItemCount() = dataList.size
}