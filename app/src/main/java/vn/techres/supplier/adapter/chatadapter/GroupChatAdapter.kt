package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.activity.ChatActivity
import vn.techres.supplier.databinding.ItemGroupBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.ClickChatID
import vn.techres.supplier.model.datamodel.ListGroupOrder


class GroupChatAdapter(var context: Context) :
    RecyclerView.Adapter<GroupChatAdapter.ViewHolder>() {
    private var dataSource = ArrayList<ListGroupOrder>()
    private var clickItemChat: ClickChatID? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<ListGroupOrder>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    fun setClickChat(clickItemChat: ClickChatID) {
        this.clickItemChat = clickItemChat
    }

    fun swap(data: ListGroupOrder?, position: Int) {
        dataSource.removeAt(position)
        if (data != null) {
            dataSource.add(0, data)
        }
        notifyItemRemoved(position)
        notifyItemInserted(0)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemGroupBinding.bind(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatItem = dataSource[position]
        with(holder) {

            val listImage: ArrayList<String> = ArrayList()
            for (i in 0 until dataSource.size) {
                listImage.add(dataSource[i].avatar.toString())
            }
            binding.image.setOnClickListener {
                AppUtils.showImageMediaSlider(
                    listImage,
                    position
                )
            }
            AppUtils.callGroupAvatar(chatItem.avatar, binding.image)

            binding.txtGroupName.text =
                chatItem.restaurant_name + " - " + context.getString(R.string.txt_group_chat)
            when (chatItem.last_message_type) {
                null -> binding.message.text =
                    context.getString(R.string.no_message_group)
                1, 7 -> binding.message.text = String.format(
                    "%s : %s",
                    chatItem.user_name_last_message,
                    chatItem.last_message
                )
                2, 3, 4, 5, 13, 8, 9, 11 -> binding.message.text =
                    chatItem.last_message
                17, 19 -> binding.message.text = String.format(
                    "%s  %s",
                    chatItem.user_name_last_message,
                    chatItem.last_message
                )
                25 -> binding.message.text =
                    String.format(
                        "%s : %s ",
                        chatItem.user_name_last_message,
                        context.getString(R.string.chat_order_supplier)
                    )
            }
            binding.txtGroupName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtGroupName.marqueeRepeatLimit = -1
            binding.txtGroupName.isSelected = true

            binding.time.text = chatItem.created_last_message

            if (chatItem.member.number == 0) {
                binding.lnCount.visibility = View.GONE
            } else {
                binding.lnCount.visibility = View.VISIBLE
                binding.count.text = chatItem.member.number.toString()
            }

            if (chatItem.member.number_order == 0) {
                binding.imgOrder.visibility = View.GONE
            } else {
                binding.imgOrder.visibility = View.VISIBLE
            }
            binding.lnClick.setOnClickListener {
                val intent = Intent(
                    binding.lnClick.context,
                    ChatActivity::class.java
                )
                intent.putExtra(TechresEnum.ID_GROUP.toString(), chatItem._id)
                intent.putExtra(TechresEnum.RESTAURANT_ID.toString(), chatItem.restaurant_id)
                intent.putExtra(TechresEnum.SUPPLIER_ID.toString(), chatItem.supplier_id)
                intent.putExtra(
                    TechresEnum.GROUP_TYPE.toString(),
                    chatItem.conversation_type
                )
                startActivity(context, intent, null)

            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

}

