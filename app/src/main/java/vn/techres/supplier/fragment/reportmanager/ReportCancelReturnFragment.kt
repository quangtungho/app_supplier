package vn.techres.supplier.fragment.reportmanager

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.jiang.android.indicatordialog.IndicatorBuilder
import com.jiang.android.indicatordialog.IndicatorDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.cancelreturnreportadapter.CancelReturnReportAdapter
import vn.techres.supplier.adapter.orderreportadapter.BaseViewHolder
import vn.techres.supplier.adapter.orderreportadapter.BasesAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentReportCancelReturnBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataReportCancelReturn
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.ReportCancelReturnResponse
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
class ReportCancelReturnFragment :
    BaseBindingFragment<FragmentReportCancelReturnBinding>(FragmentReportCancelReturnBinding::inflate),
    OnChartValueSelectedListener {
    val tagName: String = ReportCancelReturnFragment::class.java.name
    private val mListCalender: ArrayList<String> = ArrayList()
    var dialogCalendar: IndicatorDialog? = null
    private var adapter: CancelReturnReportAdapter? = null
    private var reportSort = 1
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    private var listCancelReturn = ArrayList<DataReportCancelReturn>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        dataFrom()
        dataTo()
        mainActivity!!.setHeader(getString(R.string.txt_cancel_return))
        mainActivity!!.setBackClick(false)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        setAdapter()
        binding!!.selectCalendar.setOnClickListener { v ->
            showDialogCalendar(
                v,
                0.1f,
                IndicatorBuilder.GRAVITY_LEFT
            )
        }
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
        getAPICancelReturn(
            TechresEnum.REPORT_TYPE_RETURN.toString().toInt(),
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
            if (reportSort == 1)
                getAPICancelReturn(
                    TechresEnum.REPORT_TYPE_RETURN.toString().toInt(),
                    binding!!.fromData.text.toString(),
                    binding!!.toData.text.toString()
                ) else (reportSort == 2)
            getAPICancelReturn(
                TechresEnum.REPORT_TYPE_CANCEL.toString().toInt(),
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
            if (reportSort == 1)
                getAPICancelReturn(
                    TechresEnum.REPORT_TYPE_RETURN.toString().toInt(),
                    binding!!.fromData.text.toString(),
                    binding!!.toData.text.toString()
                ) else (reportSort == 2)
            getAPICancelReturn(
                TechresEnum.REPORT_TYPE_CANCEL.toString().toInt(),
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
    private fun getAPICancelReturn(type: Int, dataFrom: String, dataTo: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-report/supplier-material-cancel-return-report?report_time=1&type=$type&from_date=$dataFrom&to_date=$dataTo"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListCancelReturn(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ReportCancelReturnResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mainActivity!!.setBackClick(true)
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: ReportCancelReturnResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listCancelReturn = response.data
                        mainActivity!!.setBackClick(true)
                        adapter!!.setDataSource(listCancelReturn)
                        setDataPieInDay()
                        chartPigMaterial()
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    private fun setAdapter() {
        adapter = mainActivity?.let { CancelReturnReportAdapter() }
        binding!!.recViewOrderReportByRestaurant.adapter = adapter
        binding!!.recViewOrderReportByRestaurant.layoutManager = LinearLayoutManager(context)
        binding!!.recViewOrderReportByRestaurant.setHasFixedSize(true)
    }
    private fun showDialogCalendar(v: View, vFl: Float, gravityCenter: Int) {
        mListCalender.clear()
        mListCalender.add("Nguyên liệu NH trả hàng")
        mListCalender.add("Hủy hàng")
        val resources = resources
        val dm = resources.displayMetrics
        val height = dm.heightPixels
        dialogCalendar = IndicatorBuilder(mainActivity)
            .width(500)
            .height((height * 0.5).toInt())
            .animator(R.style.dialog_exit)
            .ArrowDirection(IndicatorBuilder.TOP)
            .bgColor(Color.WHITE)
            .gravity(gravityCenter)
            .ArrowRectage(vFl)
            .radius(18)
            .layoutManager(LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false))
            .adapter(object : BasesAdapter() {
                override fun onBindView(holder: BaseViewHolder?, position: Int) {
                    val tv: TextView? = holder!!.getView(R.id.item_add)
                    tv!!.text = mListCalender[position]
                    if (position == mListCalender.size - 1) {
                        holder.setVisibility(R.id.item_line, BaseViewHolder.GONE)
                    } else {
                        holder.setVisibility(R.id.item_line, BaseViewHolder.VISIBLE)
                    }
                }

                override fun getLayoutID(position: Int): Int {
                    return R.layout.item_calendar
                }

                override fun clickable(): Boolean {
                    return true
                }

                override fun onItemClick(v: View?, position: Int) {
                    binding!!.txtCalendar.text = mListCalender[position]
                    when (position) {
                        0 -> {
                            reportSort = 1
                            getAPICancelReturn(
                                TechresEnum.REPORT_TYPE_RETURN.toString().toInt(),
                                binding!!.fromData.text.toString(),
                                binding!!.toData.text.toString()
                            )
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )
                        }
                        1 -> {
                            reportSort = 2
                            getAPICancelReturn(
                                TechresEnum.REPORT_TYPE_CANCEL.toString().toInt(),
                                binding!!.fromData.text.toString(),
                                binding!!.toData.text.toString()
                            )
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                    }
                    dialogCalendar!!.dismiss()

                }

                override fun getItemCount(): Int {
                    return mListCalender.size
                }

            }).create()
        dialogCalendar!!.setCanceledOnTouchOutside(true)
        dialogCalendar!!.show(v)
    }
    fun chartPigMaterial() {
        binding!!.pigChat.holeRadius = 70f
        binding!!.pigChat.transparentCircleRadius = 61f
        binding!!.pigChat.setDrawCenterText(true)
        binding!!.pigChat.isDrawHoleEnabled = true
        binding!!.pigChat.rotationAngle = 90f
        binding!!.pigChat.isRotationEnabled = true
        binding!!.pigChat.setUsePercentValues(true)
        binding!!.pigChat.description.isEnabled = false
        binding!!.pigChat.setDrawEntryLabels(false)
        var nSum = 0.0
        for (i in 0 until listCancelReturn.size) {
            nSum += listCancelReturn[i].total_amount.toFloat()
        }

        binding!!.pigChat.centerText =
            resources.getString(R.string.title_material) + "\n" + currencyFormat(nSum.toString())
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
        for (i in listCancelReturn.indices) {
            entries.add(
                PieEntry(
                    listCancelReturn[i].total_amount.toFloat(),
                    i
                )
            )
        }

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
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }
    override fun onNothingSelected() {
    }
}