package vn.techres.supplier.adapter.chatadapter.archiveadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.databinding.ItemRecycleviewTimelineBinding
import vn.techres.supplier.model.chat.data.ListFile
import vn.techres.supplier.model.chat.data.ListMessageFile

class FileTimeLineAdapter(var context: Context) :
    RecyclerView.Adapter<FileTimeLineAdapter.ViewHolder>() {
    private var fileGroupAdapter: FileGroupAdapter? = null
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
        fileGroupAdapter = FileGroupAdapter(context)
        fileGroupAdapter!!.setDataSource(listItem)
        recyclerView.layoutManager =
            LinearLayoutManager(SupplierApplication.context)
        recyclerView.adapter = fileGroupAdapter
    }
}