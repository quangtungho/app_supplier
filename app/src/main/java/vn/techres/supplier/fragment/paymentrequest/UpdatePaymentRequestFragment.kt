package vn.techres.supplier.fragment.paymentrequest

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import vn.techres.supplier.databinding.FragmentUpdatePaymentRequestBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.PaymentClick
import vn.techres.supplier.model.datamodel.DataListOrder
import vn.techres.supplier.model.datamodel.PaymentRequest
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.UpdatePaymentRequestParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.OrderResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService


class UpdatePaymentRequestFragment :
    BaseBindingFragment<FragmentUpdatePaymentRequestBinding>(FragmentUpdatePaymentRequestBinding::inflate),
    PaymentClick {
    val tagName: String = UpdatePaymentRequestFragment::class.java.name
    private var restaurantID: Int? = 0
    private var restaurantBrandId: Int? = 0
    private var branchID: Int? = 0
    private var idPayment: Int? = 0
    private var dateFrom: String? = ""
    private var dateTo: String? = ""
    private var paymentList = ArrayList<DataListOrder>()
    private var adapter: ItemPaymentRequestAdapter? = null
    private var listDebt = ArrayList<DataListOrder>()
    private var listData = PaymentRequest()
    var listPaymentRequest: ArrayList<Int> = ArrayList()

    private var paymentListInsert = ArrayList<Int>()
    private var paymentListDelete = ArrayList<Int>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
//        dataFrom()
//        dataTo()

        listData = Gson().fromJson(
            cacheManager.get(TechresEnum.DATA_PAYMENT.toString()),
            PaymentRequest::class.java
        )
        binding!!.layoutDate.fromData.text = listData.from_date
        binding!!.layoutDate.toData.text = listData.to_date
        for (i in 0 until listData.supplier_orders.size) {
            listPaymentRequest.add(listData.supplier_orders[i].supplier_order_id)
        }

        restaurantID = cacheManager.get(TechresEnum.RESTAUNRANT_ID.toString())!!.toInt()
        idPayment = cacheManager.get(TechresEnum.ID_PAYMENT.toString())!!.toInt()
        restaurantBrandId = cacheManager.get(TechresEnum.RESTAUNRANT_BRANCH_ID.toString())!!.toInt()
        branchID = cacheManager.get(TechresEnum.BRANCH_ID.toString())!!.toInt()
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        binding!!.layoutDate.fromData.setOnClickListener {
            dialogFromDatePicker()
        }
        binding!!.layoutDate.toData.setOnClickListener {
            dialogToDatePicker()
        }
        onClick()
        getAPIListPayment(
            binding!!.layoutDate.fromData.text.toString(),
            binding!!.layoutDate.toData.text.toString()
        )
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

    private fun onClick() {
        binding!!.mRootButton.setOnClickListener {
            binding!!.mRootButton.isEnabled = false
            Handler().postDelayed({ binding!!.mRootButton.isEnabled = true }, 5000)
            getAPIUpdatePayment()
        }
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
            getAPIListPayment(
                binding!!.layoutDate.fromData.text.toString(),
                binding!!.layoutDate.toData.text.toString()
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
            binding!!.layoutDate.toData.text = String.format(
                "%s/%s/%s",
                getDate(datePicker!!.day),
                getDate(datePicker.month),
                datePicker.year
            )
            getAPIListPayment(
                binding!!.layoutDate.fromData.text.toString(),
                binding!!.layoutDate.toData.text.toString()
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


    /**
     * Cập nhật phiếu
     */
    private fun getAPIUpdatePayment() {
        val params = UpdatePaymentRequestParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.params.restaurant_id = restaurantID
        params.params.restaurant_brand_id = restaurantBrandId
        params.params.branch_id = branchID
        params.params.status = 0
        params.params.from_date = listData.from_date
        params.params.to_date = listData.to_date
        params.params.insert_supplier_order_ids = paymentListInsert
        params.params.delete_supplier_order_ids = paymentListDelete
        params.request_url =
            "/api/supplier-restaurant-debt-payment-requests/$idPayment/update"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getUpdatePayment(params)
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
                        binding!!.mRootButton.isEnabled = true
                        Toast.makeText(
                            requireActivity(),
                            "Cập nhật phiếu thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else {
                        binding!!.mRootButton.isEnabled = true
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    /**
     * goi API get chi tiet don hang cong no
     */
    private fun getAPIListPayment(dateFrom: String, dateTo: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders/supplier?branch_id=$branchID&statuses=4,6,7&from=$dateFrom&to=$dateTo&page=0&limit=100&payment_status=0"
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
                        listDebt = response.data.list
                        for (i in 0 until listDebt.size) {
                            for (j in 0 until listPaymentRequest.size) {
                                if (listPaymentRequest[j] == listDebt[i].id) {
                                    listDebt[i].is_join = 0
                                }
                            }
                        }
                        adapter!!.setDataSource(listDebt)

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