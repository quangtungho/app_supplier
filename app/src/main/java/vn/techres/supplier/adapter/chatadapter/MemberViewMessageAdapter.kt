package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemMemberViewMessageBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.model.chat.data.MessageView

class MemberViewMessageAdapter(var context: Context) :
    RecyclerView.Adapter<MemberViewMessageAdapter.ViewHolder>() {
    private var dataSource = ArrayList<MessageView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_member_view_message, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<MessageView>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMemberViewMessageBinding.bind(view)
    }


    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            AppUtils.callPhotoAvatar(dataSource[position].avatar, binding.imvAvatar)

            dataSource[position].full_name!!.split(" ").toTypedArray()
            binding.tvName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.tvName.marqueeRepeatLimit = -1
            binding.tvName.text = dataSource[position].full_name
            binding.tvName.isSelected = true

        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}