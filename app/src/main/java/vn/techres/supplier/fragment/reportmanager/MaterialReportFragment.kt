package vn.techres.supplier.fragment.reportmanager

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import vn.techres.supplier.adapter.materialreportdapter.MaterialReportAdapter
import vn.techres.supplier.adapter.orderreportadapter.BaseViewHolder
import vn.techres.supplier.adapter.orderreportadapter.BasesAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentMaterialReportBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataCategory
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.CategoryActiveResponse
import vn.techres.supplier.model.response.MaterialsReportResponse
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
class MaterialReportFragment :
    BaseBindingFragment<FragmentMaterialReportBinding>(FragmentMaterialReportBinding::inflate),
    OnChartValueSelectedListener {
    val tagName: String = MaterialReportFragment::class.java.name
    private val mListCalender: ArrayList<String> = ArrayList()
    private val mIConCalender: ArrayList<Int> = ArrayList()
    var dialogCalendar: IndicatorDialog? = null
    private var adapter: MaterialReportAdapter? = null
    private var materialReportSort = 1
    private var dateTo: String? = ""
    private var listCategoryExchange = ArrayList<DataCategory>()
    var listSpinnerString: ArrayList<String> = ArrayList()
    private var listMaterials = MaterialsReportResponse()
    private var idCategoryExchange = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        mainActivity!!.setHeader(getString(R.string.txt_materialreport))
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
        binding!!.chartPieReportWarehouse.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartPieReportWarehouse.setNoDataTextColor(Color.BLACK)
        binding!!.chartPieReportExport.setNoDataText(getString(R.string.txt_no_data_chart))
        binding!!.chartPieReportExport.setNoDataTextColor(Color.BLACK)
    }

    override fun onResume() {
        super.onResume()
        getAPICategory()
    }

    private fun dataSpinner() {
        listSpinnerString.add(0, requireContext().getString(R.string.txt_all_category))
        for (item in listCategoryExchange.indices) {
            binding!!.spinnerData.adapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                listSpinnerString
            )
        }
        binding!!.spinnerData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0) {
                    idCategoryExchange = -1
                    getAPIMaterialsReport(-1)
                } else {
                    for (i in listCategoryExchange.indices) {
                        if (listSpinnerString[p2] == listCategoryExchange[i].name) {
                            idCategoryExchange = listCategoryExchange[i].id
                            getAPIMaterialsReport(idCategoryExchange)
                            break
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun getAPICategory() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/material-categories?status=1"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListCategoryActive(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CategoryActiveResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: CategoryActiveResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listCategoryExchange = response.data
                        listSpinnerString.clear()
                        for (item in listCategoryExchange.indices) {
                            listSpinnerString.add(listCategoryExchange[item].name)
                            idCategoryExchange = listCategoryExchange[item].id
                        }
                        dataSpinner()
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun setAdapter() {
        adapter = mainActivity?.let { MaterialReportAdapter() }
        binding!!.rvMaterialReport.adapter = adapter
        binding!!.rvMaterialReport.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.rvMaterialReport.setHasFixedSize(true)
    }

    @SuppressLint("SetTextI18n")
    fun getDateDefault(): String {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        return dateTo.toString()
    }

    private fun showDialogCalendar(v: View, vFl: Float, gravityCenter: Int) {
        mListCalender.clear()
        mIConCalender.clear()
        mListCalender.add(getString(R.string.today))
        mIConCalender.add(R.drawable.ic_filter_today)
        mListCalender.add(getString(R.string.ByWeak))
        mIConCalender.add(R.drawable.ic_filter_today)
        mListCalender.add(getString(R.string.ByThisMonth))
        mIConCalender.add(R.drawable.ic_filter_today)
        mListCalender.add(getString(R.string.txt_revenue_three_months))
        mIConCalender.add(R.drawable.ic_filter_week)
        mListCalender.add(getString(R.string.ByYear))
        mIConCalender.add(R.drawable.ic_filter_month)
        mListCalender.add(getString(R.string.By3Month))
        mIConCalender.add(R.drawable.ic_filter_three_month)
        mListCalender.add(getString(R.string.all_year))
        mIConCalender.add(R.drawable.ic_filter_year)
        val resources = resources
        val dm = resources.displayMetrics
        val height = dm.heightPixels
        dialogCalendar = IndicatorBuilder(requireActivity())
            .width(500)
            .height((height * 0.5).toInt())
            .animator(R.style.dialog_exit)
            .ArrowDirection(IndicatorBuilder.TOP)
            .bgColor(Color.WHITE)
            .gravity(gravityCenter)
            .ArrowRectage(vFl)
            .radius(18)
            .layoutManager(LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false))
            .adapter(object : BasesAdapter() {
                override fun onBindView(holder: BaseViewHolder?, position: Int) {
                    val tv: TextView? = holder!!.getView(R.id.item_add)
                    tv!!.text = mListCalender[position]
                    tv.setCompoundDrawablesWithIntrinsicBounds(mIConCalender[position], 0, 0, 0)
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
                    binding!!.icCalendar.setImageResource(mIConCalender[position])
                    when (position) {
                        0 -> {
                            materialReportSort = 1

                            getAPIMaterialsReport(idCategoryExchange)
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )
                        }
                        1 -> {
                            materialReportSort = 2

                            getAPIMaterialsReport(idCategoryExchange)
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                        2 -> {
                            materialReportSort = 3

                            getAPIMaterialsReport(idCategoryExchange)
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                        3 -> {
                            materialReportSort = 4

                            getAPIMaterialsReport(idCategoryExchange)
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                        4 -> {
                            materialReportSort = 5
                            getAPIMaterialsReport(idCategoryExchange)
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                        5 -> {
                            materialReportSort = 6
                            getAPIMaterialsReport(idCategoryExchange)
                            mainActivity!!.setLoading(true)
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    mainActivity!!.setLoading(false)
                                }, TechresEnum.TIME_500.toString().toLong()
                            )

                        }
                        6 -> {
                            materialReportSort = 7
                            idCategoryExchange = -1
                            getAPIMaterialsReport(idCategoryExchange)
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

    //API thong ke don  hang ngay
    private fun getAPIMaterialsReport(idCategoryExchange: Int) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-report/material-report?material_categorie_id=$idCategoryExchange&group_by_type=$materialReportSort&from_date=" + getDateDefault()

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getMaterialsReport(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MaterialsReportResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mainActivity!!.setBackClick(true)
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: MaterialsReportResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listMaterials = response
                        adapter!!.setDataSource(listMaterials)
                        adapter!!.notifyDataSetChanged()
                        chartPigMaterialImport()
                        chartPigMaterialExport()
                        setDataPieImport()
                        setDataPieExport()
                        mainActivity!!.setBackClick(true)
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }

                }
            })
    }

    @SuppressLint("UseRequireInsteadOfGet")
    fun chartPigMaterialImport() {
        binding!!.chartPieReportWarehouse.holeRadius = 70f
        binding!!.chartPieReportWarehouse.transparentCircleRadius = 61f
        binding!!.chartPieReportWarehouse.setDrawCenterText(true)
        binding!!.chartPieReportWarehouse.isDrawHoleEnabled = true
        binding!!.chartPieReportWarehouse.rotationAngle = 90f
        binding!!.chartPieReportWarehouse.isRotationEnabled = true
        binding!!.chartPieReportWarehouse.setUsePercentValues(true)
        binding!!.chartPieReportWarehouse.description.isEnabled = false
        binding!!.chartPieReportWarehouse.setDrawEntryLabels(false)
        var nSum = BigDecimal.ZERO

        nSum += listMaterials.data.total_import.toDouble().toBigDecimal()

        binding!!.chartPieReportWarehouse.centerText = getString(R.string.title_warehouse) + "\n" + currencyFormat(nSum.toString())
        binding!!.chartPieReportWarehouse.setOnChartValueSelectedListener(this)
        binding!!.chartPieReportWarehouse.animateXY(1500, 1500)
        binding!!.chartPieReportWarehouse.setEntryLabelColor(Color.BLACK)
        binding!!.chartPieReportWarehouse.setEntryLabelTextSize(12f)
    }

    fun chartPigMaterialExport() {
        binding!!.chartPieReportExport.holeRadius = 70f
        binding!!.chartPieReportExport.transparentCircleRadius = 61f
        binding!!.chartPieReportExport.setDrawCenterText(true)
        binding!!.chartPieReportExport.isDrawHoleEnabled = true
        binding!!.chartPieReportExport.rotationAngle = 90f
        binding!!.chartPieReportExport.isRotationEnabled = true
        binding!!.chartPieReportExport.setUsePercentValues(true)
        binding!!.chartPieReportExport.description.isEnabled = false
        binding!!.chartPieReportExport.setDrawEntryLabels(false)
        var nSum = BigDecimal.ZERO

        nSum += listMaterials.data.total_export.toDouble().toBigDecimal()


        binding!!.chartPieReportExport.centerText =
            getString(R.string.title_wexport) + "\n" + currencyFormat(nSum.toString())
        binding!!.chartPieReportExport.setOnChartValueSelectedListener(this)
        binding!!.chartPieReportExport.animateXY(1500, 1500)
        binding!!.chartPieReportExport.setEntryLabelColor(Color.BLACK)
        binding!!.chartPieReportExport.setEntryLabelTextSize(12f)
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    private fun setDataPieImport() {
        val entries = ArrayList<PieEntry>()
        /**
         * Lấy doanh thu từng danh mục add vào mảng entries
         * entries là mảng để hiển thị % trên chart
         */
        entries.add(
            PieEntry(
                listMaterials.data.total_import.toFloat()
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

        binding!!.chartPieReportWarehouse.data = data

//         undo all highlights
        binding!!.chartPieReportWarehouse.highlightValues(null)

        binding!!.chartPieReportWarehouse.invalidate()
    }
    private fun setDataPieExport() {
        val entries = ArrayList<PieEntry>()
        /**
         * Lấy doanh thu từng danh mục add vào mảng entries
         * entries là mảng để hiển thị % trên chart
         */
        entries.add(
            PieEntry(
                listMaterials.data.total_export.toFloat()
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

        binding!!.chartPieReportExport.data = data

//         undo all highlights
        binding!!.chartPieReportExport.highlightValues(null)

        binding!!.chartPieReportExport.invalidate()
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {
    }
}