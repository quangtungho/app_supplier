package vn.techres.supplier.adapter.chatadapter.archiveadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.databinding.ItemRecycleviewTimelineBinding
import vn.techres.supplier.model.chat.data.ListFile
import vn.techres.supplier.model.chat.data.ListMessageFile

class ImageTimeLineAdapter(var context: Context) :
    RecyclerView.Adapter<ImageTimeLineAdapter.ViewHolder>() {
    private var mediaGroupAdapter: MediaGroupAdapter? = null
    private var dataSource = ArrayList<ListMessageFile>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycleview_timeline, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<ListMessageFile>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecycleviewTimelineBinding.bind(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = dataSource[position]
            binding.itemDate.text = data.time

            setAdapterImageVideo(binding.itemRecycle, data.list)
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    private fun setAdapterImageVideo(recyclerView: RecyclerView, listItem: ArrayList<ListFile>) {
        mediaGroupAdapter = MediaGroupAdapter(context)
        mediaGroupAdapter!!.setDataSource(listItem)
        recyclerView.layoutManager =
            GridLayoutManager(SupplierApplication.context,
                3,
                RecyclerView.VERTICAL,
                false)
        recyclerView.adapter = mediaGroupAdapter
    }
}