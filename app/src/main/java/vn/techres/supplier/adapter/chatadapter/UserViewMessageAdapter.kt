package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemUserViewMessageBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.model.chat.data.MessageView

class UserViewMessageAdapter(var context: Context) :
    RecyclerView.Adapter<UserViewMessageAdapter.ViewHolder>() {
    private var dataSource = ArrayList<MessageView>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_view_message, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<MessageView>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemUserViewMessageBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = dataSource[position]
            AppUtils.callPhotoAvatar(data.avatar, binding.imvAvatar)
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}