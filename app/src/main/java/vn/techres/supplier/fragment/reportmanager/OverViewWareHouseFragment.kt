package vn.techres.supplier.fragment.reportmanager

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOverViewWareHouseBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.MyValueFormatter
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.WareHouseReportResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OverViewWareHouseFragment :
    BaseBindingFragment<FragmentOverViewWareHouseBinding>(FragmentOverViewWareHouseBinding::inflate),
    OnChartValueSelectedListener {
    val tagName: String = OverViewWareHouseFragment::class.java.name
    private var type: Int? = 0
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    private var wareHouse = WareHouseReportResponse()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        mainActivity!!.setHeader(getString(R.string.report_overview_ware_house_fragment))
        mainActivity!!.setBackClick(true)
        dataFrom()
        dataTo()
        getAPIWareHouseTotal(
            binding!!.fromData.text.toString(),
            binding!!.toData.text.toString()
        )
        binding!!.fromData.setOnClickListener {
            dialogFromDatePicker()
        }
        binding!!.toData.setOnClickListener {
            dialogToDatePicker()
        }
        binding!!.chartBarWareHouse.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartBarWareHouse.setNoDataTextColor(Color.BLACK)
    }

    @SuppressLint("SetTextI18n")
    fun dataFrom() {
        val sdfTo = SimpleDateFormat(getString(R.string.mmyyyy), Locale.getDefault())
        dateFrom = "01/" + sdfTo.format(Date())
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
            getAPIWareHouseTotal(
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
            getAPIWareHouseTotal(
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
    ////__________________CHart_________________//
    //Chart Bar - Báo cáo  tổng kho
    private fun setDataBarWareHouse() {
        val listCount = ArrayList<String>()
        listCount.add(0, "Tồn đầu")
        listCount.add(1, "Nhập kho")
        listCount.add(2, "Xuất kho")
        listCount.add(3, "Tồn kho hiện tại")
        listCount.add(4, "Hủy hàng")
        val xAxis: XAxis = binding!!.chartBarWareHouse.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)
        xAxis.labelRotationAngle = -85f
        xAxis.textSize = 10f
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setValueFormatter { value, _ -> listCount[value.toInt() % listCount.size] }
        val leftAxis: YAxis = binding!!.chartBarWareHouse.axisLeft
        val values = ArrayList<BarEntry>()
        values.add(
            0,
            BarEntry(
                (1).toFloat(),
                wareHouse.data.total_inventory_first_amount!!.toFloat() / 1000000
            )
        )
        values.add(
            1,
            BarEntry((2).toFloat(), wareHouse.data.total_import_amount!!.toFloat() / 1000000)
        )
        values.add(
            2,
            BarEntry((3).toFloat(), wareHouse.data.total_export_amount!!.toFloat() / 1000000)
        )
        values.add(
            3,
            BarEntry((4).toFloat(), wareHouse.data.total_inventory_now_amount!!.toFloat() / 1000000)
        )
        values.add(
            4,
            BarEntry((5).toFloat(), wareHouse.data.total_cancel_amount!!.toFloat() / 1000000)
        )
        binding!!.chartBarWareHouse.legend.isEnabled = false
        binding!!.chartBarWareHouse.description.isEnabled = false
        binding!!.chartBarWareHouse.setTouchEnabled(true)
        binding!!.chartBarWareHouse.setPinchZoom(false)
        // set listeners
        binding!!.chartBarWareHouse.setOnChartValueSelectedListener(this)
        binding!!.chartBarWareHouse.setDrawGridBackground(false)
        binding!!.chartBarWareHouse.setScaleEnabled(false)
        binding!!.chartBarWareHouse.isScaleXEnabled = true
        binding!!.chartBarWareHouse.axisRight.isEnabled = false
        binding!!.chartBarWareHouse.animateY(5000)
        binding!!.chartBarWareHouse.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding!!.chartBarWareHouse.axisRight.setDrawGridLines(false)
        binding!!.chartBarWareHouse.axisLeft.setDrawGridLines(false)
        binding!!.chartBarWareHouse.xAxis.setDrawGridLines(false)
        val set1 = BarDataSet(values, "")
        set1.setDrawIcons(false)
        val colors = ArrayList<Int>()
        colors.add(ColorTemplate.getHoloBlue())
        set1.colors = colors
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val data = BarData(dataSets)
        val custom = MyValueFormatter(0)
        set1.valueFormatter = custom
        data.setValueTextSize(10f)
        data.barWidth = 0.6f
        binding!!.chartBarWareHouse.data = data
        // }
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    //API bao cao tổng kho
    private fun getAPIWareHouseTotal(fromDate: String, toDate: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-report/supplier-warehouse-sesion-report?supplier_employee_id=-1&report_time=1&from_date=$fromDate&to_date=$toDate"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getWareHouseReport(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<WareHouseReportResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("SetTextI18n")
                override fun onNext(response: WareHouseReportResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        wareHouse = response
                        binding!!.totalInventoryFirstAmount.text =
                            currencyFormat(wareHouse.data.total_inventory_first_amount.toString())
                        binding!!.totalImportAmount.text =
                            currencyFormat(wareHouse.data.total_import_amount.toString())
                        binding!!.totalExportAmount.text =
                            currencyFormat(wareHouse.data.total_export_amount.toString())
                        binding!!.totalInventoryNowAmount.text =
                            currencyFormat(wareHouse.data.total_inventory_now_amount.toString())
                        binding!!.totalCancelAmount.text =
                            currencyFormat(wareHouse.data.total_cancel_amount.toString())
                        setDataBarWareHouse()
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {
    }
}