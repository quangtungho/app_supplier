package vn.techres.supplier.fragment.ordermanager.orderbilldetail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.ordermanageradapterr.supplierorderrequestadapter.DialogBillMaterialOrderRequestAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOrderDetailBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.Currency
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.datamodel.DataBillOrderMaterial
import vn.techres.supplier.model.datamodel.DataDetailBillOrder
import vn.techres.supplier.model.datamodel.DataListMaterial
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.ConfirmBillOrderParams
import vn.techres.supplier.model.params.StatusBillOrderParams
import vn.techres.supplier.model.response.BillOrderMaterialResponse
import vn.techres.supplier.model.response.DetailBillOrderResponse
import vn.techres.supplier.model.response.OrderBillResponse
import vn.techres.supplier.model.response.OrderResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderBillDetailFragment :
        BaseBindingFragment<FragmentOrderDetailBinding>(FragmentOrderDetailBinding::inflate),
        OnClickTransID {
    val tagName: String = OrderBillDetailFragment::class.java.name
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null

    // adapter cofirm
    private var adapter: DialogBillMaterialOrderRequestAdapter? = null

    //Lấy danh sách nguyên liệu theo phiếu đặt hàng từ kế toán
    private var listBillOrderMaterial = ArrayList<DataBillOrderMaterial>()
    private var dataMaterial = ArrayList<DataListMaterial>()

    //Lấy chi tiết phiếu yêu cầu đơn hàng của kế toán lên nhà cung cấp
    private var dataBillOrder = DataDetailBillOrder()
    var position = 0
    var iStatus = 0
    var iSize = 0
    private var idOrderBill: Int = 0
    var deliveryDate = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constraintLayoutMain.startAnimation(animFragment)
        binding!!.linearLayoutBtn.visibility = View.VISIBLE
        binding!!.txtTotailMaterial.visibility = View.GONE
        binding!!.totalmaterial.visibility = View.GONE
        binding!!.btnDeleteOrder.visibility = View.VISIBLE
        binding!!.viewTotalPrice.visibility = View.VISIBLE
        binding!!.totalPrice.visibility = View.VISIBLE
        binding!!.numberOfOrders.visibility = View.VISIBLE
        binding!!.linearTotail.visibility = View.VISIBLE
        binding!!.viewNumberOfOrders.visibility = View.VISIBLE
        binding!!.lnSumOrder.visibility = View.VISIBLE
        binding!!.function.visibility = View.VISIBLE
        binding!!.viewReturnsOrder.visibility = View.VISIBLE
        binding!!.txtCode.visibility = View.GONE
        binding!!.code.visibility = View.GONE
        binding!!.txtDiscount.visibility = View.GONE
        binding!!.discount.visibility = View.GONE
        binding!!.txtVat.visibility = View.GONE
        binding!!.vat.visibility = View.GONE
        binding!!.linearLayoutBtn.visibility = View.GONE
        idOrderBill = cacheManager.get(TechresEnum.GET_BY_ID.toString())!!.toInt()
        iSize = cacheManager.get(TechresEnum.GET_SIZE.toString())!!.toInt()
        iStatus = cacheManager.get(TechresEnum.STATUS.toString())!!.toInt()
        mainActivity!!.setHeader(getString(R.string.order_details))
        mainActivity!!.setBackClick(true)
        getAPIDetailBillOrder()
        getAPIBillOrderMaterial()

        setClick()
        mDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            var month = month
            month += 1
            deliveryDate = "$day/$month/$year"
            binding!!.btnDate.text = deliveryDate
        }
    }

    private fun setClick() {
        if (iStatus == 5) {
            binding!!.btnAcceptOrder.visibility = View.GONE
            binding!!.btnEditOrder.visibility = View.VISIBLE
            binding!!.btnAcceptOrder.setOnClickListener {
                binding!!.btnEditOrder.isEnabled = false
                Handler().postDelayed({ binding!!.btnEditOrder.isEnabled = true }, 3000)
                binding!!.btnAcceptOrder.showLoading()
                apiConfirmBillOrder(deliveryDate)
            }
        } else {
            binding!!.btnAcceptOrder.visibility = View.VISIBLE
            binding!!.btnEditOrder.visibility = View.GONE
            binding!!.btnAcceptOrder.setOnClickListener {
                binding!!.btnAcceptOrder.isEnabled = false
                Handler().postDelayed({ binding!!.btnEditOrder.isEnabled = true }, 3000)
                binding!!.btnAcceptOrder.showLoading()
                apiConfirmBillOrder(deliveryDate)
            }
        }
        binding!!.btnDeleteOrder.setOnClickListener {
            btnDeleteOrder()
        }
        binding!!.btnDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH]
            val day = cal[Calendar.DAY_OF_MONTH]
            val dialogDate = DatePickerDialog(
                    mainActivity!!,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day
            )
            dialogDate.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogDate.show()
        }
        binding!!.editDiscounts.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding!!.editDiscounts.text.isNotEmpty() && binding!!.editDiscounts.text.toString()
                                .toFloat() > 100
                ) {
                    Toast.makeText(
                            mainActivity!!,
                            getString(R.string.toast_edittext_discount_percent_undecent),
                            Toast.LENGTH_SHORT
                    ).show()
                    binding!!.editDiscounts.setText("100")
                    binding!!.editDiscounts.setSelection(s!!.length)
                } else {
                    if (binding!!.editDiscounts.text.isNotEmpty() && binding!!.editTaxVat.text.isNotEmpty() && s?.isNotEmpty() == true) {
                        val totalOnDiscount =
                                (dataBillOrder.total_amount
                                        ?: 0.0) - (binding!!.editDiscounts.text.toString()
                                        .toFloat()
                                        * (dataBillOrder.total_amount ?: 0.0))
                        val totalOnDiscountAndVat =
                                totalOnDiscount - binding!!.editTaxVat.text.toString()
                                        .toFloat() * totalOnDiscount
                        binding!!.totailBillOrder.text = totalOnDiscountAndVat.toString()
                    } else if (binding!!.editDiscounts.text.isNotEmpty()) {
                        val totalOnDiscount =
                                (dataBillOrder.total_amount ?: 0.0) - (dataBillOrder.total_amount
                                        ?: 0.0 * binding!!.editDiscounts.text.toString().toFloat())
                        binding!!.totailBillOrder.text = totalOnDiscount.toString()
                    } else if (binding!!.editTaxVat.text.isNotEmpty()) {
                        val totalOnVat =
                                (dataBillOrder.total_amount
                                        ?: 0.0) - (binding!!.editTaxVat.text.toString()
                                        .toFloat() *
                                        (dataBillOrder.total_amount ?: 0.0))
                        binding!!.totailBillOrder.text = totalOnVat.toString()
                    } else {
                        binding!!.totailBillOrder.text =
                                (dataBillOrder.total_amount ?: 0.0).toString()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding!!.editTaxVat.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding!!.editTaxVat.text.isNotEmpty() && binding!!.editTaxVat.text.toString()
                                .toFloat() > 100
                ) {
                    Toast.makeText(
                            mainActivity!!,
                            getString(R.string.toast_edittext_vat_undecent),
                            Toast.LENGTH_SHORT
                    ).show()
                    binding!!.editTaxVat.setText("100")
                    binding!!.editTaxVat.setSelection(s!!.length)
                } else {
                    if (binding!!.editTaxVat.text.isNotEmpty() && binding!!.editTaxVat.text.isNotEmpty() && s?.isNotEmpty() == true) {
                        val totalOnDiscount =
                                (dataBillOrder.total_amount
                                        ?: 0.0) - (binding!!.editDiscounts.text.toString()
                                        .toFloat()
                                        * (dataBillOrder.total_amount ?: 0.0))
                        val totalOnDiscountAndVat =
                                totalOnDiscount - binding!!.editTaxVat.text.toString()
                                        .toFloat() * totalOnDiscount
                        binding!!.totailBillOrder.text = totalOnDiscountAndVat.toString()
                    } else if (binding!!.editDiscounts.text.isNotEmpty()) {
                        val totalOnDiscount =
                                (dataBillOrder.total_amount ?: 0.0) - (dataBillOrder.total_amount
                                        ?: 0.0 * binding!!.editDiscounts.text.toString().toFloat())
                        binding!!.totailBillOrder.text = totalOnDiscount.toString()
                    } else if (binding!!.editTaxVat.text.isNotEmpty()) {
                        val totalOnVat =
                                (dataBillOrder.total_amount
                                        ?: 0.0) - (binding!!.editTaxVat.text.toString()
                                        .toFloat() *
                                        (dataBillOrder.total_amount ?: 0.0))
                        binding!!.totailBillOrder.text = totalOnVat.toString()
                    } else {
                        binding!!.totailBillOrder.text =
                                (dataBillOrder.total_amount ?: 0.0).toString()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun setAdapter() {
        adapter = DialogBillMaterialOrderRequestAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)
        binding!!.recyclerView.setHasFixedSize(true)
        adapter?.setClickRecyclerDelete(this)
    }

    //------------CHỨC NĂNG-----------//
    //BTN HUY
    private fun btnDeleteOrder() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_reason_cancel)
        val window = dialog.window
        window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val edtInputNote: EditText = dialog.findViewById(R.id.edtInputNote)
        val btnYes: Button = dialog.findViewById(R.id.yes)
        val btnNo: Button = dialog.findViewById(R.id.no)
        btnYes.setOnClickListener {
            val inputNote = edtInputNote.text.toString()
            //goi api
            changeBillOrder(TechresEnum.STATUS_3.toString().toInt(), inputNote)
            dialog.dismiss()

        }
        btnNo.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }

    private fun formatFloatToString(f: Float): String {
        val i = f.toInt()
        return if (f == i.toFloat()) i.toString() else f.toString()
    }

    //BTN BACK
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }

    //__________________API DU LIEU________________//
