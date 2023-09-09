package vn.techres.supplier.fragment.navigate

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOverviewBinding
import vn.techres.supplier.fragment.reportmanager.OverViewWareHouseFragment
import vn.techres.supplier.helper.*
import vn.techres.supplier.model.datamodel.*
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.*
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

class OverviewFragment :
        BaseBindingFragment<FragmentOverviewBinding>(FragmentOverviewBinding::inflate),
        OnChartValueSelectedListener {

    val tagName: String = OverviewFragment::class.java.name
    private var timer: Timer? = null
    private var type: Int? = 0
    private var event: Int? = 0
    private var position: Int? = 0
    private var timerTask: TimerTask? = null
    val handler = Handler()
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    private var dataOrder = DataOrderInDay()
    private var revenueThisWeek = ReportRevenueWeek()
    private var revenueLastWeek = ReportRevenueWeek()
    private var revenueThisMonth = ReportRevenueThisMonth()
    private var revenueThreeMonths = ReportRevenueThreeMonths()
    private var revenueThisYear = ReportRevenueThisYear()
    private var revenueLastYear = ReportRevenueLastYear()
    private var wareHouse = DataWareHouseReport()
    private var overViewWareHouseFragment = OverViewWareHouseFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainActivity!!.setLoading(false)
        super.onViewCreated(view, savedInstanceState)
        mainActivity!!.setLoading(false)
        onBody()
        getAPIOverViewOrderInDay()
        getAPIRevenueThisWeek()
        getAPIRevenueThisMonth()
        getAPIRevenueThreeMonths()
        getAPIRevenueThisYear()
        getAPIRevenueLastYear()
        dataFromOrderReport()
        dataToOrderReport()
        getAPIWareHouseTotal(dateFrom.toString(), dateTo.toString())
        onWareHouse()

        binding!!.chartLineThisWeek.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartLineThisWeek.setNoDataTextColor(Color.BLACK)
        binding!!.chartBarWareHouse.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartBarWareHouse.setNoDataTextColor(Color.BLACK)
        binding!!.chartBarThreeMonths.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartBarThreeMonths.setNoDataTextColor(Color.BLACK)
        binding!!.chartBarThisMonth.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartBarThisMonth.setNoDataTextColor(Color.BLACK)
        binding!!.chartBarThisYear.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartBarThisYear.setNoDataTextColor(Color.BLACK)
        binding!!.chartBarLastYear.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartBarLastYear.setNoDataTextColor(Color.BLACK)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onBody() {
        fragmentCreate()
        onClickFragmentAccountMenu()
    }

    override fun onResume() {
        super.onResume()
        getAPIOverViewOrderInDay()
        getAPIRevenueThisWeek()
        getAPIRevenueThisMonth()
        getAPIRevenueThreeMonths()
        getAPIRevenueThisYear()
        getAPIRevenueLastYear()
        dataFromOrderReport()
        dataToOrderReport()
        getAPIWareHouseTotal(dateFrom.toString(), dateTo.toString())
        onWareHouse()
    }

    private fun dataFromOrderReport() {
        val sdfTo = SimpleDateFormat(getString(R.string.mmyyyy), Locale.getDefault())
        dateFrom = "01/" + sdfTo.format(Date())

    }

    private fun dataToOrderReport() {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
    }

    private fun onWareHouse() {
        binding!!.lnWareHouse.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                    {
                        activity?.supportFragmentManager
                                ?.beginTransaction()
                                ?.replace(R.id.nav_host, OverViewWareHouseFragment())
                                ?.addToBackStack(overViewWareHouseFragment.tagName)
                                ?.commit()
                    }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    private fun startTimer() {
        timer = Timer()
        initializeTimerTask()
        timer!!.schedule(timerTask, 1000, 1000)
    }

    private fun stopTimerTask() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    private fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            @SuppressLint("SimpleDateFormat")
            override fun run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post {
                    //get the current timeStamp
                    val calendar = Calendar.getInstance()
                    val sdf = SimpleDateFormat(getString(R.string.hhmmss))
                    val date = sdf.format(calendar.time)
                    binding!!.txtTime.text = date
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimerTask()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun fragmentCreate() {
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        mainActivity!!.setBackClick(false)
        mainActivity!!.setHideHeader(false)
        mainActivity!!.setHeader(getString(R.string.title_overview_fragment))
        binding!!.nameUser.text = CurrentUser.getCurrentUser(mainActivity!!)!!.name
        binding!!.nameUser.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding!!.nameUser.marqueeRepeatLimit = -1
        binding!!.nameUser.isSelected = true
        binding!!.userName.text = CurrentUser.getCurrentUser(mainActivity!!)!!.name
        binding!!.userName.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding!!.userName.marqueeRepeatLimit = -1
        binding!!.userName.isSelected = true
        AppUtils.callPhotoAvatar(
                CurrentUser.getCurrentUser(mainActivity!!)!!.avatar,
                binding!!.imgAvatar
        )
        //        setSwipeBack(true);
        binding!!.txtDate.text = DateUtils.todayDateString
        startTimer()
        //Lấy ngày hiện tại
        val dateToday = SimpleDateFormat(getString(R.string.ddmmyyyy))
        val monthToday = SimpleDateFormat(getString(R.string.mmyyyy))
        val currentDateToday = dateToday.format(Date())
        val currentMonthToday = monthToday.format(Date())
        binding!!.txtDateToday.text = " - $currentDateToday"
        binding!!.txtMonthToday.text = " - $currentMonthToday"
    }

    private fun onClickFragmentAccountMenu() {
        binding!!.controllAccount.setOnClickListener {
            val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
            binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
            Handler(Looper.getMainLooper()).postDelayed(
                    {
                        cacheManager.put(
                                TechresEnum.KEY_BACK.toString(),
                                TechresEnum.ACCOUNTMENU_ID_FRAGMENT.toString()
                        )
                        activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.nav_host, MainFragment())?.commitNow()
                    }, TechresEnum.TIME_50.toString().toLong()
            )
        }
    }
    //--------------CHART--------------CHART--------------CHART--------------CHART--------------CHART

    //Chart Line - Báo cáo doanh thu trong tuần hiện tại
    private fun lineChartThisWeek(dataLine1: ReportRevenueWeek, dataLine2: ReportRevenueWeek) {
        binding!!.chartLineThisWeek.legend.isEnabled = true
        binding!!.chartLineThisWeek.description.isEnabled = false
        binding!!.chartBarThisMonth.setPinchZoom(false)
        binding!!.chartLineThisWeek.setTouchEnabled(true)
        // set listeners
        binding!!.chartLineThisWeek.setOnChartValueSelectedListener(this)
        binding!!.chartLineThisWeek.setDrawGridBackground(false)
        binding!!.chartLineThisWeek.isScaleXEnabled = true
        binding!!.chartLineThisWeek.animateY(5000)
        binding!!.chartLineThisWeek.axisRight.setDrawGridLines(false)
        binding!!.chartLineThisWeek.axisLeft.setDrawGridLines(false)
        binding!!.chartLineThisWeek.xAxis.setDrawGridLines(false)
        val xAxis: XAxis = binding!!.chartLineThisWeek.xAxis
        binding!!.chartLineThisWeek.axisRight.isEnabled = false
        val numMap = HashMap<Int, String>()
        numMap[0] = getString(R.string.txt_monday)
        numMap[1] = getString(R.string.txt_tuesday)
        numMap[2] = getString(R.string.txt_wednesday)
        numMap[3] = getString(R.string.txt_thursday)
        numMap[4] = getString(R.string.txt_friday)
        numMap[5] = getString(R.string.txt_saturday)
        numMap[6] = getString(R.string.txt_sunday)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setValueFormatter { value, _ -> numMap[value.toInt()] }
        setDataLine(dataLine1, dataLine2)
    }

    private fun setDataLine(dataSet1: ReportRevenueWeek, dataSet2: ReportRevenueWeek) {
//        val custom: IAxisValueFormatter = MyAxisValueFormatter(type!!)
        val leftAxis: YAxis = binding!!.chartLineThisWeek.axisLeft
        val yAxis: YAxis = binding!!.chartLineThisWeek.axisRight
        yAxis.isEnabled = false
//        leftAxis.valueFormatter = custom
        val valuesThisWeek = ArrayList<Entry>()
        dataSet1.report.forEachIndexed { index, revenueWeek ->
            valuesThisWeek.add(
                    Entry(
                            index.toFloat(),
                            revenueWeek.supplier_revenue.toFloat() / 1000000
                    )
            )
        }
        val valuesLastWeek = ArrayList<Entry>()
        dataSet2.report.forEachIndexed { index, revenueWeek ->
            valuesLastWeek.add(
                    Entry(
                            index.toFloat(),
                            revenueWeek.supplier_revenue.toFloat() / 1000000
                    )
            )
        }
        val setThisWeek = LineDataSet(valuesThisWeek, "Tuần này")
        setThisWeek.color = Color.parseColor("#0072CB")
        val setLastWeek = LineDataSet(valuesLastWeek, "Tuần trước")
        setLastWeek.color = Color.parseColor("#FFA233")
        val data = ArrayList<ILineDataSet>()
        data.add(setThisWeek)
        data.add(setLastWeek)
        binding!!.chartLineThisWeek.data = LineData(data)
    }


    //Chart Bar - Báo cáo
// doanh thu 30 ngày
    private fun setDataBarThisMonth() {
//        val custom: IAxisValueFormatter = MyAxisValueFormatter(type!!)
        val leftAxis: YAxis = binding!!.chartBarThisMonth.axisLeft
        val yAxis: YAxis = binding!!.chartBarThisMonth.axisRight
        yAxis.isEnabled = false
//        leftAxis.valueFormatter = custom
        val values = ArrayList<BarEntry>()
        binding!!.chartBarThisMonth.legend.isEnabled = false
        binding!!.chartBarThisMonth.description.isEnabled = false
        binding!!.chartBarThisMonth.setPinchZoom(false)
        binding!!.chartBarThisMonth.setTouchEnabled(true)
        // set listeners
        binding!!.chartBarThisMonth.setOnChartValueSelectedListener(this)
        binding!!.chartBarThisMonth.setDrawGridBackground(false)
        binding!!.chartBarThisMonth.isScaleXEnabled = true
        binding!!.chartBarThisMonth.animateY(5000)
        binding!!.chartBarThisMonth.axisRight.setDrawGridLines(false)
        binding!!.chartBarThisMonth.axisLeft.setDrawGridLines(false)
        binding!!.chartBarThisMonth.xAxis.setDrawGridLines(false)
        binding!!.chartBarThisMonth.xAxis.labelCount = revenueThisMonth.report.size
        binding!!.chartBarThisMonth.zoom(1.12f, 0f, 1.2f, 0f)
        val xAxis: XAxis = binding!!.chartBarThisMonth.xAxis
        xAxis.labelRotationAngle = -65f

        for (i in revenueThisMonth.report.indices) {
            values.add(
                    BarEntry(
                            (i + 1).toFloat(),
                            (revenueThisMonth.report[i].supplier_revenue).toFloat() / 1000000
                    )
            )
        }
        binding!!.chartBarThisMonth.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val set1: BarDataSet
        if (binding!!.chartBarThisMonth.data != null &&
                binding!!.chartBarThisMonth.data.dataSetCount > 0
        ) {
            set1 = binding!!.chartBarThisMonth.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            binding!!.chartBarThisMonth.data.notifyDataChanged()
            binding!!.chartBarThisMonth.notifyDataSetChanged()
        } else {

            set1 = BarDataSet(values, "")
            set1.setDrawIcons(false)
            val colors = ArrayList<Int>()
            colors.add(ColorTemplate.getHoloBlue())
            set1.colors = colors
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
//            val customChart = MyValueFormatter(0)
//            set1.valueFormatter = customChart
            data.setValueTextSize(10f)
            data.barWidth = 0.6f
            binding!!.chartBarThisMonth.data = data
        }
    }

    //Chart Bar - Báo cáo doanh thu 3 tháng gần nhất
    private fun setDataBarThreeMonths() {
//        val custom: IAxisValueFormatter = MyAxisValueFormatter(type!!)
        val leftAxis: YAxis = binding!!.chartBarThreeMonths.axisLeft
        val yAxis: YAxis = binding!!.chartBarThreeMonths.axisRight
        yAxis.isEnabled = false
//        leftAxis.valueFormatter = custom
        val values = ArrayList<BarEntry>()
        binding!!.chartBarThreeMonths.legend.isEnabled = false
        binding!!.chartBarThreeMonths.description.isEnabled = false
        binding!!.chartBarThreeMonths.setTouchEnabled(true)
        // set listeners
        binding!!.chartBarThreeMonths.setOnChartValueSelectedListener(this)
        binding!!.chartBarThreeMonths.setDrawGridBackground(false)
        binding!!.chartBarThreeMonths.isScaleXEnabled = true
        binding!!.chartBarThreeMonths.animateY(5000)
        binding!!.chartBarThreeMonths.axisRight.setDrawGridLines(false)
        binding!!.chartBarThreeMonths.axisLeft.setDrawGridLines(false)
        binding!!.chartBarThreeMonths.xAxis.setDrawGridLines(false)
        for (i in revenueThreeMonths.report.indices) {
            values.add(
                    BarEntry(
                            (i).toFloat(),
                            (revenueThreeMonths.report[i].supplier_revenue).toFloat() / 1000000
                    )
            )
        }
        binding!!.chartBarThreeMonths.xAxis
        val listCount = ArrayList<String>()
        listCount.add(getString(R.string.month) + " " + revenueThreeMonths.report[0].time.toString())
        listCount.add(getString(R.string.month) + " " + revenueThreeMonths.report[1].time.toString())
        listCount.add(getString(R.string.month) + " " + revenueThreeMonths.report[2].time.toString())
        val xAxis: XAxis = binding!!.chartBarThreeMonths.xAxis
        xAxis.labelCount = 3

        binding!!.chartBarThreeMonths.xAxis.valueFormatter = IndexAxisValueFormatter(listCount)
        binding!!.chartBarThreeMonths.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val set1: BarDataSet
        if (binding!!.chartBarThreeMonths.data != null && binding!!.chartBarThreeMonths.data.dataSetCount > 0) {
            if (binding!!.chartBarThreeMonths.data.getDataSetByIndex(1) != null) {
                set1 = binding!!.chartBarThreeMonths.data.getDataSetByIndex(1) as BarDataSet
                set1.values = values
            }
        } else {
            set1 = BarDataSet(values, "")
            set1.setDrawIcons(false)
            val colors = ArrayList<Int>()
            colors.add(ColorTemplate.getHoloBlue())
            set1.colors = colors
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
//            val customChart = MyValueFormatter(0)
//            set1.valueFormatter = customChart
            data.setValueTextSize(10f)
            data.barWidth = 0.6f
            binding!!.chartBarThreeMonths.data = data
        }

    }

    //Chart Bar - Báo cáo doanh thu năm hiện tại
    private fun setDataBarThisYear() {
//        val custom: IAxisValueFormatter = MyAxisValueFormatter(type!!)
        val leftAxis: YAxis = binding!!.chartBarThisYear.axisLeft
        val yAxis: YAxis = binding!!.chartBarThisYear.axisRight
        yAxis.isEnabled = false
//
//        leftAxis.valueFormatter = custom
        val values = ArrayList<BarEntry>()
        binding!!.chartBarThisYear.legend.isEnabled = false
        binding!!.chartBarThisYear.description.isEnabled = false
        binding!!.chartBarThisYear.setTouchEnabled(true)
        // set listeners
        binding!!.chartBarThisYear.setOnChartValueSelectedListener(this)
        binding!!.chartBarThisYear.setDrawGridBackground(false)
        binding!!.chartBarThisYear.isScaleXEnabled = true
        binding!!.chartBarThisYear.animateY(5000)

        binding!!.chartBarThisYear.axisRight.setDrawGridLines(false)
        binding!!.chartBarThisYear.axisLeft.setDrawGridLines(false)
        binding!!.chartBarThisYear.xAxis.setDrawGridLines(false)
        for (i in revenueThisYear.report.indices) {
            values.add(
                    BarEntry(
                            (i + 1).toFloat(),
                            (revenueThisYear.report[i].supplier_revenue).toFloat() / 1000000
                    )
            )
        }
        binding!!.chartBarThisYear.xAxis
        val listCount = ArrayList<String>()
        listCount.add(getString(R.string.monthT) + revenueThisYear.toString())
        val xAxis: XAxis = binding!!.chartBarThisYear.xAxis
        xAxis.labelCount = 12
        binding!!.chartBarThisYear.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val set1: BarDataSet
        if (binding!!.chartBarThisYear.data != null &&
                binding!!.chartBarThisYear.data.dataSetCount > 0
        ) {
            if (binding!!.chartBarThisYear.data.getDataSetByIndex(1) != null) {
                set1 = binding!!.chartBarThisYear.data.getDataSetByIndex(1) as BarDataSet
                set1.values = values
                binding!!.chartBarThisYear.data.notifyDataChanged()
                binding!!.chartBarThisYear.notifyDataSetChanged()
            }
        } else {
            set1 = BarDataSet(values, "")
            set1.setDrawIcons(false)
            val colors = ArrayList<Int>()
            colors.add(ColorTemplate.getHoloBlue())
            set1.colors = colors
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
//            val customChart = MyValueFormatter(0)
//            set1.valueFormatter = customChart
            data.setValueTextSize(7f)
            data.barWidth = 0.9f
            binding!!.chartBarThisYear.data = data

        }
    }

    //Chart Bar - Báo cáo doanh thu năm trước
    private fun setDataBarLastYear() {
//        val custom: IAxisValueFormatter = MyAxisValueFormatter(type!!)

        val leftAxis: YAxis = binding!!.chartBarLastYear.axisLeft
        val yAxis: YAxis = binding!!.chartBarLastYear.axisRight
        yAxis.isEnabled = false
//
//        leftAxis.valueFormatter = custom
        val values = ArrayList<BarEntry>()
        binding!!.chartBarLastYear.legend.isEnabled = false
        binding!!.chartBarLastYear.description.isEnabled = false
        binding!!.chartBarLastYear.setTouchEnabled(true)
        // set listeners
        binding!!.chartBarLastYear.setOnChartValueSelectedListener(this)
        binding!!.chartBarLastYear.setDrawGridBackground(false)
        binding!!.chartBarLastYear.isScaleXEnabled = true
        binding!!.chartBarLastYear.animateY(5000)

        binding!!.chartBarLastYear.axisRight.setDrawGridLines(false)
        binding!!.chartBarLastYear.axisLeft.setDrawGridLines(false)
        binding!!.chartBarLastYear.xAxis.setDrawGridLines(false)

        for (i in revenueLastYear.report.indices) {
            values.add(
                    BarEntry(
                            (i + 1).toFloat(),
                            (revenueLastYear.report[i].supplier_revenue).toFloat() / 1000000
                    )
            )
        }
        binding!!.chartBarLastYear.xAxis
        val listCount = ArrayList<String>()
        listCount.add(getString(R.string.monthT) + revenueLastYear.toString())
        val xAxis: XAxis = binding!!.chartBarLastYear.xAxis
        xAxis.labelCount = 12
        binding!!.chartBarLastYear.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val set1: BarDataSet
        if (binding!!.chartBarLastYear.data != null &&
                binding!!.chartBarLastYear.data.dataSetCount > 0
        ) {
            if (binding!!.chartBarLastYear.data.getDataSetByIndex(1) != null) {
                set1 = binding!!.chartBarLastYear.data.getDataSetByIndex(1) as BarDataSet
                set1.values = values
                binding!!.chartBarLastYear.data.notifyDataChanged()
                binding!!.chartBarLastYear.notifyDataSetChanged()
            }
        } else {
            set1 = BarDataSet(values, "")
            set1.setDrawIcons(false)
            val colors = ArrayList<Int>()
            colors.add(ColorTemplate.getHoloBlue())
            set1.colors = colors
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
//            val customChart = MyValueFormatter(0)
//            set1.valueFormatter = customChart
            data.setValueTextSize(7f)
            data.barWidth = 0.9f
            binding!!.chartBarLastYear.data = data

        }
    }

    //Chart Bar - Báo cáo  tổng kho
    private fun setDataBarWareHouse() {
        val listCount = ArrayList<String>()
        listCount.add(0, getString(R.string.survive_head))
        listCount.add(1, getString(R.string.title_warehouse))
        listCount.add(2, getString(R.string.title_wexport))
        listCount.add(3, getString(R.string.current_inventory))
        listCount.add(4, getString(R.string.txt_cancels))
        val xAxis: XAxis = binding!!.chartBarWareHouse.xAxis
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)
        xAxis.labelRotationAngle = -65f
        xAxis.textSize = 10f
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setValueFormatter { value, _ -> listCount[value.toInt() % listCount.size] }
        val values = ArrayList<BarEntry>()
        values.add(
                0,
                BarEntry(
                        (1).toFloat(),
                        wareHouse.total_inventory_first_amount!!.toFloat() / 1000000
                )
        )
        values.add(
                1,
                BarEntry((2).toFloat(), wareHouse.total_import_amount!!.toFloat() / 1000000)
        )
        values.add(
                2,
                BarEntry((3).toFloat(), wareHouse.total_export_amount!!.toFloat() / 1000000)
        )
        values.add(
                3,
                BarEntry(
                        (4).toFloat(),
                        wareHouse.total_inventory_now_amount!!.toFloat() / 1000000
                )
        )
        values.add(
                4,
                BarEntry((5).toFloat(), wareHouse.total_cancel_amount!!.toFloat() / 1000000)
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
        val set1: BarDataSet
        if (binding!!.chartBarWareHouse.data != null &&
                binding!!.chartBarWareHouse.data.dataSetCount > 0
        ) {
            if (binding!!.chartBarWareHouse.data.getDataSetByIndex(1) != null) {
                set1 = binding!!.chartBarWareHouse.data.getDataSetByIndex(1) as BarDataSet
                set1.values = values
                binding!!.chartBarWareHouse.data.notifyDataChanged()
                binding!!.chartBarWareHouse.notifyDataSetChanged()
            }
        } else {
            set1 = BarDataSet(values, "")
            set1.setDrawIcons(false)
            val colors = ArrayList<Int>()
            colors.add(ColorTemplate.getHoloBlue())
            set1.colors = colors
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
//            val custom = MyValueFormatter(0)
//            set1.valueFormatter = custom
            data.setValueTextSize(10f)
            data.barWidth = 0.6f
            binding!!.chartBarWareHouse.data = data
        }
    }

    //--------------API---------------API---------------API--------------API--------------API----------
//API đơn hàng trong ngày new
    private fun getAPIOverViewOrderInDay() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-report/supplier_order_quantity_order_report?report_time=${TechresEnum.REPORT_TIME}"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getOverViewOrderInDay(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<OverViewOrderInDayResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: OverViewOrderInDayResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            //da giao / chua giao
                            dataOrder = response.data
                            binding!!.txtTotalOrder.text =
                                    currencyFormat(dataOrder.total_all_order_amount.toString())
                            binding!!.txtOrderDelivered.text = dataOrder.order_delivered.toString()
                            binding!!.txtOrderProcessing.text =
                                    dataOrder.order_processing.toString()
                            binding!!.totalOrder.text =
                                    dataOrder.total_order.toString()

                            binding!!.waitRestaurant.text =
                                    dataOrder.waiting_confirm.toString()


                            binding!!.orderDelivered.text =
                                    dataOrder.order_delivered.toString()
                            binding!!.orderNotDelivered.text =
                                    dataOrder.order_not_delivered.toString()
                            binding!!.returnOrder.text =
                                    dataOrder.return_order.toString()
                            //da thu / cong no/ tong tien
                            binding!!.revenueDone.text =
                                    currencyFormat(dataOrder.total_amount_paid.toString())


                            binding!!.revenueDebt.text =
                                    currencyFormat(dataOrder.debt.toString())


                            binding!!.totalAmount.text =
                                    currencyFormat(dataOrder.total_order_amount.toString())

//                        position!! += 1
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }

                    }
                })
    }

    //API doanh thu trong tuần
    private fun getAPIRevenueThisWeek() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-report/order-revenue-total-report?report_type=${TechresEnum.REPORT_TYPE_THIS_WEEK}"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getRevenueWeek(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RevenueWeekResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: RevenueWeekResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            revenueThisWeek = response.data
                            binding!!.totailThisWeek.text =
                                    currencyFormat(revenueThisWeek.total_revenue.toString())
                            getAPIRevenueLastWeek()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    private fun getAPIRevenueLastWeek() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-report/order-revenue-total-report?report_type=${TechresEnum.REPORT_TYPE_LAST_WEEK}"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getRevenueWeek(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RevenueWeekResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}
                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: RevenueWeekResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            revenueLastWeek = response.data
                            binding!!.totailLastWeek.text =
                                    currencyFormat(revenueLastWeek.total_revenue.toString())
                            lineChartThisWeek(revenueThisWeek, revenueLastWeek)
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    //API doanh thu tháng hiện tại
    private fun getAPIRevenueThisMonth() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-report/order-revenue-total-report?report_type=${TechresEnum.REPORT_TYPE_THIS_MONTH}"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getRevenueThisMonth(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RevenueThisMonthResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: RevenueThisMonthResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            revenueThisMonth = response.data
                            binding!!.totailMonth.text =
                                    currencyFormat(revenueThisMonth.total_revenue.toString())
                            setDataBarThisMonth()
                            binding!!.totalRevenue.text =
                                    currencyFormat(revenueThisMonth.total_revenue.toString())
                            binding!!.totalCost.text =
                                    currencyFormat(revenueThisMonth.total_cost.toString())
                            binding!!.totalProfit.text =
                                    currencyFormat(revenueThisMonth.total_profit.toString())
                            binding!!.totalTiSuat.text =
                                    currencyFormat(revenueThisMonth.profit_percent.toString())
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    //API doanh thu 3 tháng gần nhất
    private fun getAPIRevenueThreeMonths() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-report/order-revenue-total-report?report_type=${TechresEnum.REPORT_TYPE_THREE_MONTHS}"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getRevenueThreeMonths(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RevenueThreeMonthsResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: RevenueThreeMonthsResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            revenueThreeMonths = response.data
                            binding!!.totailThreeMonth.text =
                                    currencyFormat(revenueThreeMonths.total_revenue.toString())
                            setDataBarThreeMonths()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    //API doanh thu năm hiện tại
    private fun getAPIRevenueThisYear() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-report/order-revenue-total-report?report_type=${TechresEnum.REPORT_TYPE_THIS_YEAR}"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getRevenueThisYear(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RevenueThisYearResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        Toast.makeText(
                                requireActivity(),
                                getString(R.string.onError),
                                Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: RevenueThisYearResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            revenueThisYear = response.data
                            binding!!.totailThisYear.text =
                                    currencyFormat(revenueThisYear.total_revenue.toString())
                            setDataBarThisYear()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    //API doanh thu năm trước đó
    private fun getAPIRevenueLastYear() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-report/order-revenue-total-report?report_type=${TechresEnum.REPORT_TYPE_LAST_YEAR}"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getRevenueLastYear(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RevenueLastYearResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: RevenueLastYearResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            revenueLastYear = response.data
                            binding!!.totailLastYear.text =
                                    currencyFormat(revenueLastYear.total_revenue.toString())
                            setDataBarLastYear()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
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

                            wareHouse = response.data
                            binding!!.totalInventoryFirstAmount.text =
                                    currencyFormat(wareHouse.total_inventory_first_amount.toString())
                            binding!!.totalImportAmount.text =
                                    currencyFormat(wareHouse.total_import_amount.toString())
                            binding!!.totalExportAmount.text =
                                    currencyFormat(wareHouse.total_export_amount.toString())
                            binding!!.totalInventoryNowAmount.text =
                                    currencyFormat(wareHouse.total_inventory_now_amount.toString())
                            binding!!.totalCancelAmount.text =
                                    currencyFormat(wareHouse.total_cancel_amount.toString())
                            setDataBarWareHouse()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    companion object {
        @JvmStatic
        fun newInstance() = OverviewFragment().apply {
            arguments = Bundle().apply { }
        }
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

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {
    }
}