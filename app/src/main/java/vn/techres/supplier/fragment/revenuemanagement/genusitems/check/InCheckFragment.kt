package vn.techres.supplier.fragment.revenuemanagement.genusitems.check

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import vn.techres.supplier.adapter.revenueadapter.CheckAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentReceiptsBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.WareHouse
import vn.techres.supplier.model.datamodel.DataGenusItems
import vn.techres.supplier.model.datamodel.DataListReceipts
import vn.techres.supplier.model.datamodel.DataReceipts
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.CreateReceiptsParams
import vn.techres.supplier.model.response.GenusItemsResponse
import vn.techres.supplier.model.response.ReceiptsResponse
import vn.techres.supplier.model.response.WareHouseResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.CurrencyEditText
import vn.techres.supplier.view.TechResTextView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class InCheckFragment :
        BaseBindingFragment<FragmentReceiptsBinding>(FragmentReceiptsBinding::inflate), OnClickTransID {
    val tagName: String = FragmentReceiptsBinding::class.java.name
    private var adapter: CheckAdapter? = null
    var listData = DataListReceipts()
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    private var dateDay: String? = ""
    private var idBill: Int = 0
    private var isCheck: Int = 0
    var idWareHouseExchange = ArrayList<Int>()
    var position = 0
    var idExchange = 0
    var listSpinnerString1: ArrayList<String> = ArrayList()
    var listSpinnerString2: ArrayList<String> = ArrayList()
    var listSpinnerStringWareHouse: ArrayList<String> = ArrayList()
    var listDataGenusItems = ArrayList<DataGenusItems>()
    var listDataGenusItems1 = ArrayList<DataGenusItems>()
    private var listDataWareHouse = ArrayList<WareHouse>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {

        setAdapter()
        binding!!.txtTotalStatus.text = getString(R.string.txtTotalStatus_check)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        binding!!.txtReceipst.text = getString(R.string.check_bill)
        mainActivity!!.setHeader(getString(R.string.check_items))
        mainActivity!!.setBackClick(true)
        dataFrom()
        dataTo()
        dataDay()
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
        binding!!.swipeRefreshLayout.setOnRefreshListener {
            onSwipeRefreshLayout()
        }
        searchCheck()
        binding!!.btnCreate.setOnClickListener {
            openDialogCreate()
        }
    }

    override fun onResume() {
        super.onResume()
        listData.list.clear()
        getAPIListReceipts(
                binding!!.fromData.text.toString(),
                binding!!.toData.text.toString()
        )
        listDataGenusItems.clear()
        getAPIGenusItems()
        listDataGenusItems1.clear()
        getAPIGenusItems1()
        listDataWareHouse.clear()
        getAPIWareHouse(dateTo!!)
    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = CheckAdapter(mainActivity!!.baseContext)
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

    private fun dataDay() {
        val sdfTo = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        dateDay = sdfTo.format(Date())
    }

    @SuppressLint("SetTextI18n")
    fun dataTo() {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        binding!!.toData.text = dateTo
    }

    private fun searchCheck() {
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
                "/api/supplier-addition-fees?from_date=$fromDate&to_date=$toDate&status=-1&type=${TechresEnum.STATUS_1}&limit=${TechresEnum.LIMIT_100}&page=${TechresEnum.STATUS_1}"
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

                            listData = response.data

                            listData.list = listData.list.stream()
                                    .filter { x -> x.status == 2 || x.status == 3 }
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
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
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

                            listDataGenusItems = response.data
                            // listDataGenusItems = listDataGenusItems.stream().filter { x -> x.is_hidden == 0 && x.supplier_addition_fee_type == 1 && x.supplier_addition_fee_reason_category_id == 2 }.collect(Collectors.toList()) as ArrayList<DataGenusItems>
                            listDataGenusItems = listDataGenusItems.stream()
                                    .filter { x -> x.is_hidden == 0 && x.supplier_addition_fee_type == 1 && x.supplier_addition_fee_reason_category_id == 2 }
                                    .collect(Collectors.toList()) as ArrayList<DataGenusItems>
                            for (item in listDataGenusItems.indices) {
                                listSpinnerString1.add(listDataGenusItems[item].name)
                            }

                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                        }
                    }
                })
    }

    private fun getAPIGenusItems1() {
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

                            listDataGenusItems1 = response.data
                            // listDataGenusItems = listDataGenusItems.stream().filter { x -> x.is_hidden == 0 && x.supplier_addition_fee_type == 1 && x.supplier_addition_fee_reason_category_id == 2 }.collect(Collectors.toList()) as ArrayList<DataGenusItems>
                            listDataGenusItems1 = listDataGenusItems1.stream()
                                    .filter { x -> x.is_hidden == 0 && x.supplier_addition_fee_type == 1 && x.supplier_addition_fee_reason_category_id == 0 }
                                    .collect(Collectors.toList()) as ArrayList<DataGenusItems>
                            for (item in listDataGenusItems1.indices) {
                                listSpinnerString2.add(listDataGenusItems1[item].name)
                            }

                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                        }
                    }
                })
    }

    private fun getAPIWareHouse(toDate: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-warehouse-sessions?type=${TechresEnum.STATUS_0}&from_date=01/01/1999&to_date=$toDate&limit=1000&page=0&status=${TechresEnum.PAYMENT_STATUS}&payment_status=0"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListWareHouse(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<WareHouseResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onNext(response: WareHouseResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {

                            listDataWareHouse = response.data.list

                            for (item in listDataWareHouse.indices) {
                                listSpinnerStringWareHouse.add(listDataWareHouse[item].code!!)
                            }
                        } else
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                    }
                })
    }


    private fun createCheck(
            amountPrice: String,
            note: String,
            id: Int,
            wareHouse: ArrayList<Int>,
            dialog: Dialog
    ) {
        val params = CreateReceiptsParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-addition-fees/create"
        params.params.supplier_addition_fee_reason_id = id
        params.params.fee_month = dateDay
        params.params.amount = AppUtils.trimCommaOfString(amountPrice).toFloat()
        params.params.note = note
        params.params.warehouse_session_ids = wareHouse
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

    //Dialog create
    @SuppressLint("SetTextI18n")
    private fun openDialogCreate() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_check)
        val window = dialog.window
        window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        idExchange = 0
        idWareHouseExchange = ArrayList()
        val editAmount: CurrencyEditText =
                dialog.findViewById(R.id.editAmount)
        val editNote: EditText = dialog.findViewById(R.id.editNote)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val txtTitle: TechResTextView = dialog.findViewById(R.id.txtTitle)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val txtWareHouseExchange: TechResTextView = dialog.findViewById(R.id.txtWareHouseExchange)
        val txtWareHouse: TechResTextView = dialog.findViewById(R.id.txtWareHouse)
        val txtAmount: TechResTextView = dialog.findViewById(R.id.txtAmount)
        val spinnerDataWareHouse: Spinner = dialog.findViewById(R.id.spinnerDataWareHouse)
        val rbGroup: RadioGroup = dialog.findViewById(R.id.rbGroup)
        val linearSpinnerWareHouse: LinearLayout = dialog.findViewById(R.id.linearSpinnerWareHouse)
        val linearSpinner1: LinearLayout = dialog.findViewById(R.id.linearSpinner1)
        val linearSpinner2: LinearLayout = dialog.findViewById(R.id.linearSpinner2)
        val txtReceiptsExchange1: TechResTextView = dialog.findViewById(R.id.txtReceiptsExchange1)
        val txtReceiptsExchange2: TechResTextView = dialog.findViewById(R.id.txtReceiptsExchange2)
        val spinnerData1: Spinner = dialog.findViewById(R.id.spinnerData1)
        val spinnerData2: Spinner = dialog.findViewById(R.id.spinnerData2)
        txtTitle.text = "Tạo phiếu chi"
        fun currencyFormat(amount: String): String? {
            val formatter = DecimalFormat("###,###,###")
            return formatter.format(amount.toDouble())
        }

