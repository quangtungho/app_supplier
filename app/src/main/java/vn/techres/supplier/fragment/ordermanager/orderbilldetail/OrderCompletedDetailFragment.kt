package vn.techres.supplier.fragment.ordermanager.orderbilldetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.ordermanageradapterr.supplierorderadapter.DialogBillMaterialOrderCompletedAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOrderDetailBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.Currency
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataDetailOrderBillAll
import vn.techres.supplier.model.datamodel.DataListMatialOrder
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.DetailListMatialOrderResponse
import vn.techres.supplier.model.response.DetailOrderBillAllResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderCompletedDetailFragment : BaseBindingFragment<FragmentOrderDetailBinding>(
    FragmentOrderDetailBinding::inflate
) {
    val tagName: String = OrderCompletedDetailFragment::class.java.name
    private var adapter: DialogBillMaterialOrderCompletedAdapter? = null
    private var dataDetailBillOrder = DataDetailOrderBillAll()
    //Lấy danh sách nguyên liệu theo phiếu đặt hàng từ kế toán
    private var listBillOrderMaterial = ArrayList<DataListMatialOrder>()
    var position = 0
    private var idOrderBill: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {

        setAdapter()
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constraintLayoutMain.startAnimation(animFragment)
        idOrderBill = cacheManager.get(TechresEnum.GET_BY_ID.toString())!!.toInt()
        binding!!.viewReturnsOrder.visibility = View.VISIBLE
        binding!!.txtReturnsOrder.visibility = View.VISIBLE
        binding!!.receive.visibility = View.VISIBLE
        binding!!.viewReceiveOrder.visibility = View.VISIBLE
        binding!!.totalMoney1.visibility = View.GONE
        binding!!.totalMoney.visibility = View.GONE
        binding!!.totalMoney2.visibility = View.VISIBLE
        binding!!.txtTotalMoney2.visibility = View.VISIBLE
        binding!!.viewNumberCount.visibility = View.VISIBLE
        binding!!.txtNumberCount.visibility = View.VISIBLE
        mainActivity!!.setHeader(getString(R.string.txt_detail_order))
        mainActivity!!.setBackClick(true)
        getAPIDetailBillOrder()
        getAPIBillOrderMaterial()
        AppUtils.callPhoto(cacheManager.get(TechresEnum.GET_BY_AVATAR.toString()), binding!!.avatar)
    }
    fun setAdapter() {
        adapter = DialogBillMaterialOrderCompletedAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)
        binding!!.recyclerView.setHasFixedSize(true)
    }
    //BTN BACK
    override fun onBackPress() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager?.popBackStack()
                cacheManager.put(
                    TechresEnum.KEY_TAB_ORDER_BILL.toString(),
                    "1"
                )
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
    //__________________API DU LIEU________________//
    //API CHI TIET NGUYEN LIEU
    private fun getAPIBillOrderMaterial() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders/$idOrderBill/supplier-order-detail?is_return_material&status&time&page&limit"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getDetailOrderMaterial(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DetailListMatialOrderResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: DetailListMatialOrderResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listBillOrderMaterial = response.data
                        adapter!!.setDataListOrder(listBillOrderMaterial)
                    } else {
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
    private fun getAPIDetailBillOrder() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders/$idOrderBill"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getDetailBillOrderAll(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DetailOrderBillAllResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("SetTextI18n")
                override fun onNext(response: DetailOrderBillAllResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {

                        dataDetailBillOrder = response.data
                        //thong tin đặt hàng
                        binding!!.nameRestaurant.text = dataDetailBillOrder.restaurant_name
                        binding!!.nameBrand.text = dataDetailBillOrder.restaurant_brand_name
                        binding!!.nameBranch.text = dataDetailBillOrder.branch_name
                        binding!!.deliveryDate.text = dataDetailBillOrder.delivery_at
                        binding!!.code.text = dataDetailBillOrder.code
                        binding!!.discount.text = dataDetailBillOrder.discount_percent.toString()
                        binding!!.vat.text = dataDetailBillOrder.vat.toString()
                        binding!!.totalmaterial.text =
                            formatFloatToString(dataDetailBillOrder.total_material!!.toFloat())

                        binding!!.totalMoney2.text =
                            Currency.formatCurrencyDecimal(
                                dataDetailBillOrder.total_amount_reality
                                    .toFloat()
                            )
                    } else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
}