//API CHI TIET NGUYEN LIEU CONFIRM
    private fun getAPIBillOrderMaterial() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-order-request/$idOrderBill/supplier-order-request-detail"
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getBillOrderMaterial(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BillOrderMaterialResponse> {
                    override fun onComplete() {}
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: BillOrderMaterialResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listBillOrderMaterial = response.data
                            adapter!!.setDataListOrder(listBillOrderMaterial)
                            binding!!.linearLayoutBtn.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }

    //API CHI TIET DON HANG COFIRM
    private fun getAPIDetailBillOrder() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-order-request/$idOrderBill"
        ServiceFactory.createRetrofitService(TechResService::class.java, requireActivity())
                .getDetailBillOrder(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DetailBillOrderResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: DetailBillOrderResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            dataBillOrder = response.data
                            binding!!.linearLayoutBtn.visibility = View.VISIBLE
                            binding!!.nameRestaurant.text = dataBillOrder.restaurant_name
                            binding!!.nameBrand.text = dataBillOrder.restaurant_brand_name
                            binding!!.nameBranch.text = dataBillOrder.branch_name
                            binding!!.code.text = dataBillOrder.code
                            if (dataBillOrder.status == 5) {
                                binding!!.editDiscounts.text = Editable.Factory.getInstance()
                                        .newEditable(formatFloatToString(dataBillOrder.supplier_discount_percent!!))
                                binding!!.editTaxVat.text = Editable.Factory.getInstance()
                                        .newEditable(formatFloatToString(dataBillOrder.supplier_vat!!))

                                binding!!.totalMoney.text = Currency.formatCurrencyDecimal(
                                        dataBillOrder.supplier_total_amount!!.toFloat()
                                ) //tong don

                                binding!!.total.text = Currency.formatCurrencyDecimal(
                                        dataBillOrder.supplier_total_amount!!.toFloat()
                                ) // thanh toan

                                binding!!.totalmaterial.text =
                                        formatFloatToString(dataBillOrder.supplier_quantity!!.toFloat())//tong nguyen liệu
                            } else {
                                binding!!.totalMoney.text = Currency.formatCurrencyDecimal(
                                        dataBillOrder.total_amount!!.toFloat()
                                ) //tong tien

                                binding!!.total.text = Currency.formatCurrencyDecimal(
                                        dataBillOrder.total_amount!!.toFloat()
                                ) // thanh toan

                                binding!!.totalmaterial.text =
                                        formatFloatToString(dataBillOrder.quantity!!.toFloat())//tong nguyen liệu
                            }

                            binding!!.deliveryDate.text = dataBillOrder.expected_delivery_time
                            binding!!.btnDate.text = dataBillOrder.expected_delivery_time
                            deliveryDate = binding!!.btnDate.text.toString()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    //____________API CHUC NANG_____________//
    //API ĐÔI TRẠNG THÁI SANG HUY PHIEU
    private fun changeBillOrder(status: Int, inputNote: String) {
        val params = StatusBillOrderParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.params.status = status
        params.params.reason = inputNote
        params.request_url = "/api/supplier-order-request/$idOrderBill/change-status"
        ServiceFactory.createRetrofitService(TechResService::class.java, requireActivity())
                .getChangeCanelledBillOrder(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<OrderBillResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: OrderBillResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            mainActivity!!.supportFragmentManager.popBackStack()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    //API  COFIRM DON HANG
    private fun apiConfirmBillOrder(deliveryDate: String) {
        val params = ConfirmBillOrderParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-orders/confirm"
        params.params.supplier_order_request_id = idOrderBill
        params.params.restaurant_material_order_request_id =
                dataBillOrder.restaurant_material_order_request_id
        params.params.expected_delivery_time = deliveryDate
        params.params.vat = binding!!.editTaxVat.text.toString().toFloat()
        params.params.discount_percent = binding!!.editDiscounts.text.toString().toFloat()
        for (i in listBillOrderMaterial.iterator()) {
            dataMaterial.add(
                    DataListMaterial(
                            listBillOrderMaterial[position].supplier_material_id!!,
                            listBillOrderMaterial[position].quantity_input!!,
                            listBillOrderMaterial[position].price_reality_input!!
                    )
            )
            position += 1
        }
//
//        for (i in listBillOrderMaterial.indices) {
//            dataMaterial.add(DataListMaterial(listBillOrderMaterial[i].supplier_material_id?:0,
//                    listBillOrderMaterial[position].quantity_input?:0f,
//                    listBillOrderMaterial[position].price_reality_input?:0f))
//        }

        params.params.list_material = dataMaterial
        ServiceFactory.createRetrofitService(TechResService::class.java, requireActivity())
                .ConfirmBillOrder(params)
                .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<OrderResponse> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: OrderResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            if (iSize == 0) {
                                binding!!.btnAcceptOrder.hideLoading()
                                mainActivity!!.supportFragmentManager.popBackStack()
                                cacheManager.put(
                                        TechresEnum.KEY_TAB_ORDER_BILL.toString(),
                                        "0"
                                )

                            } else {
                                binding!!.btnAcceptOrder.hideLoading()
                                mainActivity!!.supportFragmentManager.popBackStack()
                            }
                            Toast.makeText(
                                    requireActivity(),
                                    "Xác nhận phiếu thành công",
                                    Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.message.equals("Phiếu này đang được lên đơn hàng!")
                        ) {
                            binding!!.btnAcceptOrder.hideLoading()
                            mainActivity!!.supportFragmentManager.popBackStack()
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {}
                })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(position: Int, id: Int) {
        listBillOrderMaterial.removeAt(position)
        adapter!!.notifyDataSetChanged()
    }
}