//Chi nhap kho

        for (item in listDataGenusItems.indices) {
            spinnerData1.adapter = ArrayAdapter(
                    mainActivity!!,
                    android.R.layout.simple_list_item_1,
                    listSpinnerString1
            )
        }
        spinnerData1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listDataGenusItems.indices) {

                    if (listSpinnerString1[p2] == listDataGenusItems[i].name) {
                        idExchange = listDataGenusItems[i].id
                        txtReceiptsExchange1.text = listDataGenusItems[i].name
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
//Chi phis khacs
        for (item in listDataGenusItems1.indices) {
            spinnerData2.adapter = ArrayAdapter(
                    mainActivity!!,
                    android.R.layout.simple_list_item_1,
                    listSpinnerString2
            )
        }
        spinnerData2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listDataGenusItems1.indices) {

                    if (listSpinnerString2[p2] == listDataGenusItems1[i].name) {
                        idExchange = listDataGenusItems1[i].id
                        txtReceiptsExchange2.text = listDataGenusItems1[i].name
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
//phieu nhap kho

        for (item in listDataWareHouse.indices) {
            spinnerDataWareHouse.adapter = ArrayAdapter(
                    mainActivity!!,
                    android.R.layout.simple_list_item_1,
                    listSpinnerStringWareHouse
            )
        }

        spinnerDataWareHouse.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        for (i in listDataWareHouse.indices) {
                            if (listSpinnerStringWareHouse[p2] == listDataWareHouse[i].code) {
                                val arrayInt = ArrayList<Int>()
                                arrayInt.add(0, listDataWareHouse[i].id!!)
                                idWareHouseExchange = arrayInt
                                txtWareHouseExchange.text = listDataWareHouse[i].code
                                txtAmount.text = currencyFormat(listDataWareHouse[i].total_amount.toInt().toString())
                                break
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }

        rbGroup.setOnCheckedChangeListener { radioGroup, i ->
            val rbWareHouse: RadioButton = dialog.findViewById(R.id.rbWareHouse)
            val rbOtherCosts: RadioButton = dialog.findViewById(R.id.rbOtherCosts)
            if (rbWareHouse.isChecked) {
                isCheck = 0
                txtWareHouse.visibility = View.VISIBLE
                linearSpinnerWareHouse.visibility = View.VISIBLE
                linearSpinner2.visibility = View.GONE
                linearSpinner1.visibility = View.VISIBLE
                editAmount.visibility = View.GONE
                txtAmount.visibility = View.VISIBLE

            } else if (rbOtherCosts.isChecked) {
                isCheck = 1
                txtWareHouse.visibility = View.GONE
                linearSpinnerWareHouse.visibility = View.GONE
                linearSpinner2.visibility = View.VISIBLE
                linearSpinner1.visibility = View.GONE
                editAmount.visibility = View.VISIBLE
                txtAmount.visibility = View.GONE
            }

        }


        btnYes.setOnClickListener {


            val note = editNote.text.toString()
            if (isCheck == 0) {
                val amountWareHouse = txtAmount.text.toString()
                when {
                    note.isEmpty() -> {
                        Toast.makeText(
                                context,
                                "Ghi chú không được để trống",
                                Toast.LENGTH_SHORT
                        )
                                .show()
                    }
                    txtReceiptsExchange1.text == getString(R.string.txt_please_choose) -> {
                        Toast.makeText(
                                context,
                                "Hãy chọn lý do chi",
                                Toast.LENGTH_SHORT
                        )
                                .show()
                    }
                    txtWareHouseExchange.text == getString(R.string.txt_please_choose) -> {
                        Toast.makeText(
                                context,
                                "Hãy chọn phiếu nhập kho",
                                Toast.LENGTH_SHORT
                        )
                                .show()
                    }
                    else -> {
                        createCheck(
                                amountWareHouse,
                                note, idExchange, idWareHouseExchange, dialog
                        )
                    }
                }
            } else if (isCheck == 1) {
                val amount = editAmount.text.toString()
                when {
                    amount.isEmpty() -> {
                        Toast.makeText(
                                context,
                                "Tổng tiền không được để trống",
                                Toast.LENGTH_SHORT
                        )
                                .show()
                    }
                    note.isEmpty() -> {
                        Toast.makeText(
                                context,
                                "Ghi chú không được để trống",
                                Toast.LENGTH_SHORT
                        )
                                .show()
                    }
                    txtReceiptsExchange2.text == getString(R.string.txt_please_choose) -> {
                        Toast.makeText(
                                context,
                                "Hãy chọn lý do chi",
                                Toast.LENGTH_SHORT
                        )
                                .show()
                    }
                    else -> {
                        createCheck(
                                amount,
                                note, idExchange, ArrayList(0), dialog
                        )

                    }

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
        val detailAdditionFeesCheckFragment = DetailAdditionFeesCheckFragment()
        cacheManager.put(TechresEnum.ID_BILL.toString(), id.toString())
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.nav_host, DetailAdditionFeesCheckFragment())
                            ?.addToBackStack(detailAdditionFeesCheckFragment.tagName)
                            ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}