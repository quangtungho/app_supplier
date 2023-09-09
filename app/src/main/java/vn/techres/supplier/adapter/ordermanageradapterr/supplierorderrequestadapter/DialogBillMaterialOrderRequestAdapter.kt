package vn.techres.supplier.adapter.ordermanageradapterr.supplierorderrequestadapter

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
import vn.techres.supplier.databinding.ItemTableDetailMaterialOrderConfirmBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.datamodel.DataBillOrderMaterial
import java.text.DecimalFormat
import java.util.*

class DialogBillMaterialOrderRequestAdapter(var context: Context) :
        RecyclerView.Adapter<DialogBillMaterialOrderRequestAdapter.ViewHolder>() {
    private var billOrderList = ArrayList<DataBillOrderMaterial>()
    private var clickRecyclerDelete: OnClickTransID? = null

    fun setClickRecyclerDelete(clickRecyclerDelete: OnClickTransID) {
        this.clickRecyclerDelete = clickRecyclerDelete
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setDataListOrder(dataListBillOrder: ArrayList<DataBillOrderMaterial>) {
        this.billOrderList = dataListBillOrder
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_table_detail_material_order_confirm, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTableDetailMaterialOrderConfirmBinding.bind(view)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    private fun formatFloatToString(f: Float): String {
        val i = f.toInt()
        return if (f == i.toFloat()) i.toString() else f.toString()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderBillOrder = billOrderList[position]
        with(holder) {
            binding.linearOutOfStock.visibility = View.VISIBLE
            binding.txtNumberCount.text = (position + 1).toString()//stt
            binding.txtNumberCount.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNumberCount.marqueeRepeatLimit = -1
            binding.txtNumberCount.isSelected = true

            binding.txtNameMaterial.text = orderBillOrder.supplier_material_name//ten nguyen lieu
            binding.txtNameMaterial.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtNameMaterial.marqueeRepeatLimit = -1
            binding.txtNameMaterial.isSelected = true


            binding.txtUnit.text = orderBillOrder.supplier_unit_name//don vi
            binding.txtUnit.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.txtUnit.marqueeRepeatLimit = -1
            binding.txtUnit.isSelected = true

            binding.unitPriceInput.text =
                    Editable.Factory.getInstance()
                            .newEditable(currencyFormat(orderBillOrder.retail_price!!.toFloat().toString()))


            if (orderBillOrder.status == 5) {
                orderBillOrder.quantity_input = orderBillOrder.supplier_quantity

                binding.txtNumberOfOrders.text =
                        formatFloatToString(orderBillOrder.quantity)//so luong dat hang = quantity
                binding.txtNumberOfOrders.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.txtNumberOfOrders.marqueeRepeatLimit = -1
                binding.txtNumberOfOrders.isSelected = true

                binding.inputNumberOfOrders.text =
                        Editable.Factory.getInstance()
                                .newEditable(formatFloatToString(orderBillOrder.supplier_quantity!!))//so luong giao = quantity

                binding.total.text =
                        currencyFormat(orderBillOrder.supplier_total_amount.toString())//thanh tien
                binding.total.ellipsize = TextUtils.TruncateAt.MARQUEE
                binding.total.marqueeRepeatLimit = -1
                binding.total.isSelected = true
            } else {
                orderBillOrder.quantity_input = orderBillOrder.quantity
                binding.txtNumberOfOrders.text =
                        formatFloatToString(orderBillOrder.quantity)//so luong dat hang = quantity
                binding.inputNumberOfOrders.text =
                        Editable.Factory.getInstance()
                                .newEditable(formatFloatToString(orderBillOrder.quantity))//so luong giao = quantity
                binding.total.text =
                        currencyFormat(orderBillOrder.total_amount.toString()) // thanh tien
            }
            if (orderBillOrder.quantity == 0f)
                binding.linearOutOfStock.visibility = View.GONE
            else
                binding.linearOutOfStock.visibility = View.VISIBLE


            binding.dateCreate.text = orderBillOrder.created_at
            binding.dateCreate.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.dateCreate.marqueeRepeatLimit = -1
            binding.dateCreate.isSelected = true
            // thoi gian tao
            binding.linearOutOfStock.setOnClickListener {
                binding.inputNumberOfOrders.text = Editable.Factory.getInstance()
                        .newEditable(orderBillOrder.out_of_stock.toString())
                binding.linearOutOfStock.visibility = View.GONE
                binding.linearEdit.visibility = View.GONE
                binding.btnEditAndBack.visibility = View.VISIBLE
                binding.outOfStock.visibility = View.VISIBLE
                binding.linearEditPrice.visibility = View.GONE
                binding.linearPrice.visibility = View.VISIBLE

            }
            binding.btnEditAndBack.setOnClickListener {
                binding.inputNumberOfOrders.text = Editable.Factory.getInstance()
                        .newEditable(orderBillOrder.out_of_stock.toString())
                binding.linearOutOfStock.visibility = View.VISIBLE
                binding.linearEdit.visibility = View.VISIBLE
                binding.btnEditAndBack.visibility = View.GONE
                binding.outOfStock.visibility = View.GONE
                binding.linearEditPrice.visibility = View.VISIBLE
                binding.linearPrice.visibility = View.GONE
            }

            orderBillOrder.price_reality_input = orderBillOrder.retail_price!!.toFloat()
            binding.inputNumberOfOrders.addTextChangedListener(object : TextWatcher {
                @SuppressLint("SetTextI18n")
                override fun afterTextChanged(s: Editable?) {
                    if (binding.inputNumberOfOrders.text.isNotEmpty() && s!!.isNotEmpty()) {
                        orderBillOrder.quantity_input = s.toString().toFloat()
                    } else
                        orderBillOrder.quantity_input = 0f
                    var totalMoneyQuatity = 0.0
                    totalMoneyQuatity =
                            (orderBillOrder.quantity_input!!.toFloat() * orderBillOrder.price_reality_input!!.toFloat()).toDouble()
                    binding.total.text = currencyFormat(totalMoneyQuatity.toString())
//                    if (value.startsWith(".")) {
//                        binding.inputNumberOfOrders.setText("0.");
//                    }
//                    if (value.startsWith("0") && !value.startsWith("0.")) {
//                        binding.inputNumberOfOrders.setText("");
//                    }
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
            binding.inputNumberOfOrders.setSelection(binding.inputNumberOfOrders.text.length)

            binding.unitPriceInput.addTextChangedListener(object : TextWatcher {
                @SuppressLint("SetTextI18n")
                override fun afterTextChanged(s: Editable?) {
                    if (Objects.requireNonNull(binding.unitPriceInput.text).toString().isNotEmpty()) {
                        binding.unitPriceInput.removeTextChangedListener(this)
                        if (s!!.isNotEmpty()) {
                            binding.unitPriceInput.setText(AppUtils.getDecimalFormattedString(binding.unitPriceInput.text.toString().replace(",", "")))
                        } else {
                            binding.unitPriceInput.setText("")
                        }
                        if (binding.unitPriceInput.text.toString().replace(",", "").length > 9) {
                            binding.unitPriceInput.setText("1,000,000,000")
                        }
                        binding.unitPriceInput.addTextChangedListener(this)
                        binding.unitPriceInput.setSelection(binding.unitPriceInput.text.length)
                    }
                    if (binding.unitPriceInput.text.toString().isNotEmpty() && s!!.isNotEmpty()) {
                        orderBillOrder.price_reality_input =
                                AppUtils.trimCommaOfString(s.toString()).toFloat()

                    } else
                        orderBillOrder.price_reality_input = 0f

                    var totalMoney = 0.0
                    totalMoney =
                            (orderBillOrder.quantity_input!!.toFloat() * orderBillOrder.price_reality_input!!.toFloat()).toDouble()
                    binding.total.text = currencyFormat(totalMoney.toString())
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
            binding.unitPriceInput.setSelection(binding.unitPriceInput.length())
        }
    }

    override fun getItemCount() = billOrderList.size

}
