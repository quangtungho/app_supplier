package vn.techres.supplier.helper

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.paginate.recycler.LoadingListItemCreator
import vn.techres.supplier.R

class CustomLoadingListItemCreator(var view: RecyclerView) : LoadingListItemCreator {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.custom_load_more_news_feed, parent, false)
        return VH(view)
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        if (view.layoutManager is StaggeredGridLayoutManager) {
            val params = vh.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            params.isFullSpan = true
        }
    }

    internal class VH(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    )
}