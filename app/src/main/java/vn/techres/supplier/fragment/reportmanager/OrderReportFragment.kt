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
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.jiang.android.indicatordialog.IndicatorBuilder
import com.jiang.android.indicatordialog.IndicatorDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.orderreportadapter.BaseViewHolder
import vn.techres.supplier.adapter.orderreportadapter.BasesAdapter
import vn.techres.supplier.adapter.orderreportadapter.OrderReportAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOrderReportBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataOrderReport
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.OrderReportResponse
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
class OrderReportFragment :
    BaseBindingFragment<FragmentOrderReportBinding>(FragmentOrderReportBinding::inflate),
    OnChartValueSelectedListener {
    val tagName: String = OrderReportFragment::class.java.name
    private var adapter: OrderReportAdapter? = null
    private val mLists: ArrayList<String> = ArrayList()
    private val mICons: ArrayList<Int> = ArrayList()
    private var orderReport = DataOrderReport()
    private var orderReportSort = 1
    private var dateTo: String? = ""
    var dialog: IndicatorDialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        setAdapter()
        fragmentCreate()
        getAPIOrderReport()
        binding!!.chartBar.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartBar.setNoDataTextColor(Color.BLACK)
    }
    private fun setAdapter() {
        adapter = mainActivity?.let { OrderReportAdapter() }
        binding!!.recViewOrderReport.adapter = adapter
        binding!!.recViewOrderReport.layoutManager = LinearLayoutManager(context)
        binding!!.recViewOrderReport.setHasFixedSize(true)
    }
    private fun fragmentCreate() {
        mainActivity!!.setHeader(getString(R.string.report_order_fragment))
        mainActivity!!.setBackClick(true)
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
        getAPIOrderReport()
    }
    @SuppressLint("SetTextI18n")
    fun getDateDefault(): String {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        return dateTo.toString()
    }
    private fun showDialog(v: View, float: Float, gravityCenter: Int) {
        mLists.clear()
        mICons.clear()
        mLists.add(getString(R.string.today))
        mICons.add(R.drawable.ic_filter_today)
        mLists.add(getString(R.string.ByWeak))
        mICons.add(R.drawable.ic_filter_week)
        mLists.add(getString(R.string.ByMonth))
        mICons.add(R.drawable.ic_filter_month)
        mLists.add(getString(R.string.By3Month))
        mICons.add(R.drawable.ic_filter_three_month)
        mLists.add(getString(R.string.ByYear))
        mICons.add(R.drawable.ic_filter_year)
        mLists.add(getString(R.string.By3Year))
        mICons.add(R.drawable.ic_filter_three_year)
        mLists.add(getString(R.string.ByAllYear))
        mICons.add(R.drawable.ic_filter_yesterday)
        val resources = resources
        val dm = resources.displayMetrics
        val height = dm.heightPixels
        IndicatorBuilder(mainActivity)
            .width(500)
            .height((height * 0.7).toInt())
            .animator(R.style.dialog_exit)
            .ArrowDirection(IndicatorBuilder.TOP)
            .bgColor(Color.WHITE)
            .gravity(gravityCenter)
            .ArrowRectage(float)
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
                            orderReportSort = 1
                            getAPIOrderReport()

                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )
                        }
                        1 -> {
                            orderReportSort = 2
                            getAPIOrderReport()
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                        2 -> {
                            orderReportSort = 3
                            getAPIOrderReport()

                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                        3 -> {
                            orderReportSort = 4
                            getAPIOrderReport()
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                        4 -> {
                            orderReportSort = 5
                            getAPIOrderReport()
                            mainActivity!!.setLoading(true)
                            binding!!.totailOrder.text = orderReport.total_amount.toString()
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                        5 -> {
                            orderReportSort = 6
                            getAPIOrderReport()
                            binding!!.totailOrder.text = orderReport.total_amount.toString()
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                        6 -> {
                            orderReportSort = 8
                            getAPIOrderReport()
                            binding!!.totailOrder.text = orderReport.total_amount.toString()
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

            }).create().also { dialog = it }
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.show(v)
    }
    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }
    private fun getAPIOrderReport() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-report/order-by-time?type=$orderReportSort&to_Date=" + getDateDefault()

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getOrderReport(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<OrderReportResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
                override fun onNext(response: OrderReportResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        orderReport = response.data
                        binding!!.totailOrder.text =
                            currencyFormat(orderReport.total_amount.toString())
                        adapter!!.setDataSource(orderReport)
                        setDataBarOderReport()
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    //Chart OrderReport
    private fun setDataBarOderReport() {
        val values = ArrayList<BarEntry>()

        binding!!.chartBar.legend.isEnabled = false
        binding!!.chartBar.description.isEnabled = false
        binding!!.chartBar.setTouchEnabled(true)
        // set listeners
        binding!!.chartBar.setOnChartValueSelectedListener(this)
        binding!!.chartBar.setDrawGridBackground(false)
        binding!!.chartBar.isScaleXEnabled = true
        binding!!.chartBar.animateY(5000)
        val xAxis: XAxis = binding!!.chartBar.xAxis
        val yAxis: YAxis = binding!!.chartBar.axisRight
        yAxis.isEnabled = false
        xAxis.labelRotationAngle = -85f
        val numMapQuarter: HashMap<Int, String> = HashMap()
        when (orderReportSort) {
            1 -> {
                orderReport.report.forEachIndexed { index, dataReportOrder ->
                    numMapQuarter[index] =
                        String.format("%s - %s", dataReportOrder.time, dataReportOrder.date)
                }
                xAxis.labelCount = 24
            }
            2 -> {
                orderReport.report.forEachIndexed { index, dataReportOrder ->
                    numMapQuarter[index] =
                        dataReportOrder.date
                }
                xAxis.labelCount = 7
            }
            3 -> {
                orderReport.report.forEachIndexed { index, dataReportOrder ->
                    numMapQuarter[index] =
                        dataReportOrder.date
                }

            }
            4 -> {
                orderReport.report.forEachIndexed { index, dataReportOrder ->
                    numMapQuarter[index] =
                        dataReportOrder.date
                }
                xAxis.labelCount = 3
            }
            5 -> {
                orderReport.report.forEachIndexed { index, dataReportOrder ->
                    numMapQuarter[index] =
                        dataReportOrder.date
                }
                xAxis.labelCount = 12
            }
            6 -> {
                orderReport.report.forEachIndexed { index, dataReportOrder ->
                    numMapQuarter[index] =
                        dataReportOrder.date
                }
                xAxis.labelCount = 3
            }
            8 -> {
                orderReport.report.forEachIndexed { index, dataReportOrder ->
                    numMapQuarter[index] =
                        dataReportOrder.date
                }

            }
        }

        xAxis.valueFormatter = IAxisValueFormatter { value: Float, axis: AxisBase? ->
            numMapQuarter[value.toInt()]
        }
        orderReport.report.forEachIndexed { index, dataReportOrder ->
            values.add(
                BarEntry(
                    (index).toFloat(),
                    (dataReportOrder.amount / 10000)
                )
            )
        }
        binding!!.chartBar.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val set1: BarDataSet
        if (binding!!.chartBar.data != null &&
            binding!!.chartBar.data.dataSetCount > 0
        ) {
            set1 = binding!!.chartBar.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            binding!!.chartBar.data.notifyDataChanged()
            binding!!.chartBar.notifyDataSetChanged()
        } else {

            set1 = BarDataSet(values, "")
            set1.setDrawIcons(false)

            val colors = ArrayList<Int>()
            colors.add(ColorTemplate.getHoloBlue())
            set1.colors = colors
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueTextSize(7f)
            data.barWidth = 0.9f
            binding!!.chartBar.data = data
        }
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }
    override fun onNothingSelected() {
    }
}