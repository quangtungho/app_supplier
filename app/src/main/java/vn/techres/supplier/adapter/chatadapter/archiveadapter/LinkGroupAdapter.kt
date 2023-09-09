package vn.techres.supplier.adapter.chatadapter.archiveadapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.databinding.ItemLinkBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.model.chat.data.ListFile

class LinkGroupAdapter(var context: Context) :
    RecyclerView.Adapter<LinkGroupAdapter.ViewHolder>() {
    private var dataSource = ArrayList<ListFile>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_link, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<ListFile>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemLinkBinding.bind(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = dataSource[position]

            binding.txtLink.setText(data.cannonical_url)
            binding.txtNameLink.setText(data.title)
            try {
                AppUtils.callPhotoLocal(data.media_thumb, binding.imgLink)
            } catch (e: Exception) {
                e.stackTrace
            }
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.data = Uri.parse(data.cannonical_url)
                SupplierApplication.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

}