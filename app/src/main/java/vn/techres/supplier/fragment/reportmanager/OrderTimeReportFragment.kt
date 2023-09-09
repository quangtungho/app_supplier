package vn.techres.supplier.fragment.reportmanager

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
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
import com.jiang.android.indicatordialog.IndicatorBuilder
import com.jiang.android.indicatordialog.IndicatorDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.orderreportadapter.BaseViewHolder
import vn.techres.supplier.adapter.orderreportadapter.BasesAdapter
import vn.techres.supplier.adapter.ordertimereportadapter.OrderTimeReportAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOrderTimeReportBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.OrderTimeReportResponse
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
class OrderTimeReportFragment :
    BaseBindingFragment<FragmentOrderTimeReportBinding>(FragmentOrderTimeReportBinding::inflate),
    OnChartValueSelectedListener {
    val tagName: String = OrderTimeReportFragment::class.java.name
    private var adapter: OrderTimeReportAdapter? = null
    private val mLists: ArrayList<String> = ArrayList()
    private val mICons: ArrayList<Int> = ArrayList()
    private var listOrderTimeReport = OrderTimeReportResponse()
    private var orderTimeReportSort = 1
    private var dateTo: String? = ""
    var dialog: IndicatorDialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        setAdapter()
        fragmentCreate()
        mainActivity!!.setBackClick(false)
        getAPIOrderTimeReport()
        binding!!.chartPieOrderTimeReport.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartPieOrderTimeReport.setNoDataTextColor(Color.BLACK)
    }
    private fun setAdapter() {
        adapter = mainActivity?.let { OrderTimeReportAdapter() }
        binding!!.recViewOrderTimeReport.adapter = adapter
        binding!!.recViewOrderTimeReport.layoutManager = LinearLayoutManager(context)
        binding!!.recViewOrderTimeReport.setHasFixedSize(true)
    }
    private fun fragmentCreate() {
        mainActivity!!.setHeader(getString(R.string.report_order_time_fragment))
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        binding!!.selectCalendar.setOnClickListener { v ->
            showDialog(
                v,
                0.1f,
                IndicatorBuilder.GRAVITY_LEFT
            )
        }
    }
    override fun onResume() {
        super.onResume()
        getAPIOrderTimeReport()
    }
    private fun getDateDefault(): String {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        return dateTo.toString()
    }
    private fun showDialog(v: View, vFl: Float, gravityCenter: Int) {
        mLists.clear()
        mICons.clear()
        mLists.add(getString(R.string.today))
        mICons.add(R.drawable.ic_filter_today)
        mLists.add(getString(R.string.ByThisMonth))
        mICons.add(R.drawable.ic_filter_week)
        mLists.add(getString(R.string.ByLastMonth))
        mICons.add(R.drawable.ic_filter_month)
        mLists.add(getString(R.string.ByThisYear))
        mICons.add(R.drawable.ic_filter_three_month)
        mLists.add(getString(R.string.ByLastYear))
        mICons.add(R.drawable.ic_filter_year)
        val resources = resources
        val dm = resources.displayMetrics
        val height = dm.heightPixels
        dialog = IndicatorBuilder(mainActivity)
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
                    tv!!.text = mLists[position]
                    tv.setCompoundDrawablesWithIntrinsicBounds(mICons[position], 0, 0, 0)
                    if (position == mLists.size - 1) {
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
                    binding!!.txtCalendar.text = mLists[position]
                    binding!!.icCalendar.setImageResource(mICons[position])
                    when (position) {
                        0 -> {
                            orderTimeReportSort = 1
                            getAPIOrderTimeReport()
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )
                        }
                        1 -> {
                            orderTimeReportSort = 2
                            getAPIOrderTimeReport()
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )
                        }
                        2 -> {
                            orderTimeReportSort = 3
                            getAPIOrderTimeReport()
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )
                        }
                        3 -> {
                            orderTimeReportSort = 4
                            getAPIOrderTimeReport()
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )
                        }
                        4 -> {
                            orderTimeReportSort = 5
                            getAPIOrderTimeReport()
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )
                        }
                    }
                    dialog!!.dismiss()

                }

                override fun getItemCount(): Int {
                    return mLists.size
                }

            }).create()
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.show(v)
    }
    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }
    fun chartPigOrderTimeReport() {
        binding!!.chartPieOrderTimeReport.holeRadius = 70f
        binding!!.chartPieOrderTimeReport.transparentCircleRadius = 61f
        binding!!.chartPieOrderTimeReport.setDrawCenterText(true)
        binding!!.chartPieOrderTimeReport.isDrawHoleEnabled = true
        binding!!.chartPieOrderTimeReport.rotationAngle = 90f
        binding!!.chartPieOrderTimeReport.isRotationEnabled = true
        binding!!.chartPieOrderTimeReport.setUsePercentValues(true)
        binding!!.chartPieOrderTimeReport.description.isEnabled = false
        binding!!.chartPieOrderTimeReport.setDrawEntryLabels(false)
        var nSum = 0.0
        for (i in 0 until listOrderTimeReport.data.size) {
            nSum += listOrderTimeReport.data[i].total_amount!!
        }

        binding!!.chartPieOrderTimeReport.centerText =
            getString(R.string.sum_order) + "\n" + currencyFormat(nSum.toString())
        binding!!.chartPieOrderTimeReport.setOnChartValueSelectedListener(this)
        binding!!.chartPieOrderTimeReport.animateXY(1500, 1500)
        binding!!.chartPieOrderTimeReport.setEntryLabelColor(Color.BLACK)
        binding!!.chartPieOrderTimeReport.setEntryLabelTextSize(12f)
    }
    private fun setDataPieOrderTimeReport() {
        val entries = ArrayList<PieEntry>()
        /**
         * Lấy doanh thu từng danh mục add vào mảng entries
         * entries là mảng để hiển thị % trên chart
         */
        for (i in listOrderTimeReport.data.indices) {
            entries.add(
                PieEntry(
                    listOrderTimeReport.data[i].total_amount!!,
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

        binding!!.chartPieOrderTimeReport.data = data

//         undo all highlights
        binding!!.chartPieOrderTimeReport.highlightValues(null)

        binding!!.chartPieOrderTimeReport.invalidate()
    }
    private fun getAPIOrderTimeReport() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-report/order-by-date?report_type=$orderTimeReportSort&to_date=" + getDateDefault()

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getOrderTimeReport(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<OrderTimeReportResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mainActivity!!.setBackClick(true)
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: OrderTimeReportResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listOrderTimeReport = response
                        adapter!!.setDataSource(response)
                        adapter!!.notifyDataSetChanged()
                        chartPigOrderTimeReport()
                        setDataPieOrderTimeReport()
                        mainActivity!!.setBackClick(true)
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }
    override fun onNothingSelected() {
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}