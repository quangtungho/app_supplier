package vn.techres.supplier.adapter.ordermanageradapterr.supplierorderadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.techres.supplier.R
import vn.techres.supplier.databinding.ItemTableDetailMaterialOrderEditBinding
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.datamodel.DataListMatialOrder
import java.math.BigDecimal
import java.text.DecimalFormat

class DialogBillMaterialOrderEditAdapter(var context: Context) :
    RecyclerView.Adapter<DialogBillMaterialOrderEditAdapter.ViewHolder>() {
    private var billOrderList = ArrayList<DataListMatialOrder>()
    private var clickRecyclerDelete: OnClickTransID? = null

    fun setClickRecyclerDelete(clickRecyclerDelete: OnClickTransID) {
        this.clickRecyclerDelete = clickRecyclerDelete
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListOrder(dataListBillOrder: ArrayList<DataListMatialOrder>) {
        this.billOrderList = dataListBillOrder
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_table_detail_material_order_edit, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableDetailMaterialOrderEditBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    private fun formatFloatToString(f: Float): String {
        val i = f.toInt()
        return if (f == i.toFloat()) i.toString() else f.toString()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderBillOrder = billOrderList[position]
        with(holder) {
            binding.linearOutOfStock.visibility = View.VISIBLE
            binding.linearEditPrice.visibility = View.VISIBLE
            binding.viewNumberPrice.visibility = View.VISIBLE

            binding.txtNumberCount.text = (position + 1).toString()//stt

            binding.txtNameMaterial.text = orderBillOrder.supplier_material_name//nguyen lieu
            binding.txtNameMaterial.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameMaterial.marqueeRepeatLimit = -1
            binding.txtNameMaterial.isSelected = true

            binding.txtUnit.text = orderBillOrder.supplier_unit_name//don vi
            binding.txtUnit.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtUnit.marqueeRepeatLimit = -1
            binding.txtUnit.isSelected = true

            if (orderBillOrder.total_quantity == 0f) {
                binding.linearOutOfStock.visibility = View.GONE
                binding.btnEditAndBack.visibility = View.VISIBLE

            } else {
                binding.linearOutOfStock.visibility = View.VISIBLE
                binding.btnEditAndBack.visibility = View.GONE
            }

            binding.txtNumberOfOrders.text =
                formatFloatToString(orderBillOrder.total_quantity!!.toFloat())
            binding.txtNumberCount.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNumberCount.marqueeRepeatLimit = -1
            binding.txtNumberCount.isSelected = true

            binding.inputNumberOfOrders.text = Editable.Factory.getInstance()
                .newEditable(formatFloatToString(orderBillOrder.total_quantity!!))

            binding.inputTotailPrice.text = Editable.Factory.getInstance()
                .newEditable(currencyFormat(orderBillOrder.price_reality.toString()))


            binding.unitPrice.text =
                currencyFormat(orderBillOrder.price_reality.toString())// đơn giá


            binding.total.text =
                currencyFormat(orderBillOrder.total_price_reality.toString())// thành tiền

            binding.dateCreate.text = orderBillOrder.created_at// ngày tạo

            //total_quantity
            orderBillOrder.quantity_input = orderBillOrder.total_quantity!!.toFloat()

            binding.linearOutOfStock.setOnClickListener {
                binding.inputNumberOfOrders.text = Editable.Factory.getInstance()
                    .newEditable(orderBillOrder.out_of_stock.toString())
                binding.linearOutOfStock.visibility = View.GONE
                binding.linearEdit.visibility = View.GONE
                binding.btnEditAndBack.visibility = View.VISIBLE
                binding.outOfStockPrice.visibility = View.VISIBLE
                binding.linearEditPrice.visibility = View.GONE
                binding.outOfStock.visibility = View.VISIBLE

            }

            binding.btnEditAndBack.setOnClickListener {
                binding.inputNumberOfOrders.text = Editable.Factory.getInstance()
                    .newEditable(orderBillOrder.out_of_stock.toString())
                binding.linearOutOfStock.visibility = View.VISIBLE
                binding.linearEdit.visibility = View.VISIBLE
                binding.btnEditAndBack.visibility = View.GONE
                binding.outOfStockPrice.visibility = View.GONE
                binding.linearEditPrice.visibility = View.VISIBLE
                binding.outOfStock.visibility = View.GONE

            }


            binding.inputNumberOfOrders.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (binding.inputNumberOfOrders.text.isNotEmpty() && s?.isNotEmpty() == true) {
                        orderBillOrder.quantity_input = s.toString().toFloat()
                        orderBillOrder.price_reality_input = s.toString().toBigDecimal()
                    } else
                        orderBillOrder.quantity_input = 0f
                    orderBillOrder.price_reality_input = BigDecimal.ZERO
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })


            //price_reality
            orderBillOrder.price_reality_input = orderBillOrder.price_reality
            binding.inputTotailPrice.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (binding.inputTotailPrice.text.isNotEmpty() && s?.isNotEmpty() == true) {
                        orderBillOrder.price_reality_input = s.toString().toBigDecimal()
                    } else
                        orderBillOrder.price_reality_input = BigDecimal.ZERO
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
//        edit controll vi tri cuoi cung
            binding.inputNumberOfOrders.setSelection(binding.inputNumberOfOrders.text.length)
            binding.inputTotailPrice.setSelection(binding.inputTotailPrice.text.length)
        }
    }

    override fun getItemCount(): Int = billOrderList.size

}
