package vn.techres.supplier.adapter.notificationadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemNotificationBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.model.entity.CurrentConfigJava
import vn.techres.supplier.model.datamodel.DataNotification

class NotificationAdapter(var context: Context) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private var dataNotification = DataNotification()

    private var configJs = CurrentConfigJava.getConfigJava(context)

    @SuppressLint("NotifyDataSetChanged")
    fun setDataNotification(dataSourceOn: DataNotification) {
        this.dataNotification = dataSourceOn
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemNotificationBinding.bind(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = dataNotification.list[position]
        with(holder) {
            binding.tvNotification.text =
                notification.name + " " + notification.content

            binding.tvNotification.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.tvNotification.marqueeRepeatLimit = -1
            binding.tvNotification.isSelected = true
            binding.txtTime.text = notification.created_at
            AppUtils.callPhoto(notification.avatar, binding.Avatar)
        }
    }

    override fun getItemCount(): Int {
        return dataNotification.list.size
    }
}