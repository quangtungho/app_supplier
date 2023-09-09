package vn.techres.supplier.fragment.debtmanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.debtadapter.DetailDebtReceivableAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentDetailDebtBinding
import vn.techres.supplier.fragment.ordermanager.orderbilldetail.OrderCompletedDetailFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickDetailDebt
import vn.techres.supplier.model.datamodel.Branch
import vn.techres.supplier.model.datamodel.DataOrder
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.BranchResponse
import vn.techres.supplier.model.response.OrderResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class DetailDebtFragment :
        BaseBindingFragment<FragmentDetailDebtBinding>(FragmentDetailDebtBinding::inflate),
        OnClickDetailDebt {
    val tagName: String = FragmentDetailDebtBinding::class.java.name
    private var adapter: DetailDebtReceivableAdapter? = null
    private var idRestaurant: Int = 0
    private var branchID: Int = 0
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    private var listDebt = DataOrder()
    private var branchList = ArrayList<Branch>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
        binding!!.spinnerListBranchOrder.onItemSelectedListener = null

    }

    private fun onBody() {
        setAdapter()
        dataFrom()
        dataTo()
        mainActivity!!.setBackClick(true)
        mainActivity!!.setHeader(getString(R.string.detail_debt))
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        idRestaurant = cacheManager.get(TechresEnum.ID_RESTAURANT_DEBT.toString())!!.toInt()
        val animScaleShowTopToBottom =
                AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.rcvDetailDebt.startAnimation(animScaleShowTopToBottom)
        binding!!.layoutDate.fromData.setOnClickListener {
            dialogFromDatePicker()
        }
        binding!!.layoutDate.toData.setOnClickListener {
            dialogToDatePicker()
        }

        getAPIBranch(idRestaurant)
        binding!!.swipeRefreshLayout.setOnRefreshListener {
            onSwipeRefreshLayout()
        }
    }

    private fun onSwipeRefreshLayout() {
        getAPIListDebtBranch(branchID)
        binding!!.swipeRefreshLayout.isRefreshing = false
    }

    fun successGetBranch() {
        branchID = branchList[0].id
        getAPIListDebtBranch(branchID)
        AppUtils.getBranchSpinner(binding!!.spinnerListBranchOrder, branchList)
        binding!!.spinnerListBranchOrder.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
            ) {
                branchID = branchList[position].id
                getAPIListDebtBranch(branchID)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    private fun currencyFormatVND(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    private fun setAdapter() {
        binding!!.rcvDetailDebt.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = DetailDebtReceivableAdapter(mainActivity!!.baseContext)
        binding!!.rcvDetailDebt.adapter =
                ConcatAdapter(adapter)
        binding!!.rcvDetailDebt.setHasFixedSize(true)
        adapter!!.setClickDetailDebt(this)
    }

    @SuppressLint("SetTextI18n")
    fun dataFrom() {
        val sdfTo = SimpleDateFormat(getString(R.string.mmyyyy), Locale.getDefault())
        dateFrom = "01/" + sdfTo.format(Date())
        binding!!.layoutDate.fromData.text = dateFrom
    }

    @SuppressLint("SetTextI18n")
    fun dataTo() {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        binding!!.layoutDate.toData.text = dateTo
    }

    private fun dialogFromDatePicker() {
        val bottomSheetDialog = BottomSheetDialog(this.mainActivity!!, R.style.SheetDialog)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_date_picker)
        bottomSheetDialog.setCancelable(false)
        val tvTitleChooseDate = bottomSheetDialog.findViewById<TextView>(R.id.tvTitleChooseDate)
        val imgClose = bottomSheetDialog.findViewById<ImageView>(R.id.imgClose)
        val btnConfirm = bottomSheetDialog.findViewById<Button>(R.id.btnConfirm)
        val datePicker =
                bottomSheetDialog.findViewById<com.ycuwq.datepicker.date.DatePicker>(R.id.datePicker)

        tvTitleChooseDate!!.text = mainActivity!!.baseContext.getString(R.string.choose_date)

        imgClose!!.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        btnConfirm!!.setOnClickListener {
            binding!!.layoutDate.fromData.text = String.format(
                    "%s/%s/%s",
                    getDate(datePicker!!.day),
                    getDate(datePicker.month),
                    datePicker.year
            )

            getAPIListDebtBranch(branchID)
            dateFrom = String.format(
                    "%s/%s/%s",
                    getDate(datePicker.day),
                    getDate(datePicker.month),
                    datePicker.year
            )
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun dialogToDatePicker() {
        val bottomSheetDialog = BottomSheetDialog(this.mainActivity!!, R.style.SheetDialog)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_date_picker)
        bottomSheetDialog.setCancelable(false)
        val tvTitleChooseDate = bottomSheetDialog.findViewById<TextView>(R.id.tvTitleChooseDate)
        val imgClose = bottomSheetDialog.findViewById<ImageView>(R.id.imgClose)
        val btnConfirm = bottomSheetDialog.findViewById<Button>(R.id.btnConfirm)
        val datePicker =
                bottomSheetDialog.findViewById<com.ycuwq.datepicker.date.DatePicker>(R.id.datePicker)

        tvTitleChooseDate!!.text = mainActivity!!.baseContext.getString(R.string.choose_date)

        imgClose!!.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        btnConfirm!!.setOnClickListener {
            binding!!.layoutDate.toData.text = String.format(
                    "%s/%s/%s",
                    getDate(datePicker!!.day),
                    getDate(datePicker.month),
                    datePicker.year
            )
            getAPIListDebtBranch(branchID)
            dateTo = String.format(
                    "%s/%s/%s",
                    getDate(datePicker.day),
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

    /**
     * Chi nhánh nhà hàng
     *
     */
    private fun getAPIBranch(idRestaurant: Int) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/branches?status=${TechresEnum.STATUS_1}&restaurant_id=$idRestaurant"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListBranch(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BranchResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: BranchResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            branchList = response.data
                            successGetBranch()

                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }

                    }
                })
    }

    /**
     * goi API get chi tiet don hang cong no
     */
    private fun getAPIListDebtBranch(idBranch: Int) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-orders/supplier?branch_id=$idBranch&key_search&statuses=${TechresEnum.STATUS_4},${TechresEnum.STATUS_6},${TechresEnum.STATUS_7}&from=$dateFrom&to=$dateTo&page=${TechresEnum.PAGE}&limit=${TechresEnum.LIMIT_1000}&payment_statuses=0,1"
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListOrderCompleted(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<OrderResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: OrderResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDebt = response.data
                            binding!!.txtTotal.text =
                                    currencyFormatVND(listDebt.total_amount.toString())
                            if (listDebt.list.size == 0) {
                                binding!!.rcvDetailDebt.visibility = View.GONE
                                binding!!.linearDataNull.visibility = View.VISIBLE
                            } else {
                                binding!!.rcvDetailDebt.visibility = View.VISIBLE
                                binding!!.linearDataNull.visibility = View.GONE

                            }
                            adapter!!.setDataList(listDebt.list)

                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }

                    }

                })
    }


    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }

    override fun onClick(position: Int, id: Int) {
        val orderCompletedDetailFragment = OrderCompletedDetailFragment()
        cacheManager.put(TechresEnum.GET_BY_ID.toString(), id.toString())
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.nav_host, OrderCompletedDetailFragment())
                            ?.addToBackStack(orderCompletedDetailFragment.tagName)
                            ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}