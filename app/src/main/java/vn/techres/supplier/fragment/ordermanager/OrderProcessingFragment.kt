package vn.techres.supplier.fragment.ordermanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.ordermanageradapterr.OrderRestaurantAdapter
import vn.techres.supplier.adapter.ordermanageradapterr.OrderRestaurantBillAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOrderRestaurantBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.ClickID
import vn.techres.supplier.model.datamodel.DataListOrderRestaurant
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.OrderRestaurantResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.SimpleDateFormat
import java.util.*
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderProcessingFragment :
    BaseBindingFragment<FragmentOrderRestaurantBinding>(FragmentOrderRestaurantBinding::inflate),
    ClickID {
    private var adapterBill: OrderRestaurantBillAdapter? = null // adaptar gom orderbill
    private var adapter: OrderRestaurantAdapter? = null// adaptar gom order
    private var orderBilRestaurant = ArrayList<DataListOrderRestaurant>()
    private var orderRestaurant = ArrayList<DataListOrderRestaurant>()
    private var dateTo: String? = ""
    private var idRestaurant: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setHeader(getString(R.string.btn_order))
        setAdapter()
        dataTo()
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        val animScaleShowTopToBottom =
            AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerView.startAnimation(animScaleShowTopToBottom)

        getAPIListOrderBill(binding!!.btnDate.text.toString())
        getAPIListOrder(binding!!.btnDate.text.toString())
        binding!!.btnDate.setOnClickListener {
            dialogToDatePicker()
        }
        binding!!.swipeRefreshLayout.setOnRefreshListener {
            onSwipeRefreshLayout()
        }
    }
    override fun onResume() {
        super.onResume()

        getAPIListOrderBill(binding!!.btnDate.text.toString())
        getAPIListOrder(binding!!.btnDate.text.toString())
    }
    private fun onSwipeRefreshLayout() {
        getAPIListOrderBill(binding!!.btnDate.text.toString())
        getAPIListOrder(binding!!.btnDate.text.toString())
        binding!!.swipeRefreshLayout.isRefreshing = false
    }
    @SuppressLint("SetTextI18n")
    fun dataTo() {
        val sdfTo = SimpleDateFormat(getString(R.string.mmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        binding!!.btnDate.text = dateTo
    }
    private fun dialogToDatePicker() {
        val bottomSheetDialog = BottomSheetDialog(this.mainActivity!!, R.style.SheetDialog)
        bottomSheetDialog.setContentView(R.layout.bottom_date_picker)
        bottomSheetDialog.setCancelable(false)
        val tvTitleChooseDate = bottomSheetDialog.findViewById<TextView>(R.id.tvTitleChooseDate)
        val imgClose = bottomSheetDialog.findViewById<ImageView>(R.id.imgClose)
        val btnConfirm = bottomSheetDialog.findViewById<Button>(R.id.btnConfirm)
        val datePicker =
            bottomSheetDialog.findViewById<com.ycuwq.datepicker.date.DatePicker>(R.id.datePicker)
        datePicker!!.dayPicker.visibility = View.GONE
        tvTitleChooseDate!!.text = mainActivity!!.baseContext.getString(R.string.choose_date)

        imgClose!!.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        btnConfirm!!.setOnClickListener {
            binding!!.btnDate.text = String.format(
                "%s/%s",
                getDate(datePicker.month),
                datePicker.year
            )
            getAPIListOrderBill(binding!!.btnDate.text.toString())
            getAPIListOrder(binding!!.btnDate.text.toString())

            binding!!.btnDate.text = String.format(
                "%s/%s",
                getDate(datePicker.month),
                datePicker.year
            )
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
    private fun getDate(date: Int): String {
        return if (date in 1..9) {
            "0$date"
        } else {
            date.toString()
        }
    }
    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapterBill = OrderRestaurantBillAdapter(mainActivity!!.baseContext)
        adapter = OrderRestaurantAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = ConcatAdapter(adapterBill, adapter)
        binding!!.recyclerView.setHasFixedSize(true)
        adapterBill?.setClickIDBill(this)
        adapter?.setClickID(this)
    }
    private fun getAPIListOrderBill(toData: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-order-request/group-by-restaurant?time=$toData&status=${TechresEnum.STATUS_1},${TechresEnum.STATUS_5}"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListOrderRestaurant(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<OrderRestaurantResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: OrderRestaurantResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        orderBilRestaurant = response.data
                        if (orderBilRestaurant.size == 0 && orderRestaurant.size == 0)
                            binding!!.linearDataNull.visibility = View.VISIBLE
                        else
                            binding!!.linearDataNull.visibility = View.GONE
                        adapterBill!!.setDataOrderBillRestaurant(orderBilRestaurant)

                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    private fun getAPIListOrder(toData: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders/group-by-restaurant?time=$toData&status=${TechresEnum.STATUS_3}&payment_status=-1"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListOrderRestaurant(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<OrderRestaurantResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: OrderRestaurantResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        orderRestaurant = response.data
                        if (orderBilRestaurant.size == 0 && orderRestaurant.size == 0)
                            binding!!.linearDataNull.visibility = View.VISIBLE
                        else
                            binding!!.linearDataNull.visibility = View.GONE
                        adapter!!.setDataOrderRestaurant(orderRestaurant)

                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    private var doubleBackToExitPressedOnce: Boolean = false
    override fun onBackPress() {
        if (doubleBackToExitPressedOnce) {
            mainActivity!!.finish()
            return
        }
        doubleBackToExitPressedOnce = true
        val snack = Snackbar.make(
            requireView(),
            R.string.toast_outapp,
            Snackbar.LENGTH_LONG
        )
        snack.show()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                doubleBackToExitPressedOnce = false
            }, TechresEnum.TIME_2000.toString().toLong()
        )
    }
    //click OrderRequest
    override fun clickIdBill(idRestaurant: Int) {
        this.idRestaurant = idRestaurant
        cacheManager.put(TechresEnum.GET_BY_ID_RESTAURANT.toString(), idRestaurant.toString())
        cacheManager.put(TechresEnum.DATA_TO.toString(), binding!!.btnDate.text.toString())
        val orderBillFragment = OrderBillFragment()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.nav_host, OrderBillFragment())
                    ?.addToBackStack(orderBillFragment.tagName)
                    ?.commit()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
    //Click order
    override fun clickId(idRestaurant: Int) {
        this.idRestaurant = idRestaurant
        cacheManager.put(TechresEnum.GET_BY_ID_RESTAURANT.toString(), idRestaurant.toString())
        cacheManager.put(TechresEnum.DATA_TO.toString(), binding!!.btnDate.text.toString())
        val orderFragment = OrderFragment()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.nav_host, OrderFragment())
                    ?.addToBackStack(orderFragment.tagName)
                    ?.commit()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}