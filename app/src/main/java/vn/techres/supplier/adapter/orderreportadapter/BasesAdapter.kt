package vn.techres.supplier.adapter.orderreportadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class BasesAdapter : RecyclerView.Adapter<BaseViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (clickable()) {
            holder.convertView.isClickable = true
            holder.convertView.setOnClickListener { v -> onItemClick(v, position) }
        }
        onBindView(holder, holder.layoutPosition)
    }

    abstract fun onBindView(holder: BaseViewHolder?, position: Int)
    override fun getItemViewType(position: Int): Int {
        return getLayoutID(position)
    }

    abstract fun getLayoutID(position: Int): Int
    abstract fun clickable(): Boolean
    open fun onItemClick(v: View?, position: Int) {}
}