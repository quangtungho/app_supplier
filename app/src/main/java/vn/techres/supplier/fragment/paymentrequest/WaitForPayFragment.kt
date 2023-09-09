package vn.techres.supplier.fragment.paymentrequest

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.paymentrequestadapter.WaitForPayAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentPaymentRequestBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.ClickIdDebt
import vn.techres.supplier.model.datamodel.ListPaymentRequest
import vn.techres.supplier.model.datamodel.PaymentRequest
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.PaymentRequestResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.SimpleDateFormat
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2022
 * ================================================
 */
class WaitForPayFragment :
    BaseBindingFragment<FragmentPaymentRequestBinding>(FragmentPaymentRequestBinding::inflate),
    ClickIdDebt {
    val tagName: String = WaitForPayFragment::class.java.name
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    private var listData = ListPaymentRequest()
    private var adapter: WaitForPayAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        dataFrom()
        dataTo()
        setAdapter()

        binding!!.lnDate.fromData.setOnClickListener {
            dialogFromDatePicker()
        }
        binding!!.lnDate.toData.setOnClickListener {
            dialogToDatePicker()
        }
        binding!!.mRootButton.setOnClickListener {
            val createPaymentRequestFragment = CreatePaymentRequestFragment()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, CreatePaymentRequestFragment())
                        ?.addToBackStack(createPaymentRequestFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
        onSearch()
        getAPIListPaymentRequest()
    }

    private fun setAdapter() {
        adapter = mainActivity?.let { WaitForPayAdapter() }
        binding!!.rclPaymentRequest.adapter = adapter
        binding!!.rclPaymentRequest.layoutManager = LinearLayoutManager(context)
        binding!!.rclPaymentRequest.setHasFixedSize(true)
        adapter!!.setClickDetailPayment(this)
    }

    @SuppressLint("SetTextI18n")
    fun dataFrom() {
        val sdfTo = SimpleDateFormat(getString(R.string.mmyyyy), Locale.getDefault())
        dateFrom = "01/" + sdfTo.format(Date())
        binding!!.lnDate.fromData.text = dateFrom
    }

    @SuppressLint("SetTextI18n")
    fun dataTo() {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        binding!!.lnDate.toData.text = dateTo
    }

    private fun onSearch() {
        binding!!.txtSearchHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList: ArrayList<PaymentRequest> = ArrayList()
                for (key in listData.list) {
                    if (listData.list.size > 0) {
                        val name: String = key.restaurant_name
                        if (name.lowercase(Locale.getDefault())
                                .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapter!!.setDataList(newList)
                binding!!.rclPaymentRequest.setHasFixedSize(true)
                binding!!.rclPaymentRequest.adapter = adapter
                return false
            }
        })
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
            binding!!.lnDate.fromData.text = String.format(
                "%s/%s/%s",
                getDate(datePicker!!.day),
                getDate(datePicker.month),
                datePicker.year
            )
            getAPIListPaymentRequest()
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
            binding!!.lnDate.toData.text = String.format(
                "%s/%s/%s",
                getDate(datePicker!!.day),
                getDate(datePicker.month),
                datePicker.year
            )
            getAPIListPaymentRequest()
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

    private fun getAPIListPaymentRequest() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-restaurant-debt-payment-requests?restaurant_id=-1&restaurant_brand_id=-1&branch_id=-1&status=${TechresEnum.STATUS_0},${TechresEnum.STATUS_1}&from_date=$dateFrom&to_date=$dateTo&limit=${TechresEnum.LIMIT_100}&page=1&is_delete=0"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListPaymentRequest(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<PaymentRequestResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mainActivity!!.setBackClick(true)
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: PaymentRequestResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listData = response.data
                        if (listData.list.size == 0) {
                            binding!!.rclPaymentRequest.visibility = View.GONE
                            binding!!.linearDataNull.visibility = View.VISIBLE
                        } else {
                            binding!!.rclPaymentRequest.visibility = View.VISIBLE
                            binding!!.linearDataNull.visibility = View.GONE
                            adapter!!.setDataList(listData.list)
                        }

                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }


    override fun clickID(id: Int, status: Int) {
        val detailPaymentRequestFragment = DetailPaymentRequestFragment()
        cacheManager.put(TechresEnum.GET_ID.toString(), id.toString())
        cacheManager.put(TechresEnum.STATUS.toString(), status.toString())
        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.nav_host, DetailPaymentRequestFragment())
                    ?.addToBackStack(detailPaymentRequestFragment.tagName)
                    ?.commit()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}