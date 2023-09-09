package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemListImageClipBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.ClickImageClip

class ImageClipAdapter (var context: Context) : RecyclerView.Adapter<ImageClipAdapter.ViewHolder>(){
    private var dataSource = ArrayList<String>()
    private var clickImageClip: ClickImageClip? = null

    fun setClickImageClip(clickImageClip: ClickImageClip) {
        this.clickImageClip = clickImageClip
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_user_tag, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<String>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemListImageClipBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = dataSource[position]
            AppUtils.callPhotoLocal(data, binding.imageCLip)
            binding.icnImageCLipDetele.setOnClickListener { clickImageClip?.clickDelete(position) }
            binding.imageCLip.setOnClickListener { clickImageClip?.clickImage(position) }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}