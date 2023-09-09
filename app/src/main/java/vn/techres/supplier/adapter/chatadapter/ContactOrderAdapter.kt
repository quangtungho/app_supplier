package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemSelectEmployeeDefaultBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.OnClickContactOrder
import vn.techres.supplier.model.datamodel.DataMemberOrder

class ContactOrderAdapter(var context: Context) :
    RecyclerView.Adapter<ContactOrderAdapter.ViewHolder>() {
    private var dataSource = ArrayList<DataMemberOrder>()
    private var onClickContactOrder: OnClickContactOrder? = null
    private var restaurantID = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_select_employee_default, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<DataMemberOrder>, restaurantID: Int) {
        this.dataSource = dataSource
        this.restaurantID = restaurantID
        notifyDataSetChanged()
    }

    fun setGroupOrderCall(onClickContactOrderCall: OnClickContactOrder) {
        this.onClickContactOrder = onClickContactOrderCall
    }


    fun setGroupOrderCallVideo(onClickContactOrderCallVideo: OnClickContactOrder) {
        this.onClickContactOrder = onClickContactOrderCallVideo
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSelectEmployeeDefaultBinding.bind(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contactOrder = dataSource[position]
        with(holder) {
            AppUtils.callPhotoAvatar(
                contactOrder.avatar,
                binding.imvAvatar
            )
            contactOrder.full_name!!.split(" ").toTypedArray()
            binding.tvName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.tvName.marqueeRepeatLimit = -1
            binding.tvName.text = contactOrder.full_name
            binding.tvName.isSelected = true
            binding.tvRoleName.text = contactOrder.role_name
            binding.imgVideoCall.visibility = View.VISIBLE
            binding.cardView.visibility = View.GONE
            binding.imgCall.visibility = View.VISIBLE
            binding.imgCall.setOnClickListener {
                onClickContactOrder!!.onClickCall(contactOrder, restaurantID)
            }
            binding.imgVideoCall.setOnClickListener {
                onClickContactOrder!!.onClickCallVideo(contactOrder, restaurantID)

            }

        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}