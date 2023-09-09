package vn.techres.supplier.adapter.restaurantcontactor

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemBranchCustomerprofileBinding
import vn.techres.supplier.model.datamodel.DataBranch

class BranchCustomerAdapter(var context: Context) :
    RecyclerView.Adapter<BranchCustomerAdapter.ViewHolder>() {

    private var dataList: ArrayList<DataBranch>? = null
    private var itemCount = 0

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<DataBranch>) {
        this.dataList = dataList
        itemCount = dataList.size
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemBranchCustomerprofileBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context)
                .inflate(R.layout.item_branch_customerprofile, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList!![position]
        with(holder) {
            binding.txtNameBranch.text = currentItem.name
            binding.txtNameBranch.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameBranch.marqueeRepeatLimit = -1
            binding.txtNameBranch.isSelected = true

            binding.txtAddressBranch.text = currentItem.address_full_text
            binding.txtAddressBranch.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtAddressBranch.marqueeRepeatLimit = -1
            binding.txtAddressBranch.isSelected = true
            binding.txtPhoneBranch.text = currentItem.phone_number
        }
    }

    override fun getItemCount() = itemCount

}