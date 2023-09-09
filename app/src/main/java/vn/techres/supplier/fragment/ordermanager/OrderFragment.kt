package vn.techres.supplier.fragment.ordermanager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.ordermanageradapterr.OrderDeliveringAdapter
import vn.techres.supplier.adapter.ordermanageradapterr.OrderReturnAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentListOrderBillBinding
import vn.techres.supplier.fragment.ordermanager.orderbilldetail.OrderDeliveryDetailFragment
import vn.techres.supplier.fragment.ordermanager.orderbilldetail.OrderReturnDetailFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickBillOrder
import vn.techres.supplier.interfaces.OnClickBillOrderRequest
import vn.techres.supplier.model.datamodel.DataOrder
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.OrderResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderFragment :
    BaseBindingFragment<FragmentListOrderBillBinding>(FragmentListOrderBillBinding::inflate),
    OnClickBillOrderRequest, OnClickBillOrder {
    val tagName: String = OrderFragment::class.java.name
    private var adapterDelivering: OrderDeliveringAdapter? = null
    private var adapterReturn: OrderReturnAdapter? = null
    private var orderReturnDetailFragment = OrderReturnDetailFragment()
    private var dataOrderDelivery = DataOrder()
    private var idBill: Int = 0
    private var dataTo: String = ""
    //Lấy chi tiết phiếu yêu cầu đơn hàng của kế toán lên nhà cung cấp
    private var orderDeliveryDetailFragment = OrderDeliveryDetailFragment()
    var position = 0
    var code = ""
    private var idOrderBill: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        setAdapter()
        mainActivity!!.setHeader(getString(R.string.list_order))
        mainActivity!!.setBackClick(true)
        idBill = cacheManager.get(TechresEnum.GET_BY_ID_RESTAURANT.toString())!!.toInt()
        dataTo = cacheManager.get(TechresEnum.DATA_TO.toString())!!.toString()
        binding!!.lnSearch.visibility = View.VISIBLE
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        val animScaleShowTopToBottom =
            AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerView.startAnimation(animScaleShowTopToBottom)
        binding!!.btnDate.text = dataTo

        binding!!.swipeRefreshLayout.setOnRefreshListener {
            onSwipeRefreshLayout()
        }
        getAPIListOrderDelivering()
    }
    override fun onResume() {
        super.onResume()

        getAPIListOrderDelivering()

    }
    private fun onSwipeRefreshLayout() {
        getAPIListOrderDelivering()
        binding!!.swipeRefreshLayout.isRefreshing = false
    }
    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapterDelivering = OrderDeliveringAdapter(mainActivity!!.baseContext)
        adapterReturn = OrderReturnAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter =
            ConcatAdapter(adapterDelivering, adapterReturn)
        binding!!.recyclerView.setHasFixedSize(true)
        //viewDetail
        adapterDelivering?.setClickView(this)
        adapterReturn?.setClickView(this)

    }
    //API lấy danh sach don hang cho xu ly(Dang giao) // status 3
    private fun getAPIListOrderDelivering() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders?time=$dataTo&payment_status=${TechresEnum.PAYMENT_STATUS}&status=${TechresEnum.STATUS_3}&page=${TechresEnum.PAGE}&limit=${TechresEnum.LIMIT_1000}&get_time_store=${TechresEnum.GET_TIME_STORE_2}&restaurant_id=$idBill"
        ServiceFactory.createRetrofitService(
            TechResService::class.java,
            requireActivity()
        )
            .getListOrderDelivering(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<OrderResponse> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: OrderResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataOrderDelivery = response.data
                        adapterDelivering!!.setDataListOrder(
                            dataOrderDelivery.list,
                            dataOrderDelivery.total_record ?: 0
                        )
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(e: Throwable) {
                }
            })
    }
    //--------------------------ONCLICK-------------------//
    override fun onClickViewOrderRequest(position: Int, id: Int) {}
    override fun onClickViewWait(
        position: Int,
        id: Int,
        img: String
    ) {
    }
    override fun onClickViewDelivery(
        position: Int,
        id: Int,
        img: String
    ) {
        this.position = position
        this.idOrderBill = id
        cacheManager.put(TechresEnum.GET_BY_ID.toString(), id.toString())
        cacheManager.put(TechresEnum.GET_BY_AVATAR.toString(), img)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.nav_host, OrderDeliveryDetailFragment())
                    ?.addToBackStack(orderDeliveryDetailFragment.tagName)
                    ?.commit()
            }, TechresEnum.TIME_100.toString().toLong()
        )

    }
    override fun onClickViewReturn(
        position: Int,
        id: Int,
        img: String
    ) {
        this.position = position
        this.idOrderBill = id
        cacheManager.put(TechresEnum.GET_BY_ID.toString(), id.toString())
        cacheManager.put(TechresEnum.GET_BY_AVATAR.toString(), img)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.nav_host, OrderReturnDetailFragment())
                    ?.addToBackStack(orderReturnDetailFragment.tagName)
                    ?.commit()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
    override fun onClickViewCanceled(
        position: Int,
        id: Int,
        img: String
    ) {
    }
    override fun onClickViewCompleted(
        position: Int,
        id: Int,
        img: String
    ) {
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
        activity?.supportFragmentManager?.popBackStack()
        cacheManager.put(
            TechresEnum.KEY_TAB_ORDER_BILL.toString(),
            "0"
        )
    }
}