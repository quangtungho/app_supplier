package vn.techres.supplier.fragment.ordermanager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.ordermanageradapterr.OrderBillAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentListOrderBillBinding
import vn.techres.supplier.fragment.ordermanager.orderbilldetail.OrderBillDetailFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickBillOrder
import vn.techres.supplier.interfaces.OnClickBillOrderRequest
import vn.techres.supplier.model.datamodel.DataListOrderBill
import vn.techres.supplier.model.datamodel.DataOrderBill
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.OrderBillResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderBillFragment :
        BaseBindingFragment<FragmentListOrderBillBinding>(FragmentListOrderBillBinding::inflate),
        OnClickBillOrderRequest, OnClickBillOrder {
    val tagName: String = OrderBillFragment::class.java.name

    //adapter phiếu
    private var adapterBill: OrderBillAdapter? = null
    private var orderDetailFragment = OrderBillDetailFragment()

    //lấy danh sách phiếu order đơn hàng lên nhà cung cấp
    private var dataOrderBill = DataOrderBill()
    private var idRestaurant: Int = 0
    private var dataTo: String = ""
    var position = 0
    var code = ""
    private var idOrder: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        mainActivity!!.setHeader(getString(R.string.list_bill_order))
        mainActivity!!.setBackClick(true)
        binding!!.lnSearch.visibility = View.VISIBLE
        idRestaurant = cacheManager.get(TechresEnum.GET_BY_ID_RESTAURANT.toString())!!.toInt()
        dataTo = cacheManager.get(TechresEnum.DATA_TO.toString())!!.toString()
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        val animScaleShowTopToBottom =
                AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerView.startAnimation(animScaleShowTopToBottom)

        searchBill()
        binding!!.btnDate.text = dataTo

        binding!!.swipeRefreshLayout.setOnRefreshListener {
            onSwipeRefreshLayout()
        }
        getAPIListOrderRequest()
    }

    override fun onResume() {
        super.onResume()
        getAPIListOrderRequest()
    }

    private fun onSwipeRefreshLayout() {
        getAPIListOrderRequest()
        binding!!.swipeRefreshLayout.isRefreshing = false
    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapterBill = OrderBillAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter =
                ConcatAdapter(adapterBill)
        binding!!.recyclerView.setHasFixedSize(true)
        //viewDetail
        adapterBill?.setClickView(this)

    }

    private fun searchBill() {
        binding!!.btnSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList: ArrayList<DataListOrderBill> = ArrayList()
                for (key in dataOrderBill.list) {
                    if (dataOrderBill.list.size > 0) {
                        val name: String = key.restaurant_name!!
                        if (name.lowercase(Locale.getDefault())
                                        .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapterBill!!.setDataListOrderBill(
                        newList,
                        dataOrderBill.total_record ?: 0
                )
                binding!!.recyclerView.setHasFixedSize(true)
                binding!!.recyclerView.adapter = adapterBill
                return false
            }
        })
    }

    //_____________________API_________________//
    //API lấy danh sach phieu dat hang /// status 1
    private fun getAPIListOrderRequest() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-order-request?time=$dataTo&status=${TechresEnum.STATUS_1},${TechresEnum.STATUS_5}&restaurant_id=$idRestaurant"
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListOrderBill(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<OrderBillResponse> {
                    override fun onComplete() {}
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: OrderBillResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            dataOrderBill = response.data
                            adapterBill!!.setDataListOrderBill(
                                    dataOrderBill.list,
                                    dataOrderBill.total_record ?: 0
                            )
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }

                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }

    //--------------------------ONCLICK-------------------//
    override fun onClickViewOrderRequest(position: Int, id: Int) {
        this.position = position
        this.idOrder = id
        cacheManager.put(TechresEnum.GET_BY_ID.toString(), id.toString())
        cacheManager.put(TechresEnum.GET_SIZE.toString(), dataOrderBill.total_record.toString())
        cacheManager.put(
                TechresEnum.STATUS.toString(),
                dataOrderBill.list[position].status.toString()
        )
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.nav_host, OrderBillDetailFragment())
                            ?.addToBackStack(orderDetailFragment.tagName)
                            ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
        )
    }

    override fun onClickViewWait(position: Int, id: Int, img: String) {}
    override fun onClickViewDelivery(position: Int, id: Int, img: String) {}
    override fun onClickViewReturn(position: Int, id: Int, img: String) {}
    override fun onClickViewCanceled(position: Int, id: Int, img: String) {}
    override fun onClickViewCompleted(position: Int, id: Int, img: String) {}
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
        cacheManager.put(
                TechresEnum.KEY_TAB_ORDER_BILL.toString(),
                "0"
        )
    }
}