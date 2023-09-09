package vn.techres.supplier.activity

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import vn.techres.supplier.R
import vn.techres.supplier.adapter.chatadapter.ChatAdapter
import vn.techres.supplier.adapter.chatadapter.MemberReceivedMessageAdapter
import vn.techres.supplier.adapter.chatadapter.MemberViewMessageAdapter
import vn.techres.supplier.base.BaseBindingActivity
import vn.techres.supplier.databinding.ActivityInformationMessageBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.PreCachingLayoutManager
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.chat.data.DetailGroup
import vn.techres.supplier.model.chat.data.Members
import vn.techres.supplier.model.chat.data.MessagesByGroup
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class InformationMessageActivity : BaseBindingActivity<ActivityInformationMessageBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityInformationMessageBinding
        get() = ActivityInformationMessageBinding::inflate
    var members = ArrayList<Members>()
    private var memberViewMessageAdapter: MemberViewMessageAdapter? = null
    private var memberReceivedMessageAdapter: MemberReceivedMessageAdapter? = null
    var chatAdapter: ChatAdapter? = null

    override fun onSetBodyView() {
        binding.header.toolbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.header.toolbarAction1.visibility = View.GONE
        binding.header.toolbarAction2.visibility = View.GONE
        binding.header.toolbarAction3.visibility = View.GONE


        val gson = Gson()
        val detailGroup: DetailGroup =
            gson.fromJson(
                intent.getStringExtra(TechresEnum.DETAIL_GROUP_CHAT.toString()),
                DetailGroup::class.java
            )
        AppUtils.callGroupAvatar(detailGroup.avatar_restaurant, binding.header.imvAvatar)
        binding.header.tvTitle.text = detailGroup.name

        binding.header.tvCountMember.text =
            String.format("%s %s", detailGroup.number_employee, getString(R.string.members))


        val messagesByGroups: ArrayList<MessagesByGroup> = ArrayList()

        val messagesByGroup: MessagesByGroup =
            gson.fromJson(
                intent.getStringExtra(TechresEnum.MESSAGE_CHAT.toString()),
                MessagesByGroup::class.java
            )

        messagesByGroups.add(messagesByGroup)

        chatAdapter = ChatAdapter(this)

        //set up chat rcl
        binding.rcvChat.adapter = chatAdapter
        binding.rcvChat.layoutManager =
            PreCachingLayoutManager(this, RecyclerView.VERTICAL, true)
        binding.rcvChat.setHasFixedSize(true)
        binding.rcvChat.itemAnimator = DefaultItemAnimator()

        //set up user chua da xem
        binding.rclUserView.layoutManager = GridLayoutManager(
            baseContext, 4
        )
        binding.rclUserView.setHasFixedSize(true)
        binding.rclUserView.itemAnimator = DefaultItemAnimator()
        memberViewMessageAdapter = MemberViewMessageAdapter(this)
        binding.rclUserView.adapter = memberViewMessageAdapter
        binding.tvUserView.text = String.format(
            "%s %s%s%s",
            "Đã xem",
            "(",
            messagesByGroup.message_viewed.size,
            ")"
        )
        val listIdMember: ArrayList<Int> = ArrayList()
        for (i in 0 until messagesByGroup.message_viewed.size) {
            if (messagesByGroup.message_viewed[i].app_name == "supplier") {
                listIdMember.add(messagesByGroup.message_viewed[i].member_id)
            }
        }
        binding.tvUserReceived.text =
            String.format(
                "%s %s%s%s",
                getString(R.string.received),
                "(",
                detailGroup.members.size,
                ")"
            )
        //set up user chua da xem
        binding.rclUserReceived.layoutManager = GridLayoutManager(
            baseContext, 4
        )
        memberReceivedMessageAdapter = MemberReceivedMessageAdapter(this)
        binding.rclUserReceived.adapter = memberReceivedMessageAdapter
        chatAdapter!!.setDataSource(messagesByGroups)
        memberViewMessageAdapter!!.setDataSource(messagesByGroups[0].message_viewed)
        memberReceivedMessageAdapter!!.setDataSource(detailGroup.members)
    }
}

