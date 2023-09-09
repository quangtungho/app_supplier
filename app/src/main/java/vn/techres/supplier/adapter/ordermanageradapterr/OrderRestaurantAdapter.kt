package vn.techres.supplier.adapter.ordermanageradapterr

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemRestaurantBinding
import vn.techres.supplier.interfaces.ClickID
import vn.techres.supplier.model.datamodel.DataListOrderRestaurant

class OrderRestaurantAdapter(var context: Context) :
    RecyclerView.Adapter<OrderRestaurantAdapter.ViewHolder>() {
    private var orderRestaurantList = ArrayList<DataListOrderRestaurant>()
    private var clickID: ClickID? = null

    fun setClickID(clickID: ClickID) {
        this.clickID = clickID
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataOrderRestaurant(orderRestaurantList: ArrayList<DataListOrderRestaurant>) {
        this.orderRestaurantList = orderRestaurantList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRestaurantBinding.bind(view)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderRestaurant = orderRestaurantList[position]
        with(holder) {
            binding.restaurantName.text = orderRestaurant.restaurant_name
            binding.restaurantName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.restaurantName.marqueeRepeatLimit = -1
            binding.restaurantName.isSelected = true


            binding.restaurantEmail.text = orderRestaurant.restaurant_email
            binding.restaurantEmail.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.restaurantEmail.marqueeRepeatLimit = -1
            binding.restaurantEmail.isSelected = true

            binding.restaurantPhone.text = orderRestaurant.restaurant_phone
            binding.restaurantPhone.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.restaurantPhone.marqueeRepeatLimit = -1
            binding.restaurantPhone.isSelected = true

            binding.restaurantAddress.text = orderRestaurant.restaurant_address
            binding.restaurantAddress.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.restaurantAddress.marqueeRepeatLimit = -1
            binding.restaurantAddress.isSelected = true

            binding.totalOrder.text = orderRestaurant.total_order.toString()
            binding.viewStatus.setBackgroundResource(R.drawable.bg_order_delivering)
            binding.linearLayoutView.setBackgroundResource(R.drawable.bg_click_delivering)

            binding.linearLayoutView.setOnClickListener {
                clickID!!.clickId(orderRestaurant.restaurant_id)
            }
        }
    }

    override fun getItemCount(): Int = orderRestaurantList.size
}
