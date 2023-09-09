package vn.techres.supplier.adapter.employeeadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemEmployeeOnBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.ClickIntentProfile
import vn.techres.supplier.model.datamodel.DataListEmployee

class EmployeeOnAdapter(var context: Context) :
    RecyclerView.Adapter<EmployeeOnAdapter.ViewHolder>() {
    private var dataSourceOn = ArrayList<DataListEmployee>()
    private var clickIntentProfile: ClickIntentProfile? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSourceOn(dataSourceOn: ArrayList<DataListEmployee>) {
        this.dataSourceOn = dataSourceOn
        notifyDataSetChanged()
    }

    fun setClickIntentProfile(clickIntentProfile: ClickIntentProfile) {
        this.clickIntentProfile = clickIntentProfile

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_employee_on, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemEmployeeOnBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val staffItem = dataSourceOn[position]
        with(holder) {
            binding.txtName.text = staffItem.name
            binding.txtName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtName.marqueeRepeatLimit = -1
            binding.txtName.isSelected = true

            binding.txtRole.text = staffItem.supplier_employee_position.toString()
            binding.txtRole.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtRole.marqueeRepeatLimit = -1
            binding.txtRole.isSelected = true
            AppUtils.callPhoto(staffItem.avatar, binding.imgStaff)

            if (dataSourceOn[position].status == 1) {
                binding.switcher.setChecked(true)
            } else if (dataSourceOn[position].status == 0) {
                binding.switcher.setChecked(false)
            }

            binding.switcher.setOnClickListener {
                clickIntentProfile!!.onCheck(position, staffItem)
            }

            binding.lnDialog.setOnClickListener {
                clickIntentProfile!!.onClick(position, staffItem.id!!.toInt())
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSourceOn.size
    }
}