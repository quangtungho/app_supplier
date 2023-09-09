package vn.techres.supplier.fragment.revenuemanagement.genusitems.receipts

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.revenueadapter.ReceiptsAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentReceiptsBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.datamodel.DataGenusItems
import vn.techres.supplier.model.datamodel.DataListReceipts
import vn.techres.supplier.model.datamodel.DataReceipts
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.CreateReceiptsParams
import vn.techres.supplier.model.response.GenusItemsResponse
import vn.techres.supplier.model.response.ReceiptsResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.CurrencyEditText
import vn.techres.supplier.view.TechResTextView
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class ReceiptsFragment :
        BaseBindingFragment<FragmentReceiptsBinding>(FragmentReceiptsBinding::inflate), OnClickTransID {
    val tagName: String = FragmentReceiptsBinding::class.java.name
    private var adapter: ReceiptsAdapter? = null
    var listData = DataListReceipts()
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    var position = 0
    var listDataGenusItems = ArrayList<DataGenusItems>()
    private var dateDay: String? = ""
    private var idBill: Int = 0
    var idExchange = 0
    var listSpinnerString: ArrayList<String> = ArrayList()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        binding!!.txtTotalStatus.text = getString(R.string.txtTotalStatus_receipts)
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        mainActivity!!.setHeader(getString(R.string.receipts_items))
        mainActivity!!.setBackClick(true)
        dataFrom()
        dataDay()
        dataTo()
        getAPIListReceipts(
                binding!!.fromData.text.toString(),
                binding!!.toData.text.toString()
        )
        binding!!.fromData.setOnClickListener {
            dialogFromDatePicker()
        }
        binding!!.toData.setOnClickListener {
            dialogToDatePicker()
        }
        binding!!.btnCreate.setOnClickListener {
            openDialogCreate()
        }
        searchReceipts()
        binding!!.swipeRefreshLayout.setOnRefreshListener {
            onSwipeRefreshLayout()
        }
    }

    override fun onResume() {
        super.onResume()
        getAPIListReceipts(
                binding!!.fromData.text.toString(),
                binding!!.toData.text.toString()
        )

        getAPIGenusItems()
    }

    private fun dataDay() {
        val sdfTo = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        dateDay = sdfTo.format(Date())
    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = ReceiptsAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)
        adapter!!.setClickView(this)
    }

    private fun onSwipeRefreshLayout() {
        getAPIListReceipts(
                binding!!.fromData.text.toString(),
                binding!!.toData.text.toString()
        )
        binding!!.swipeRefreshLayout.isRefreshing = false
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

    private fun searchReceipts() {
        binding!!.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList = DataListReceipts()
                for (key in listData.list) {
                    if (listData.list.size > 0) {
                        val name: String = key.code
                        if (name.lowercase(Locale.getDefault())
                                        .contains(newText!!) || name.contains(newText)
                        )
                            newList.list.add(key)
                    }
                }
                adapter!!.setDataListReceipts(newList)
                binding!!.recyclerView.setHasFixedSize(true)
                binding!!.recyclerView.adapter = adapter

                return false
            }
        })
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
            getAPIListReceipts(
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
            getAPIListReceipts(
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

    private fun getAPIListReceipts(fromDate: String, toDate: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-addition-fees?from_date=$fromDate&to_date=$toDate&status=-1&type=${TechresEnum.STATUS_0}&limit=${TechresEnum.LIMIT_100}&page=${TechresEnum.STATUS_1}"
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListReceipts(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ReceiptsResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onNext(response: ReceiptsResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listData.list.clear()
                            listData = response.data
                            listData.list = listData.list.stream()
                                    .filter { x -> x.status == 0 || x.status == 1 }
                                    .collect(Collectors.toList()) as ArrayList<DataReceipts>
                            if (listData.list.size == 0) {
                                binding!!.linearDataNull.visibility = View.VISIBLE
                                binding!!.recyclerView.visibility = View.GONE
                            } else {
                                binding!!.linearDataNull.visibility = View.GONE
                                binding!!.recyclerView.visibility = View.VISIBLE
                            }
                            binding!!.txtTotal.text = listData.list.size.toString()

                            adapter!!.setDataListReceipts(listData)
                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                        }
                    }

                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                })
    }

    private fun createReceipts(amount: String, note: String, id: Int, dialog: Dialog) {
        val params = CreateReceiptsParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-addition-fees/create"
        params.params.supplier_addition_fee_reason_id = id
        params.params.fee_month = dateDay
        params.params.amount = AppUtils.trimCommaOfString(amount).toFloat()
        params.params.note = note
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .createReceipts(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ReceiptsResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: ReceiptsResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listData.list.clear()
                            getAPIListReceipts(
                                    binding!!.fromData.text.toString(),
                                    binding!!.toData.text.toString()
                            )
                            dialog.dismiss()
                            val snack = Snackbar.make(
                                    requireView(),
                                    getString(R.string.toast_create_success),
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()

                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {}

                })
    }

    private fun getAPIGenusItems() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-addition-fee-reasons"
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListGenusItems(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<GenusItemsResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onNext(response: GenusItemsResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDataGenusItems.clear()
                            listDataGenusItems = response.data
                            listDataGenusItems = listDataGenusItems.stream()
                                    .filter { x -> x.is_hidden == 0 && x.supplier_addition_fee_type == 0 && x.supplier_addition_fee_reason_category_id == 0 }
                                    .collect(Collectors.toList()) as ArrayList<DataGenusItems>
                            for (item in listDataGenusItems.indices) {
                                listSpinnerString.add(listDataGenusItems[item].name)
                            }
                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                        }
                    }
                })
    }

    //Dialog create
    private fun openDialogCreate() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_receipts)
        val window = dialog.window
        window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        idExchange = 0
        val spinnerData: Spinner = dialog.findViewById(R.id.spinnerData)
        val editAmount: CurrencyEditText =
                dialog.findViewById(R.id.editAmount)
        val editNote: EditText = dialog.findViewById(R.id.editNote)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val txtTitle: TechResTextView = dialog.findViewById(R.id.txtTitle)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val txtReceiptsExchange: TechResTextView = dialog.findViewById(R.id.txtReceiptsExchange)
        txtTitle.text = "Tạo phiếu thu"

        for (item in listDataGenusItems.indices) {
            spinnerData.adapter = ArrayAdapter(
                    mainActivity!!,
                    android.R.layout.simple_list_item_1,
                    listSpinnerString
            )
        }
        spinnerData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listDataGenusItems.indices) {
                    if (listSpinnerString[p2] == listDataGenusItems[i].name) {
                        idExchange = listDataGenusItems[i].id
                        txtReceiptsExchange.text = listDataGenusItems[i].name
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btnYes.setOnClickListener {
            val amount = editAmount.text.toString()
            val note = editNote.text.toString()
            when {
                txtReceiptsExchange.text == getString(R.string.txt_please_choose) -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_receipts_exchange_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                amount.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_amount_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                note.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_note_exchange_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }

                else -> {
                    createReceipts(
                           amount,
                            note, idExchange, dialog
                    )
                }
            }
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }

    override fun onClick(position: Int, id: Int) {
        this.position = position
        this.idBill = id
        val detailAdditionFeesFragment = DetailAdditionFeesFragment()
        cacheManager.put(TechresEnum.ID_BILL.toString(), id.toString())
        cacheManager.put(
                TechresEnum.STATUS_RECEIPTS.toString(),
                listData.list[position].status.toString()
        )
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.nav_host, DetailAdditionFeesFragment())
                            ?.addToBackStack(detailAdditionFeesFragment.tagName)
                            ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
        )

    }
}