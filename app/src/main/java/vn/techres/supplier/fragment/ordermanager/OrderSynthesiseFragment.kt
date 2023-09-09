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
import vn.techres.supplier.adapter.ordermanageradapterr.summaterialorder.SumMaterialOrderAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentSumMaterialOrderBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataSumMaterialOrder
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.SumMaterialOrderResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.SimpleDateFormat
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderSynthesiseFragment :
    BaseBindingFragment<FragmentSumMaterialOrderBinding>(FragmentSumMaterialOrderBinding::inflate) {
    val tagName: String = OrderSynthesiseFragment::class.java.name
    private var adapter: SumMaterialOrderAdapter? = null
    private var dateTo: String? = ""
    private var dateToMonth: String? = ""
    private var listData = ArrayList<DataSumMaterialOrder>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        dataTo()
        dataToMonth()
        mainActivity!!.setLoading(true)
        setAdapter()
        mainActivity!!.setHeader(getString(R.string.btn_order))
        mainActivity!!.setBackClick(false)
        mainActivity!!.setLoading(false)
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        val animScaleShowTopToBottom =
            AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerView.startAnimation(animScaleShowTopToBottom)
        binding!!.btnDate.setOnClickListener {
            dialogToDatePicker()
        }
        binding!!.btnDateMonth.setOnClickListener {
            dialogToDatePickerMonth()
        }
        binding!!.checkBox.setOnClickListener {
            if (binding!!.checkBox.isChecked) {
                binding!!.txtMonth.text = getText(R.string.view_month)
                binding!!.btnDateMonth.visibility = View.GONE
                binding!!.btnDate.visibility = View.GONE
                binding!!.btnDateMonth.visibility = View.VISIBLE
                getAPISumMaterialOrder(binding!!.btnDateMonth.text.toString())
            } else {
                binding!!.txtMonth.text = getText(R.string.view_day)
                binding!!.btnDateMonth.visibility = View.VISIBLE
                binding!!.btnDate.visibility = View.VISIBLE
                binding!!.btnDateMonth.visibility = View.GONE
                getAPISumMaterialOrder(binding!!.btnDate.text.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (binding!!.checkBox.isChecked) {
            getAPISumMaterialOrder(binding!!.btnDateMonth.text.toString())
        } else {
            getAPISumMaterialOrder(binding!!.btnDate.text.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    fun dataTo() {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        binding!!.btnDate.text = dateTo
    }

    @SuppressLint("SetTextI18n")
    fun dataToMonth() {
        val sdfToMonth = SimpleDateFormat(getString(R.string.mmyyyy), Locale.getDefault())
        dateToMonth = sdfToMonth.format(Date())
        binding!!.btnDateMonth.text = dateToMonth
    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = SumMaterialOrderAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter =
            ConcatAdapter(adapter)
        binding!!.recyclerView.setHasFixedSize(true)
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
            binding!!.btnDate.text = String.format(
                "%s/%s/%s",
                getDate(datePicker!!.day),
                getDate(datePicker.month),
                datePicker.year
            )
            getAPISumMaterialOrder(binding!!.btnDate.text.toString())
            binding!!.btnDate.text = String.format(
                "%s/%s/%s",
                getDate(datePicker.day),
                getDate(datePicker.month),
                datePicker.year
            )
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun dialogToDatePickerMonth() {
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
            binding!!.btnDateMonth.text = String.format(
                "%s/%s",
                getDate(datePicker.month),
                datePicker.year
            )
            getAPISumMaterialOrder(binding!!.btnDateMonth.text.toString())

            binding!!.btnDateMonth.text = String.format(
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

    //_____________________API_________________//
    //API lấy danh sach phieu dat hang /// status 1
    private fun getAPISumMaterialOrder(time: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders/total-quantity-material?time=$time"
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListSumMaterialOrder(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SumMaterialOrderResponse> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: SumMaterialOrderResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE){
                        listData = response.data
                        if (listData.size == 0) {
                            binding!!.linearDataNull.visibility = View.VISIBLE
                            binding!!.linearData.visibility = View.GONE
                        } else {
                            binding!!.linearDataNull.visibility = View.GONE
                            binding!!.linearData.visibility = View.VISIBLE
                        }
                        adapter!!.setDataList(listData)
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }

                }

                override fun onError(e: Throwable) {
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