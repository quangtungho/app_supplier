package vn.techres.supplier.fragment.reportmanager

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
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
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentReportOrderAmountBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.MyValueFormatter
import vn.techres.supplier.model.datamodel.DataOrderAmount
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.ReportOrderAmountResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class ReportOrderAmountFragment :
    BaseBindingFragment<FragmentReportOrderAmountBinding>(FragmentReportOrderAmountBinding::inflate),
    OnChartValueSelectedListener {
    val tagName: String = ReportOrderAmountFragment::class.java.name
    private var dataOrderAmount = DataOrderAmount()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setHeader(getString(R.string.report_order_amount))
        mainActivity!!.setBackClick(true)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        binding!!.chartBar.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartBar.setNoDataTextColor(Color.BLACK)
    }
    override fun onResume() {
        super.onResume()
        getAPIOrderAmount()
    }
    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }
    private fun getAPIOrderAmount() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-report/supplier-total-order-amount-by-time"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getReportOrderAmount(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ReportOrderAmountResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: ReportOrderAmountResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataOrderAmount = response.data
                        binding!!.thisWeekTotalOrderAmount.text =
                            currencyFormat(dataOrderAmount.this_week_total_order_amount.toString())
                        binding!!.lastWeekTotalOrderAmount.text =
                            currencyFormat(dataOrderAmount.last_week_total_order_amount.toString())
                        binding!!.thisMonthTotalOrderAmount.text =
                            currencyFormat(dataOrderAmount.this_month_total_order_amount.toString())
                        binding!!.lastMonthTotalOrderAmount.text =
                            currencyFormat(dataOrderAmount.last_month_total_order_amount.toString())
                        setDataBarOrderAmount()
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    private fun setDataBarOrderAmount() {
        val listCount = ArrayList<String>()
        listCount.add(0, "Tháng trước")
        listCount.add(1, "Tuần này")
        listCount.add(2, "Tuần trước ")
        listCount.add(3, "Tháng này")
        val xAxis: XAxis = binding!!.chartBar.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)
        xAxis.labelRotationAngle = -85f
        xAxis.textSize = 10f
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setValueFormatter { value, _ -> listCount[value.toInt() % listCount.size] }
        binding!!.chartBar.axisLeft
        val values = ArrayList<BarEntry>()
        values.add(
            0,
            BarEntry(
                (1).toFloat(),
                dataOrderAmount.this_week_total_order_amount / 1000000
            )
        )
        values.add(
            1,
            BarEntry((2).toFloat(), dataOrderAmount.last_week_total_order_amount / 1000000)
        )
        values.add(
            2,
            BarEntry((3).toFloat(), dataOrderAmount.this_month_total_order_amount / 1000000)
        )
        values.add(
            3,
            BarEntry((4).toFloat(), dataOrderAmount.last_month_total_order_amount / 1000000)
        )
        binding!!.chartBar.legend.isEnabled = false
        binding!!.chartBar.description.isEnabled = false
        binding!!.chartBar.setTouchEnabled(true)
        binding!!.chartBar.setPinchZoom(false)
        // set listeners
        binding!!.chartBar.setOnChartValueSelectedListener(this)
        binding!!.chartBar.setDrawGridBackground(false)
        binding!!.chartBar.setScaleEnabled(false)
        binding!!.chartBar.isScaleXEnabled = true
        binding!!.chartBar.axisRight.isEnabled = false
        binding!!.chartBar.animateY(5000)
        binding!!.chartBar.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val set1: BarDataSet
        if (binding!!.chartBar.data != null &&
            binding!!.chartBar.data.dataSetCount > 0
        ) {
            if (binding!!.chartBar.data.getDataSetByIndex(1) != null) {
                set1 = binding!!.chartBar.data.getDataSetByIndex(1) as BarDataSet
                set1.values = values
                binding!!.chartBar.data.notifyDataChanged()
                binding!!.chartBar.notifyDataSetChanged()
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
            val custom = MyValueFormatter(0)
            set1.valueFormatter = custom
            data.setValueTextSize(10f)
            data.barWidth = 0.6f
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