package vn.techres.supplier.fragment.paymentrequest

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.paymentrequestadapter.ItemPaymentRequestAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentCreatePaymentRequestBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.PaymentClick
import vn.techres.supplier.model.datamodel.*
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.CreatePaymentRequestParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.BranchResponse
import vn.techres.supplier.model.response.CustomerResponse
import vn.techres.supplier.model.response.OrderResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreatePaymentRequestFragment :
    BaseBindingFragment<FragmentCreatePaymentRequestBinding>(FragmentCreatePaymentRequestBinding::inflate),
    PaymentClick {
    val tagName: String = CreatePaymentRequestFragment::class.java.name
    private var branchList = ArrayList<Branch>()
    private var restaurantList = ArrayList<Customer>()
    private var restaurantBrandId: Int = 0
    private var branchID: Int = 0
    private var restaurantID: Int = 0
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    private var listData = PaymentRequest()
    private var listDebt = DataOrder()
    private var addBill = ArrayList<DataListOrder>()
    private var paymentList = ArrayList<DataListOrder>()
    private var adapter: ItemPaymentRequestAdapter? = null
    private var paymentListInsert = ArrayList<Int>()
    private var paymentListDelete = ArrayList<Int>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()

    }

    private fun onBody() {
        setAdapter()
        dataFrom()
        dataTo()
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)

        binding!!.layoutDate.fromData.setOnClickListener {
            dialogFromDatePicker()
        }
        binding!!.layoutDate.toData.setOnClickListener {
            dialogToDatePicker()
        }
        getAPIRestaurant()
        onClick()
    }

    private fun onClick() {
        binding!!.mRootButton.setOnClickListener {
            binding!!.mRootButton.isEnabled = false
            Handler().postDelayed({ binding!!.mRootButton.isEnabled = true }, 5000)
            getAPICreatePayment()
        }
    }

    private fun setAdapter() {
        binding!!.rcvListPaymentRequest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding!!.rcvListPaymentRequest.setItemViewCacheSize(500)
        adapter = ItemPaymentRequestAdapter(mainActivity!!.baseContext)
        binding!!.rcvListPaymentRequest.adapter =
            ConcatAdapter(adapter)
        binding!!.rcvListPaymentRequest.setHasFixedSize(true)
        adapter!!.setPaymentClick(this)
    }

    @SuppressLint("SetTextI18n")
    fun dataFrom() {
        val sdfTo = SimpleDateFormat(getString(R.string.mmyyyy), Locale.getDefault())
        dateFrom = "01/" + sdfTo.format(Date())
        binding!!.layoutDate.fromData.text = dateFrom
    }

    @SuppressLint("SetTextI18n")
    fun dataTo() {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        binding!!.layoutDate.toData.text = dateTo
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
            binding!!.layoutDate.fromData.text = String.format(
                "%s/%s/%s",
                getDate(datePicker!!.day),
                getDate(datePicker.month),
                datePicker.year
            )

            getAPIListPayment(branchID)
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
            binding!!.layoutDate.toData.text = String.format(
                "%s/%s/%s",
                getDate(datePicker!!.day),
                getDate(datePicker.month),
                datePicker.year
            )
            getAPIListPayment(branchID)
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

    private fun getAPIRestaurant() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/restaurants?status=${TechresEnum.CUSTOMER_ACTIVE_MANAGER}&limit=${TechresEnum.LIMIT_100}&page=${TechresEnum.PAGE}"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListCustomer(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CustomerResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
                override fun onNext(response: CustomerResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        restaurantList = response.data!!.list
                        if (response.data!!.list.size > 0) {
                            getAPIBranch(restaurantList[0].id)
                            AppUtils.getRestaurantSpinner(
                                binding!!.spinnerListRestaurant,
                                response.data!!.list
                            )
                            binding!!.spinnerListRestaurant.onItemSelectedListener = object :
                                AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    restaurantID = restaurantList[position].id
                                    getAPIBranch(restaurantID)
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                        }
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    /**
     * Chi nhánh nhà hàng
     *
     */
    private fun getAPIBranch(idRestaurant: Int) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/branches?status=${TechresEnum.STATUS_1}&restaurant_id=$idRestaurant"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireContext()
        )
            .getListBranch(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BranchResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: BranchResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        branchList = response.data
                        if (response.data.size > 0) {
                            getAPIListPayment(branchList[0].id)
                            AppUtils.getBranchSpinner(
                                binding!!.spinnerListBranchOrder,
                                response.data
                            )
                            binding!!.spinnerListBranchOrder.onItemSelectedListener = object :
                                AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    branchID = branchList[position].id
                                    restaurantBrandId = branchList[position].restaurant_brand_id
                                    getAPIListPayment(branchID)
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                        }
                    }
                    else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    /**
     * goi API get chi tiet don hang cong no
     */
    private fun getAPIListPayment(idBranch: Int) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders/supplier?branch_id=$idBranch&statuses=${TechresEnum.STATUS_4},${TechresEnum.STATUS_6},${TechresEnum.STATUS_7}&from=$dateFrom&to=$dateTo&page=0&limit=${TechresEnum.LIMIT_100}&payment_status=0"
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
                        listDebt = response.data
                        adapter!!.setDataSource(listDebt.list)
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    private fun checkUpdatePayment(listData: ArrayList<DataListOrder>) {
        val listInsertTemp = listData.filter { it.is_join == 1 }
        val listDeleteTemp = listData.filter { it.is_join == 0 }
        paymentListInsert.clear()
        paymentListDelete.clear()
        for (i in listInsertTemp.indices) {
            paymentListInsert.add(listInsertTemp[i].id ?: 0)
        }
        for (i in listDeleteTemp.indices) {
            paymentListDelete.add(listDeleteTemp[i].id ?: 0)
        }
    }

    /**
     * Tạo phiếu
     */
    private fun getAPICreatePayment() {
        val params = CreatePaymentRequestParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.params.restaurant_id = restaurantID
        params.params.restaurant_brand_id = restaurantBrandId
        params.params.branch_id = branchID
        params.params.status = 0
        params.params.from_date = dateFrom
        params.params.to_date = dateTo
        params.params.to_date = dateTo
        params.params.supplier_order_ids = paymentListInsert
        params.request_url =
            "/api/supplier-restaurant-debt-payment-requests/create"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getCreatePayment(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: BaseResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        mainActivity!!.supportFragmentManager.popBackStack()
                        Toast.makeText(
                            requireActivity(),
                            "Tạo phiếu thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }

    override fun onItemTag(user: DataListOrder?, position: Int, isTag: Boolean) {
        if (isTag) {
            paymentList.add(
                    DataListOrder(
                            user!!.id!!
                    )
            )
            binding!!.mRootButton.visibility = View.VISIBLE
        } else {
            for (i in 0 until paymentList.size) {
                if (paymentList[i].id == listData.id) {
                    paymentList.removeAt(i)
                }
            }
            if (paymentList.size == 0) {
                binding!!.mRootButton.visibility = View.GONE
            }
        }
    }

    override fun onItemTag(id: Int, position: Int, isTag: Boolean) {
    }

    override fun onUpdate(data: ArrayList<DataListOrder>) {
        checkUpdatePayment(data)
    }


}