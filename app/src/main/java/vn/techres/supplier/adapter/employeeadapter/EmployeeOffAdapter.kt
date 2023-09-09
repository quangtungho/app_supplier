package vn.techres.supplier.adapter.employeeadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemEmployeeOffBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.ClickIntentProfile
import vn.techres.supplier.model.datamodel.DataListEmployee

class EmployeeOffAdapter(var context: Context) :
    RecyclerView.Adapter<EmployeeOffAdapter.ViewHolder>() {
    private var dataSourceOff = ArrayList<DataListEmployee>()
    private var clickIntentProfile: ClickIntentProfile? = null
    private var itemCount = 0

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSourceOff(dataSourceOff: ArrayList<DataListEmployee>) {
        this.dataSourceOff = dataSourceOff
        itemCount = dataSourceOff.size
        notifyDataSetChanged()
    }

    fun setClickIntentProfile(clickIntentProfile: ClickIntentProfile) {
        this.clickIntentProfile = clickIntentProfile

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_employee_off, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemEmployeeOffBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val staffItem = dataSourceOff[position]
        with(holder) {
            AppUtils.callPhoto(staffItem.avatar, binding.imgStaff)
            if (dataSourceOff[position].status == 0) {
                binding.switcher.setChecked(false)
            } else if (dataSourceOff[position].status == 1) {
                binding.switcher.setChecked(true)
            }

            binding.txtName.text = staffItem.name
            binding.txtName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtName.marqueeRepeatLimit = -1
            binding.txtName.isSelected = true
            binding.txtRole.text = staffItem.supplier_employee_position
            binding.txtRole.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtRole.marqueeRepeatLimit = -1
            binding.txtRole.isSelected = true

            binding.lnDialog.setOnClickListener {
                clickIntentProfile!!.onClick(position, staffItem.id!!.toInt())

            }

            binding.switcher.setOnClickListener {
                clickIntentProfile!!.onCheck(position, staffItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSourceOff.size
    }
}