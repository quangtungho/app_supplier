package vn.techres.supplier.adapter.unitsadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemSpinnerSpecificationBinding
import vn.techres.supplier.interfaces.OnClickRefreshRecyclerView

class SpinnerSpecificationInUnitsAdapter(var context: Context) :
        RecyclerView.Adapter<SpinnerSpecificationInUnitsAdapter.ViewHolder>() {

    private var dataList = ArrayList<String>()
    private var clickRecycler: OnClickRefreshRecyclerView? = null

    fun setClickRecycler(clickRecycler: OnClickRefreshRecyclerView) {
        this.clickRecycler = clickRecycler
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: ArrayList<String>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
                LayoutInflater.from(context).inflate(R.layout.item_spinner_specification, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSpinnerSpecificationBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (dataList[position] != null) {
            val currentItem = dataList[position]
            with(holder) {
                binding.txtSpinnerData.text = currentItem
                binding.btnRemove.setOnClickListener {
                    clickRecycler!!.onClick(position)
                }
            }
        }

    }


    override fun getItemCount(): Int {
        return dataList.size
    }

}