package vn.techres.supplier.fragment.ordermanager.orderbilldetail

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.ordermanageradapterr.supplierorderadapter.DialogBillMaterialOrderReturnAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOrderDetailBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.Currency
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataReturnsOrder
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.ReturnBillOrderParams
import vn.techres.supplier.model.response.DetailMaterialResponse
import vn.techres.supplier.model.response.OrderResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.TechResTextView

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderReturnDetailFragment : BaseBindingFragment<FragmentOrderDetailBinding>(
        FragmentOrderDetailBinding::inflate
) {
    val tagName: String = OrderReturnDetailFragment::class.java.name
    private var adapter: DialogBillMaterialOrderReturnAdapter? = null
    private var dataDetailBillOrder = DataReturnsOrder()

    //Lấy danh sách nguyên liệu theo phiếu đặt hàng từ kế toán
    var position = 0
    var idOrderBill: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constraintLayoutMain.startAnimation(animFragment)


        binding!!.txtReturnsOrder.visibility = View.VISIBLE
        binding!!.viewReturnsOrder.visibility = View.VISIBLE

        binding!!.numberOfOrders.visibility = View.GONE
        binding!!.delivering.visibility = View.GONE

        binding!!.view1.visibility = View.GONE
        binding!!.viewReturnsOrder.visibility = View.GONE
        binding!!.btnRetweetOrder1.visibility = View.VISIBLE

        binding!!.totalMoney1.visibility = View.GONE
        binding!!.totalMoney.visibility = View.GONE
        binding!!.totalMoney2.visibility = View.VISIBLE
        binding!!.txtTotalMoney2.visibility = View.VISIBLE

        binding!!.viewFunction.visibility = View.GONE
        idOrderBill = cacheManager.get(TechresEnum.GET_BY_ID.toString())!!.toInt()
        mainActivity!!.setHeader(getString(R.string.txt_detail_order))
        mainActivity!!.setBackClick(true)
        getAPIDetailReturns()
//        getAPIBillOrderMaterial()

        //tra hang ve kho
        binding!!.btnRetweetOrder.setOnClickListener {
            onRetweetOrderWahouse()
        }
        // tra hang bị hong?
        binding!!.btnRetweetOrder1.setOnClickListener {
            onRetweetOrder()
        }
        AppUtils.callPhoto(cacheManager.get(TechresEnum.GET_BY_AVATAR.toString()), binding!!.avatar)
    }

    fun setAdapter() {
        adapter = DialogBillMaterialOrderReturnAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)
        binding!!.recyclerView.setHasFixedSize(true)

    }

    //duyet trả về kho
    private fun onRetweetOrderWahouse() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_notify)
        val window = dialog.window
        window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtTitle: TechResTextView = dialog.findViewById(R.id.txtTitle)
        val txtNotify: TechResTextView = dialog.findViewById(R.id.txtContent)
        val btnYes: Button = dialog.findViewById(R.id.yes)
        val btnNo: Button = dialog.findViewById(R.id.no)

        txtTitle.setText(R.string.dialog_notify_title)
        txtNotify.setText(R.string.dialog_notify__bill_order_change_status)

        btnYes.setOnClickListener {
            changeBillOrder(
                    TechresEnum.STATUS_2.toString().toInt(),
            )
            dialog.dismiss()
            activity?.supportFragmentManager?.popBackStack()
            cacheManager.put(
                    TechresEnum.KEY_TAB_ORDER_BILL.toString(),
                    "2"
            )
        }

        btnNo.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()

    }

    //duyet tra huy hang
    private fun onRetweetOrder() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_notify)
        val window = dialog.window
        window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtTitle: TechResTextView = dialog.findViewById(R.id.txtTitle)
        val txtNotify: TechResTextView = dialog.findViewById(R.id.txtContent)
        val btnYes: Button = dialog.findViewById(R.id.yes)
        val btnNo: Button = dialog.findViewById(R.id.no)

        txtTitle.setText(R.string.dialog_notify_title)
        txtNotify.setText(R.string.dialog_notify__bill_order_change_status)

        btnYes.setOnClickListener {
            changeBillOrder(
                    TechresEnum.STATUS_3.toString().toInt()
            )
            dialog.dismiss()
            activity?.supportFragmentManager?.popBackStack()
            cacheManager.put(
                    TechresEnum.KEY_TAB_ORDER_BILL.toString(),
                    "2"
            )
        }

        btnNo.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()

    }

    //BTN BACK
    override fun onBackPress() {
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager?.popBackStack()
                    cacheManager.put(
                            TechresEnum.KEY_TAB_ORDER_BILL.toString(),
                            "2"
                    )
                }, TechresEnum.TIME_100.toString().toLong()
        )
    }

    //________________API TRANG THAI_________________//
    private fun changeBillOrder(supplierWarehouseSessionType: Int) {
        val params = ReturnBillOrderParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.params.supplier_warehouse_session_type = supplierWarehouseSessionType
        params.request_url =
                "/api/supplier-material-return-requests/$idOrderBill/change-status"
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getChangeOrder(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<OrderResponse> {
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: OrderResponse) {
                        if (response.status != AppConfig.SUCCESS_CODE) {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    private fun formatFloatToString(f: Float): String {
        val i = f.toInt()
        return if (f == i.toFloat()) i.toString() else f.toString()
    }

    //API CHI TIET DON HANG
    private fun getAPIDetailReturns() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-material-return-requests/$idOrderBill"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, mainActivity!!
        )
                .getDetailMaterial(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DetailMaterialResponse> {
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: DetailMaterialResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            dataDetailBillOrder = response.data
                            if (dataDetailBillOrder.status == 0) {
                                binding!!.linearLayoutBtn.visibility = View.VISIBLE
                                binding!!.btnRetweetOrder.visibility = View.VISIBLE
                                binding!!.btnRetweetOrder1.visibility = View.VISIBLE
                            } else {
                                binding!!.linearLayoutBtn.visibility = View.GONE
                                binding!!.btnRetweetOrder.visibility = View.GONE
                                binding!!.btnRetweetOrder1.visibility = View.GONE
                            }
                            //thong tin đặt hàng
                            binding!!.nameRestaurant.text =
                                    dataDetailBillOrder.restaurant_name
                            binding!!.nameBrand.text =
                                    dataDetailBillOrder.restaurant_brand_name
                            binding!!.nameBranch.text =
                                    dataDetailBillOrder.branch_name
                            binding!!.code.text = dataDetailBillOrder.code//mã dh
                            binding!!.deliveryDate.text =
                                    dataDetailBillOrder.created_at
                            binding!!.discount.text = dataDetailBillOrder.discount_percent.toString()
                            binding!!.vat.text = dataDetailBillOrder.vat.toString()
                            binding!!.totalmaterial.text =
                                    formatFloatToString(dataDetailBillOrder.total_material.toFloat())//tong nguyen lieu
                            binding!!.totalMoney2.text =
                                    Currency.formatCurrencyDecimal(dataDetailBillOrder.total_amount.toFloat()) // tthanh tien

                            adapter!!.setDataListOrder(dataDetailBillOrder)
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }
}