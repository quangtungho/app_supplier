package vn.techres.supplier.adapter.chatadapter.detailadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemSelectRoleDefaultBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.OnClickCall
import vn.techres.supplier.interfaces.RoleClick
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.chat.data.Members

class DetailGroupAdapter(var context: Context) :
    RecyclerView.Adapter<DetailGroupAdapter.ViewHolder>() {
    private var dataSource = ArrayList<Members>()
    private var roleClick: RoleClick? = null
    private var onClickContactOrder: OnClickCall? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_select_role_default, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<Members>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    @JvmName("setRoleClick1")
    fun setRoleClick(roleClick: RoleClick) {
        this.roleClick = roleClick
    }

    fun setGroupOrderCall(onClickContactOrderCall: OnClickCall) {
        this.onClickContactOrder = onClickContactOrderCall
    }


    fun setGroupOrderCallVideo(onClickContactOrderCallVideo: OnClickCall) {
        this.onClickContactOrder = onClickContactOrderCallVideo
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSelectRoleDefaultBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = dataSource[position]
            val listImage: ArrayList<String> = ArrayList()
            for (i in 0 until dataSource.size) {
                listImage.add(dataSource[i].avatar)
            }
            binding.imvAvatar.setOnClickListener {
                AppUtils.showImageMediaSlider(
                    listImage,
                    position
                )
            }
            AppUtils.callGroupAvatar(data.avatar, binding.imvAvatar)

            binding.tvName.text = data.full_name
            binding.tvRoleName.text = data.role_name
            if (data.app_name != "supplier") {
                binding.imgMore.visibility = View.GONE
                binding.callAction.visibility = View.VISIBLE
            } else {
                binding.imgMore.visibility = View.VISIBLE
                binding.callAction.visibility = View.GONE
            }
            if (data.member_id == CurrentUser.getCurrentUser(context)!!.id || data.role_id == 1)
                binding.imgMore.visibility = View.GONE
            binding.imgMore.setOnClickListener {
                roleClick!!.onItemTag(data.member_id, position, true)
            }
            binding.imgCall.setOnClickListener {
                onClickContactOrder!!.onClickCall(data)
            }
            binding.imgVideoCall.setOnClickListener {
                onClickContactOrder!!.onClickCallVideo(data)

            }
        }


    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}