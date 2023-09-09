package vn.techres.supplier.adapter.restaurantcontactor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemContactorsBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.model.datamodel.DataRestaurantContactors

class ContactorAdapter(val context: Context) :
    RecyclerView.Adapter<ContactorAdapter.ViewHolder>() {
    private var dataSource = ArrayList<DataRestaurantContactors>()
    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<DataRestaurantContactors>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contactors, parent, false)
        return ViewHolder(view)
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemContactorsBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contactorItem = dataSource[position]
        with(holder) {
            AppUtils.callPhoto(contactorItem.avatar, binding.imgAvatar)

            binding.txtNameRestaurant.text = contactorItem.restaurant_name
            binding.txtBrand.text = contactorItem.branch_name
            binding.txtNameContactor.text = contactorItem.full_name
            binding.txtPhone.text = contactorItem.phone
            binding.txtAddress.text = contactorItem.address
            binding.txtEmail.text = contactorItem.email
            if (dataSource[position].is_main_contactor == 1) {
                binding.rbContact.isChecked = false
            } else if (dataSource[position].is_main_contactor == 0) {
                binding.rbContact.isChecked = true

            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}

