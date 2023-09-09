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
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.orderreportbyrestaurantadapter.OrderReportByRestaurantAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOrderReportByRestaurantBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.OrderReportByRestaurantResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderReportByRestaurantFragment : BaseBindingFragment<FragmentOrderReportByRestaurantBinding>(
    FragmentOrderReportByRestaurantBinding::inflate
), OnChartValueSelectedListener {
    val tagName: String = OrderReportByRestaurantFragment::class.java.name

    private var listOrderReportByRestaurantReport = OrderReportByRestaurantResponse()
    private var adapter: OrderReportByRestaurantAdapter? = null
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setBackClick(false)
        setAdapter()
        dataFrom()
        dataTo()
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        mainActivity!!.setHeader(getString(R.string.report_order_by_restaurant))
        getAPIOrderReportByRestaurantReportt(
            binding!!.fromData.text.toString(),
            binding!!.toData.text.toString()
        )
        binding!!.fromData.setOnClickListener {
            dialogFromDatePicker()
        }
        binding!!.toData.setOnClickListener {
            dialogToDatePicker()
        }
        binding!!.pigChat.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.pigChat.setNoDataTextColor(Color.BLACK)
    }
    override fun onResume() {
        super.onResume()

        getAPIOrderReportByRestaurantReportt(
            binding!!.fromData.text.toString(),
            binding!!.toData.text.toString()
        )
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
            getAPIOrderReportByRestaurantReportt(
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
            getAPIOrderReportByRestaurantReportt(
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
    fun chartPigCategoryReport() {
        binding!!.pigChat.holeRadius = 70f
        binding!!.pigChat.transparentCircleRadius = 61f
        binding!!.pigChat.setDrawCenterText(true)
        binding!!.pigChat.isDrawHoleEnabled = true
        binding!!.pigChat.rotationAngle = 90f
        binding!!.pigChat.isRotationEnabled = true
        binding!!.pigChat.setUsePercentValues(true)
        binding!!.pigChat.description.isEnabled = false
        binding!!.pigChat.setDrawEntryLabels(false)
        var nSum = BigDecimal.ZERO
            nSum += listOrderReportByRestaurantReport.data.total_amount!!.toDouble().toBigDecimal()


        binding!!.pigChat.centerText =
            getString(R.string.sum_order) + "\n" + currencyFormat(nSum.toString())
        binding!!.pigChat.setOnChartValueSelectedListener(this)
        binding!!.pigChat.animateXY(1500, 1500)
        binding!!.pigChat.setEntryLabelColor(Color.BLACK)
        binding!!.pigChat.setEntryLabelTextSize(12f)
    }
    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }
    private fun setDataPieInDay() {
        val entries = ArrayList<PieEntry>()
        /**
         * Lấy doanh thu từng danh mục add vào mảng entries
         * entries là mảng để hiển thị % trên chart
         */
            entries.add(
                PieEntry(
                    listOrderReportByRestaurantReport.data.total_amount!!.toFloat()
                )
            )

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors

        dataSet.valueLinePart1OffsetPercentage = 60f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f
        dataSet.sliceSpace = 1f

        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)

        binding!!.pigChat.data = data

//         undo all highlights
        binding!!.pigChat.highlightValues(null)

        binding!!.pigChat.invalidate()
    }
    private fun setAdapter() {
        adapter =
            mainActivity?.let { OrderReportByRestaurantAdapter() }
        binding!!.recViewOrderReportByRestaurant.adapter = adapter
        binding!!.recViewOrderReportByRestaurant.layoutManager = LinearLayoutManager(context)
        binding!!.recViewOrderReportByRestaurant.setHasFixedSize(true)
    }
    private fun getAPIOrderReportByRestaurantReportt(fromDate: String, toDate: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-report/order-by-restaurant?report_type=${TechresEnum.REPORT_TYPE}&from_date=$fromDate&to_date=$toDate"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getOrderReportByRestaurant(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<OrderReportByRestaurantResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mainActivity!!.setBackClick(true)
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: OrderReportByRestaurantResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listOrderReportByRestaurantReport = response
                        mainActivity!!.setBackClick(true)
                        adapter!!.setDataSource(
                            listOrderReportByRestaurantReport
                        )
                        chartPigCategoryReport()
                        setDataPieInDay()
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