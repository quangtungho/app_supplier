package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.model.datamodel.DataGroupOrder


class SelectEmployeeAdapter(var context: Context) :
    RecyclerView.Adapter<SelectEmployeeAdapter.ViewHolder>() {
    private var dataSource = DataGroupOrder()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_select_employee_default, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: DataGroupOrder) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        val image: CircleImageView = itemView.findViewById(R.id.image)
//
//        val txtGroupName: TechResTextView = itemView.findViewById(R.id.txtGroupName)
//
//        val time: TechResTextView = itemView.findViewById(R.id.time)
//
//        val message: AXEmojiTextView = itemView.findViewById(R.id.message)
//
//        val logTag: ImageView = itemView.findViewById(R.id.logTag)
//
//        val lnCount: LinearLayout = itemView.findViewById(R.id.lnCount)
//        val count: TechResTextView = itemView.findViewById(R.id.count)
//
//
//        val lnClick: LinearLayout = itemView.findViewById(R.id.lnClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemEmployee = dataSource

//        AppUtils.callPhotoAvatar(itemEmployee.avatar, holder.image)
//
//        holder.txtGroupName.text = itemEmployee.name
//        holder.message.text = itemEmployee.last_message
//        holder.time.text = itemEmployee.created_last_message
//
//
//        holder.lnClick.setOnClickListener {
//            val intent = Intent(
//                holder.lnClick.context,
//                ChatActivity::class.java
//            )
////            intent.putExtra(TechresEnum.ID_GROUP.toString(), chatItem._id)
////            intent.putExtra(TechresEnum.GROUP_TYPE.toString(), data.getConversation_type())
//            startActivity(context, intent, null)
//        }
    }

    override fun getItemCount(): Int {
        return dataSource.list.size
    }
}

