package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemGroupShareBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.ShareMessage
import vn.techres.supplier.model.datamodel.ListGroupOrder

class GroupShareMessageAdapter(var context: Context) :
    RecyclerView.Adapter<GroupShareMessageAdapter.ViewHolder>() {
    private var dataSource = ArrayList<ListGroupOrder>()
    var isShow = true
    var shareMessage: ShareMessage? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_group_share, parent, false)
        return ViewHolder(view)
    }

    @JvmName("setRoleClick1")
    fun setShareMessage(ShareMessage: ShareMessage) {
        this.shareMessage = ShareMessage
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<ListGroupOrder>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemGroupShareBinding.bind(view)
    }


    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.txtGroupName.text = dataSource[position].name

            if (isShow) {
                binding.lnShow.visibility = View.VISIBLE
            } else binding.lnShow.visibility = View.GONE
            AppUtils.callPhotoAvatar(dataSource[position].avatar_restaurant, binding.image)
            binding.image.setOnClickListener {
                AppUtils.showImageMediaSlider(dataSource[position].avatar_restaurant,
                    0)
            }

        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}