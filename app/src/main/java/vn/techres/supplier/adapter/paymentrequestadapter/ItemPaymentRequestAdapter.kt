package vn.techres.supplier.adapter.paymentrequestadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.databinding.ItemMemberNotificationSettingBinding
import vn.techres.supplier.databinding.ItemPaymentRequestBinding
import vn.techres.supplier.databinding.ItemRequestPaymentBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.PaymentClick
import vn.techres.supplier.interfaces.RoleClick
import vn.techres.supplier.model.datamodel.DataListEmployee
import vn.techres.supplier.model.datamodel.DataListOrder
import java.text.DecimalFormat

class ItemPaymentRequestAdapter(var context: Context) :
    RecyclerView.Adapter<ItemPaymentRequestAdapter.ViewHolder>() {
    private var dataSource = ArrayList<DataListOrder>()
    private var paymentClick: PaymentClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_payment_request, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<DataListOrder>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    @JvmName("setRoleClick1")
    fun setPaymentClick(paymentClick: PaymentClick) {
        this.paymentClick = paymentClick
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemPaymentRequestBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = dataSource[position]
            binding.txtCode.text = data.code
            binding.txtNameRestaurant.text = data.restaurant_name
            binding.txtBranchName.text = data.branch_name
            binding.txtTotal.text = currencyFormat(data.total_amount.toString())
            binding.txtDateCreate.text = data.created_at.toString()

            if (data.is_join == 1) {
                binding.tvMemberJoined.visibility = View.VISIBLE
                binding.imvCheckJoin.visibility = View.GONE
            } else {
                binding.imvCheckJoin.visibility = View.VISIBLE
                binding.tvMemberJoined.visibility = View.GONE
            }

            binding.lnDialog.setOnClickListener {
                if (data.is_join == 1){
                    data.is_join = 0
                    binding.tvMemberJoined.visibility = View.GONE
                    binding.imvCheckJoin.visibility = View.VISIBLE

                }else{
                    data.is_join = 1
                    binding.tvMemberJoined.visibility = View.VISIBLE
                    binding.imvCheckJoin.visibility = View.GONE
                }
                paymentClick!!.onUpdate(dataSource)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}