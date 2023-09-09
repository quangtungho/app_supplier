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
import vn.techres.supplier.adapter.ordermanageradapterr.OrderReturnAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentCancelledBinding
import vn.techres.supplier.fragment.ordermanager.orderbilldetail.OrderReturnDetailFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickBillOrder
import vn.techres.supplier.model.datamodel.DataListReturnsOrder
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.ReturnsOrderResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.SimpleDateFormat
import java.util.*
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderReturnsFragment :
    BaseBindingFragment<FragmentCancelledBinding>(FragmentCancelledBinding::inflate),
    OnClickBillOrder {
    private var adapter: OrderReturnAdapter? = null// adaptar gom order
    private var returnOrder = DataListReturnsOrder()
    private var dateTo: String? = ""
    var position = 0
    private var idOrderBill: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        setAdapter()
        dataTo()
        mainActivity!!.setBackClick(false)
        mainActivity!!.setHeader(getString(R.string.btn_order))
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        val animScaleShowTopToBottom =
            AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerView.startAnimation(animScaleShowTopToBottom)

        getAPIReturn(binding!!.btnDate.text.toString())
        binding!!.btnDate.setOnClickListener {
            dialogToDatePicker()
        }
        binding!!.swipeRefreshLayout.setOnRefreshListener {
            onSwipeRefreshLayout()
        }
    }
    override fun onResume() {
        super.onResume()
        getAPIReturn(binding!!.btnDate.text.toString())
    }
    private fun onSwipeRefreshLayout() {

        getAPIReturn(binding!!.btnDate.text.toString())
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
            getAPIReturn(binding!!.btnDate.text.toString())

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
        adapter = OrderReturnAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = ConcatAdapter(adapter)
        binding!!.recyclerView.setHasFixedSize(true)
        adapter?.setClickView(this)
    }
    private fun getAPIReturn(toData: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-material-return-requests?limit=${TechresEnum.LIMIT_100}&time=$toData&restaurant_id=${TechresEnum.STATUS_RETURNS}&page=${TechresEnum.PAGE}&status=${TechresEnum.STATUS_RETURNS}"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListReturnsOrder(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ReturnsOrderResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: ReturnsOrderResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        returnOrder = response.data
                        if (returnOrder.list.size == 0)
                            binding!!.linearDataNull.visibility = View.VISIBLE
                        else
                            binding!!.linearDataNull.visibility = View.GONE

                        adapter!!.setDataListOrder(returnOrder.list)

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
    override fun onClickViewWait(position: Int, id: Int, img: String) {
    }
    override fun onClickViewDelivery(position: Int, id: Int, img: String) {

    }
    override fun onClickViewReturn(position: Int, id: Int, img: String) {
        this.position = position
        this.idOrderBill = id
        cacheManager.put(TechresEnum.GET_BY_ID.toString(), id.toString())
        cacheManager.put(TechresEnum.GET_BY_AVATAR.toString(), img)
        val orderReturnDetailFragment = OrderReturnDetailFragment()
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
    override fun onClickViewCanceled(position: Int, id: Int, img: String) {

    }
    override fun onClickViewCompleted(position: Int, id: Int, img: String) {

    }
}