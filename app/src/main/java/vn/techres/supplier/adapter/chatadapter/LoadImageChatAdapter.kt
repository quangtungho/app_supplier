package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemLoadImageChatBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.model.chat.data.FileChat


class LoadImageChatAdapter (var context: Context) : RecyclerView.Adapter<LoadImageChatAdapter.ViewHolder>(){
    private var dataSource = ArrayList<FileChat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_image_chat, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<FileChat>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemLoadImageChatBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = dataSource[position]
            val lp = binding.imgChat.layoutParams
            if (lp is FlexboxLayoutManager.LayoutParams) {
                lp.flexGrow = 1f
            }
            if (data.link_original!!.contains("public/")) {
                AppUtils.callPhoto(data.link_original, binding.imgChat)
            } else {
                AppUtils.callPhotoLocal(data.link_original, binding.imgChat)
            }

            itemView.setOnClickListener {
                AppUtils.showImageMediaSliderFileChat(
                    dataSource,
                    position
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}