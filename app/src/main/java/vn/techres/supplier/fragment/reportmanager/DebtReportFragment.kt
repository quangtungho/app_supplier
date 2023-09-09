package vn.techres.supplier.fragment.reportmanager


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.github.mikephil.charting.components.XAxis
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
import vn.techres.supplier.adapter.debtadapter.DebtManagerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentDebtReportBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.MyValueFormatter
import vn.techres.supplier.model.datamodel.DataDebt
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.DebtResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class DebtReportFragment :
    BaseBindingFragment<FragmentDebtReportBinding>(FragmentDebtReportBinding::inflate),
    OnChartValueSelectedListener {
    val tagName: String = DebtReportFragment::class.java.name
    private var adapter: DebtManagerAdapter? = null
    private var dataDebt = DataDebt()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setHideHeader(false)
        mainActivity!!.setBackClick(true)
        mainActivity!!.setHeader(getString(R.string.btn_rpdebt))
        adapter = DebtManagerAdapter(childFragmentManager, mainActivity!!)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        getAPIDebt()
        binding!!.chartBar.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartBar.setNoDataTextColor(Color.BLACK)
    }
    private fun setDataBarChat() {
        val dataCount = ArrayList<String>()
        dataCount.add(0, "Phải thu")
        dataCount.add(1, "Phải trả")
        val xAxis: XAxis = binding!!.chartBar.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 2
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)
        xAxis.textSize = 10f
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setValueFormatter { value, _ -> dataCount[value.toInt() % dataCount.size] }
        val values = ArrayList<BarEntry>()
        values.add(
            0,
            BarEntry((1).toFloat(), dataDebt.total_receivable_debt_amount.toFloat() / 1000000)
        )
        values.add(
            1,
            BarEntry((2).toFloat(), dataDebt.total_to_pay_debt_amount.toFloat() / 1000000)
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
    private fun getAPIDebt() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-report/total-debt"
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getDebtManager(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DebtResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("SetTextI18n")
                override fun onNext(response: DebtResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataDebt = response.data
                        setDataBarChat()
                        binding!!.totalReceivableDebtAmount.text =
                            currencyFormatVND(dataDebt.total_receivable_debt_amount.toString())
                        binding!!.totalToPayDebtAmount.text =
                            currencyFormatVND(dataDebt.total_to_pay_debt_amount.toString())
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    private fun currencyFormatVND(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }
    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }
    override fun onNothingSelected() {
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}