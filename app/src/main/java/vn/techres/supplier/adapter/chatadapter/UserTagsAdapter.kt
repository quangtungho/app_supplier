package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.CustomItemUserTagBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.ChangeUserTag
import vn.techres.supplier.model.chat.data.Members
import java.util.*
import java.util.stream.Collectors


open class UserTagsAdapter(var context: Context) :
    RecyclerView.Adapter<UserTagsAdapter.ViewHolder>() {
    private val dataTemp: List<Members>? = null
    private var dataSource = ArrayList<Members>()
    private var mInfos: List<Members>? = null
    private var changeUserTag: ChangeUserTag? = null

    fun setChangeUserTag(changeUserTag: ChangeUserTag) {
        this.changeUserTag = changeUserTag
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_user_tag, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<Members>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = CustomItemUserTagBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = dataSource[position]
            if (data.member_id == -1) {
                binding.imvAvatar.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ava_tag_username
                    )
                )
                binding.tvName.text = context.resources.getString(R.string.tag_all_member)
            } else {
                AppUtils.callPhotoAvatar(data.avatar, binding.imvAvatar)
                binding.tvName.text = data.full_name

            }
            itemView.setOnClickListener {
                changeUserTag!!.onClick(data.full_name, data.member_id, data.avatar)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        val finalCharText = charText
        mInfos = if (charText != "") {
            dataSource!!.stream().filter { t: Members ->
                t.full_name.toLowerCase().contains(finalCharText) || t.normalize_name
                    .toLowerCase().contains(finalCharText) || t.prefix.toLowerCase()
                    .contains(finalCharText)
            }.collect(Collectors.toList<Any>()) as List<Members>?
        } else dataSource
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}
