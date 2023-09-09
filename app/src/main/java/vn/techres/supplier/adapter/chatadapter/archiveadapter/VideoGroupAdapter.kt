package vn.techres.supplier.adapter.chatadapter.archiveadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemMediaVideoBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.model.chat.data.ListFile

class VideoGroupAdapter(var context: Context) :
    RecyclerView.Adapter<VideoGroupAdapter.ViewHolder>() {
    private var dataSource = ArrayList<ListFile>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_media_video, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<ListFile>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMediaVideoBinding.bind(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = dataSource[position]

            AppUtils.callPhoto(data.link_original, binding.thumbnail)

            val listImage: ArrayList<String> = ArrayList()
            for (i in 0 until dataSource.size) {
                listImage.add(dataSource[i].link_original)
            }

            itemView.setOnClickListener {
                AppUtils.showImageMediaSlider(listImage,
                    position)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

}