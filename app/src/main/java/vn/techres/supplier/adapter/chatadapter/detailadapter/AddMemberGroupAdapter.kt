package vn.techres.supplier.adapter.chatadapter.detailadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.databinding.ItemMemberNotificationSettingBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.RoleClick
import vn.techres.supplier.model.datamodel.DataListEmployee

class AddMemberGroupAdapter(var context: Context) :
    RecyclerView.Adapter<AddMemberGroupAdapter.ViewHolder>() {

    private var dataSource = ArrayList<DataListEmployee>()

    private var roleClick: RoleClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_member_notification_setting, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<DataListEmployee>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    @JvmName("setRoleClick1")
    fun setRoleClick(roleClick: RoleClick) {
        this.roleClick = roleClick
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMemberNotificationSettingBinding.bind(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            val data = dataSource[position]

            AppUtils.callPhotoAvatar(data.avatar, binding.imvAvatar)
            binding.tvName.text = data.name
            binding.tvRoleName.text = data.supplier_employee_position
            binding.imvStatus.setImageResource(R.drawable.ic_circle_online)

            if (data.is_join == true) {
                binding.tvMemberJoined.visibility = View.VISIBLE
                binding.imvCheckJoin.visibility = View.GONE
            } else {
                binding.tvMemberJoined.visibility = View.GONE
                binding.imvCheckJoin.visibility = View.VISIBLE
                itemView.setOnClickListener {
                    if (data.join_check == true) {
                        dataSource[position].join_check = false
                        binding.imvCheckJoin.setImageDrawable(
                            SupplierApplication.context.getDrawable(
                                R.drawable.ic_correct_no
                            )
                        )
                        roleClick!!.onItemTag(
                            data,
                            position,
                            false
                        )
                    } else {
                        dataSource[position].join_check = true
                        binding.imvCheckJoin.setImageDrawable(
                            SupplierApplication.context.getDrawable(
                                R.drawable.ic_correct_check
                            )
                        )
                        roleClick!!.onItemTag(
                            data,
                            position,
                            true
                        )
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}