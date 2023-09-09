package vn.techres.supplier.fragment.ordermanager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.jiang.android.indicatordialog.IndicatorDialog
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.Size
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.ordermanageradapterr.OrderCanceledAdapter
import vn.techres.supplier.adapter.ordermanageradapterr.OrderCompletedAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOrderBinding
import vn.techres.supplier.databinding.ItemCalendarDayBinding
import vn.techres.supplier.fragment.ordermanager.orderbilldetail.OrderCanceledDetailFragment
import vn.techres.supplier.fragment.ordermanager.orderbilldetail.OrderCompletedDetailFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickBillOrder
import vn.techres.supplier.model.datamodel.DataOrder
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.OrderResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderCompletedFragment :
    BaseBindingFragment<FragmentOrderBinding>(FragmentOrderBinding::inflate),
    OnClickBillOrder {
    val tagName: String = OrderCompletedFragment::class.java.name
    private var adapterCompleted: OrderCompletedAdapter? = null
    private var adapterCanceled: OrderCanceledAdapter? = null
    var dialog: IndicatorDialog? = null
    private var dataOrderCompleted = DataOrder()
    private var dataOrderCanceled = DataOrder()
    private var orderCompletedDetailFragment = OrderCompletedDetailFragment()
    private var orderCanceledDetailFragment = OrderCanceledDetailFragment()
    var position = 0
    var code = ""
    private var dateTo: String? = ""
    private var dateToMonth: String? = ""
    private var idOrderBill: Int = 0

    @SuppressLint("NewApi")
    private var selectedDate = LocalDate.now()

    @SuppressLint("NewApi")
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

    @SuppressLint("NewApi")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")

    @SuppressLint("NewApi")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMM")

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        dataTo()
        dataToMonth()
        mainActivity!!.setHeader(getString(R.string.btn_order))
        binding!!.exSevenCalendar.visibility = View.VISIBLE
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
                binding!!.exSevenCalendar.visibility = View.GONE
                binding!!.txtMonth.text = getText(R.string.view_month)
                binding!!.btnDateMonth.visibility = View.GONE
                binding!!.btnDate.visibility = View.GONE
                binding!!.btnDateMonth.visibility = View.VISIBLE
                getAPIListOrderCanceled(
                    binding!!.btnDateMonth.text.toString(),
                    TechresEnum.GET_TIME_STORE_2.toString().toInt()
                )
                getAPIListOrderCompleted(
                    binding!!.btnDateMonth.text.toString(),
                    TechresEnum.GET_TIME_STORE_2.toString().toInt()
                )
            } else {
                binding!!.exSevenCalendar.visibility = View.VISIBLE
                binding!!.txtMonth.text = getText(R.string.view_day)
                binding!!.btnDateMonth.visibility = View.VISIBLE
                binding!!.btnDate.visibility = View.VISIBLE
                binding!!.btnDateMonth.visibility = View.GONE
                getAPIListOrderCanceled(
                    binding!!.btnDate.text.toString(), TechresEnum.GET_TIME_STORE.toString().toInt()
                )
                getAPIListOrderCompleted(
                    binding!!.btnDate.text.toString(), TechresEnum.GET_TIME_STORE.toString().toInt()
                )
            }
        }
        onFunction()
    }

    @SuppressLint("NewApi")
    private fun onFunction() {
        val dm = DisplayMetrics()
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        binding!!.exSevenCalendar.apply {
            val dayWidth = dm.widthPixels / 5
            val dayHeight = (dayWidth * 1.25).toInt()
            daySize = Size(dayWidth, dayHeight)
        }

        @SuppressLint("NewApi")
        class DayViewContainer(view: View) : ViewContainer(view) {
            val bind = ItemCalendarDayBinding.bind(view)
            lateinit var day: CalendarDay

            init {
                view.setOnClickListener {
                    val firstDay = binding!!.exSevenCalendar.findFirstVisibleDay()
                    val lastDay = binding!!.exSevenCalendar.findLastVisibleDay()
                    if (firstDay == day) {
                        binding!!.exSevenCalendar.smoothScrollToDate(day.date)
                    } else if (lastDay == day) {
                        binding!!.exSevenCalendar.smoothScrollToDate(day.date.minusDays(4))
                    }
                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        binding!!.exSevenCalendar.notifyDateChanged(day.date)
                        oldDate?.let { binding!!.exSevenCalendar.notifyDateChanged(it) }
                    }
                }
            }

            @SuppressLint("ResourceType")
            fun bind(day: CalendarDay) {
                this.day = day
                bind.exSevenDateText.text = dateFormatter.format(day.date)
                bind.exSevenDayText.text = dayFormatter.format(day.date)
                bind.exSevenMonthText.text = monthFormatter.format(day.date)
                bind.exSevenDateText.setTextColor(view.context.getColor(if (day.date == selectedDate) R.color.blue else R.color.black))
                if (day.date == selectedDate) {
                    bind.linearSelection.setBackgroundResource(R.color.bg_time)
                } else bind.linearSelection.setBackgroundResource(R.color.white)
                bind.exSevenSelectedView.isVisible = day.date == selectedDate
                val formatters = DateTimeFormatter.ofPattern("d/MM/uuuu")
                val text: String = selectedDate.format(formatters)
                binding!!.btnDate.text = text
                getAPIListOrderCanceled(
                    binding!!.btnDate.text.toString(),
                    TechresEnum.GET_TIME_STORE.toString().toInt()
                )
                getAPIListOrderCompleted(
                    binding!!.btnDate.text.toString(), TechresEnum.GET_TIME_STORE.toString().toInt()
                )
            }
        }
        binding!!.exSevenCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) = container.bind(day)
        }
        val currentMonth = YearMonth.now()
        binding!!.exSevenCalendar.setup(
            currentMonth,
            currentMonth.plusMonths(3),
            DayOfWeek.values().random()
        )
        binding!!.exSevenCalendar.scrollToDate(LocalDate.now())
    }

    override fun onResume() {
        super.onResume()
        if (binding!!.checkBox.isChecked) {
            getAPIListOrderCanceled(
                binding!!.btnDateMonth.text.toString(),
                TechresEnum.GET_TIME_STORE_2.toString().toInt()
            )
            getAPIListOrderCompleted(
                binding!!.btnDateMonth.text.toString(),
                TechresEnum.GET_TIME_STORE_2.toString().toInt()
            )
        } else {
            getAPIListOrderCanceled(
                binding!!.btnDate.text.toString(), TechresEnum.GET_TIME_STORE.toString().toInt()
            )
            getAPIListOrderCompleted(
                binding!!.btnDate.text.toString(), TechresEnum.GET_TIME_STORE.toString().toInt()
            )
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

    @SuppressLint("NewApi")
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

            getAPIListOrderCanceled(
                binding!!.btnDate.text.toString(), TechresEnum.GET_TIME_STORE.toString().toInt()
            )
            getAPIListOrderCompleted(
                binding!!.btnDate.text.toString(), TechresEnum.GET_TIME_STORE.toString().toInt()
            )
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
        val datePicker = bottomSheetDialog.findViewById<com.ycuwq.datepicker.date.DatePicker>(R.id.datePicker)
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
            getAPIListOrderCanceled(
                binding!!.btnDateMonth.text.toString(),
                TechresEnum.GET_TIME_STORE_2.toString().toInt()
            )
            getAPIListOrderCompleted(
                binding!!.btnDateMonth.text.toString(),
                TechresEnum.GET_TIME_STORE_2.toString().toInt()
            )

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

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapterCompleted = OrderCompletedAdapter(mainActivity!!.baseContext)
        adapterCanceled = OrderCanceledAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = ConcatAdapter(adapterCompleted, adapterCanceled)
        binding!!.recyclerView.setHasFixedSize(true)
        adapterCompleted?.setClickView(this)
        adapterCanceled?.setClickView(this)
    }

    //______________________________API________________________//
    //API lấy danh sach don hang cho xu ly(Hoan tat)
    @SuppressLint("StringFormatInvalid")
    private fun getAPIListOrderCompleted(toDate: String, getTimStore: Int) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders?time=$toDate&payment_status=${TechresEnum.PAYMENT_STATUS}&status=${TechresEnum.STATUS_4},${TechresEnum.STATUS_7}&page=${TechresEnum.PAGE}&limit=${TechresEnum.LIMIT_1000}&get_time_store=$getTimStore"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListOrderCompleted(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<OrderResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: OrderResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataOrderCompleted = response.data
                        if (dataOrderCanceled.list.size != 0 || dataOrderCompleted.list.size != 0) {
                            binding!!.linearDataNull.visibility = View.GONE
                        } else if (dataOrderCompleted.list.size == 0 && dataOrderCanceled.list.size == 0) {
                            binding!!.linearDataNull.visibility = View.VISIBLE
                        }
                        adapterCompleted!!.setDataListOrder(
                            dataOrderCompleted.list,
                            dataOrderCompleted.total_record ?: 0
                        )
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }

                }
            })
    }

    private fun getAPIListOrderCanceled(toDate: String, getTimStore: Int) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders?time=$toDate&payment_status=${TechresEnum.PAYMENT_STATUS}&status=${TechresEnum.STATUS_5}&page=${TechresEnum.PAGE}&limit=${TechresEnum.LIMIT_1000}&get_time_store=$getTimStore"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListOrderCanceled(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<OrderResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: OrderResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataOrderCanceled = response.data
                        if (dataOrderCanceled.list.size != 0 || dataOrderCompleted.list.size != 0) {
                            binding!!.linearDataNull.visibility = View.GONE
                        } else if (dataOrderCompleted.list.size == 0 && dataOrderCanceled.list.size == 0) {
                            binding!!.linearDataNull.visibility = View.VISIBLE
                        }
                        adapterCanceled!!.setDataListOrder(
                            dataOrderCanceled.list,
                            dataOrderCanceled.total_record ?: 0
                        )

                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    //____________________ONCLICK______________//
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

    override fun onClickViewWait(position: Int, id: Int, img: String) {}
    override fun onClickViewDelivery(position: Int, id: Int, img: String) {}
    override fun onClickViewReturn(position: Int, id: Int, img: String) {}

    //Item  huy don hang
    override fun onClickViewCanceled(position: Int, id: Int, img: String) {
        this.position = position
        this.idOrderBill = id
        cacheManager.put(TechresEnum.GET_BY_ID.toString(), id.toString())
        cacheManager.put(TechresEnum.GET_BY_AVATAR.toString(), img)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.nav_host, OrderCanceledDetailFragment())
                    ?.addToBackStack(orderCanceledDetailFragment.tagName)
                    ?.commit()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }

    //Item hoan tat
    override fun onClickViewCompleted(
        position: Int,
        id: Int,
        img: String
    ) {
        this.position = position
        this.idOrderBill = id
        cacheManager.put(TechresEnum.GET_BY_ID.toString(), id.toString())
        cacheManager.put(TechresEnum.GET_BY_AVATAR.toString(), img)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.nav_host, OrderCompletedDetailFragment())
                    ?.addToBackStack(orderCompletedDetailFragment.tagName)
                    ?.commit()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}