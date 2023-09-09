package vn.techres.supplier.adapter.customeradapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemCustomerCustomerBinding
import vn.techres.supplier.interfaces.OnClickCustomerID
import vn.techres.supplier.model.datamodel.Customer

class CustomerActiveAdapter(var context: Context) :
    RecyclerView.Adapter<CustomerActiveAdapter.ViewHolder>() {
    private var dataList: ArrayList<Customer>? = null
    private var clickRecycler: OnClickCustomerID? = null
    private var itemCount = 0

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<Customer>) {
        this.dataList = dataList
        itemCount = dataList.size
        notifyDataSetChanged()
    }

    fun setClickRecycler(clickRecycler: OnClickCustomerID) {
        this.clickRecycler = clickRecycler
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_customer_customer, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCustomerCustomerBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList?.get(position)
        if (currentItem != null) {
            with(holder) {
                binding.txtName.text = currentItem.name
                binding.txtName.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.txtName.marqueeRepeatLimit = -1
                binding.txtName.isSelected = true

                binding.contactName.text = currentItem.contact_name
                binding.txtPhone.text = currentItem.phone
                binding.txtAddress.text = currentItem.address
                binding.txtAddress.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.txtAddress.marqueeRepeatLimit = -1
                binding.txtAddress.isSelected = true
                binding.txtEmail.text = currentItem.email
                binding.txtEmail.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.txtEmail.marqueeRepeatLimit = -1
                binding.txtEmail.isSelected = true
                binding.txtNumberBranch.text = currentItem.number_branches.toString()
                binding.linearLayoutView.setOnClickListener {
                    clickRecycler!!.onClick(
                        position,
                        currentItem.id, currentItem.contactor_avatar
                    )
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return itemCount
    }
}