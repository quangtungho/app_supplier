package vn.techres.supplier.fragment.debtmanager

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
import vn.techres.supplier.adapter.debtadapter.DebtToPayAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentDebtToPayBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.WareHouse
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.WareHouseResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.SimpleDateFormat
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class DebtToPayFragment :
    BaseBindingFragment<FragmentDebtToPayBinding>(FragmentDebtToPayBinding::inflate) {
    val tagName: String = FragmentDebtToPayBinding::class.java.name
    private var adapter: DebtToPayAdapter? = null
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    private var listData = ArrayList<WareHouse>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        mainActivity!!.setBackClick(false)
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constrainLayoutMain.startAnimation(animFragment)

        val animScaleShowTopToBottom =
            AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerView.startAnimation(animScaleShowTopToBottom)

        dataFrom()
        dataTo()
        getAPIDebtToPay(binding!!.fromData.text.toString(), binding!!.toData.text.toString())
        binding!!.fromData.setOnClickListener {
            dialogFromDatePicker()
        }
        binding!!.toData.setOnClickListener {
            dialogToDatePicker()
        }
    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = DebtToPayAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter =
            ConcatAdapter(adapter)
        binding!!.recyclerView.setHasFixedSize(true)

    }

    @SuppressLint("SetTextI18n")
    fun dataFrom() {
        val sdfTo = SimpleDateFormat("", Locale.getDefault())
        dateFrom = "01/01/1999" + sdfTo.format(Date())
        binding!!.fromData.text = dateFrom
    }

    @SuppressLint("SetTextI18n")
    fun dataTo() {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        binding!!.toData.text = dateTo
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
            binding!!.fromData.text = String.format(
                "%s/%s/%s",
                getDate(datePicker!!.day),
                getDate(datePicker.month),
                datePicker.year
            )
            getAPIDebtToPay(
                binding!!.fromData.text.toString(),
                binding!!.toData.text.toString()
            )
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
            binding!!.toData.text = String.format(
                "%s/%s/%s",
                getDate(datePicker!!.day),
                getDate(datePicker.month),
                datePicker.year
            )
            getAPIDebtToPay(
                binding!!.fromData.text.toString(),
                binding!!.toData.text.toString()
            )
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
     * goi API get danh sach công no chi cua supplier
     */
    private fun getAPIDebtToPay(fromDate: String, toDate: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-warehouse-sessions?type=${TechresEnum.STATUS_0}&from_date=$fromDate&to_date=$toDate&limit=1000&page=0&status=${TechresEnum.PAYMENT_STATUS}&payment_status=0"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListWareHouse(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<WareHouseResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: WareHouseResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listData = response.data.list
                        if (listData.size == 0) {
                            binding!!.linearDataNull.visibility = View.VISIBLE
                            binding!!.recyclerView.visibility = View.GONE
                        } else {
                            binding!!.linearDataNull.visibility = View.GONE
                            binding!!.recyclerView.visibility = View.VISIBLE
                        }
                        adapter!!.setDataList(listData)
                        binding!!.txtNumberCount.text = listData.size.toString()
                    } else {
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
}