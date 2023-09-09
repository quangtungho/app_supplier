package vn.techres.supplier.fragment.paymentrequest

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.paymentrequestadapter.DetailPaymentRequestAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentDetailPaymentRequestBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataListOrder
import vn.techres.supplier.model.datamodel.PaymentRequest
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.ChangePaymentRequestParams
import vn.techres.supplier.model.response.DetailBillPaymentRequestResponse
import vn.techres.supplier.model.response.DetailPaymentRequestResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat
import java.util.Collections.replaceAll


class DetailPaymentRequestFragment :
        BaseBindingFragment<FragmentDetailPaymentRequestBinding>(FragmentDetailPaymentRequestBinding::inflate) {
    val tagName: String = DetailPaymentRequestFragment::class.java.name
    private var listData = PaymentRequest()
    private var listDataBill = ArrayList<DataListOrder>()
    private var adapter: DetailPaymentRequestAdapter? = null
    private var idPayment: Int = 0
    private var idStatus: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        idStatus = cacheManager.get(TechresEnum.STATUS.toString())!!.toInt()
        idPayment = cacheManager.get(TechresEnum.GET_ID.toString())!!.toInt()
        when (idStatus) {
            0 -> {
                binding!!.status.text = "Chờ gửi phiếu yêu cầu"
                binding!!.mRootButton.visibility = View.VISIBLE
            }
            1 -> {
                binding!!.status.text = "Đã gửi phiếu yêu cầu"
                binding!!.mRootButton.visibility = View.GONE

            }
            2 -> {
                binding!!.status.text = "Đã hoàn tất phiếu yêu cầu "
                binding!!.mRootButton.visibility = View.GONE
            }
        }
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        setAdapter()
        getAPIDetailPaymentRequest()
        onClickButton()
    }

    private fun currencyFormatVND(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    private fun onClickButton() {
        binding!!.btnDelete.setOnClickListener {
            binding!!.btnDelete.isEnabled = false
            getAPIDeletePaymentRequest()
        }
        binding!!.btnCofirm.setOnClickListener {
            binding!!.btnCofirm.isEnabled = false
            getAPIChangePaymentRequest()
        }
        binding!!.btnUpdate.setOnClickListener {
            val updatePaymentRequestFragment = UpdatePaymentRequestFragment()
            val json = Gson().toJson(listData)
            cacheManager.put(TechresEnum.DATA_PAYMENT.toString(), json.toString())
            cacheManager.put(
                    TechresEnum.ID_PAYMENT.toString(),
                    listData.id.toString()
            )
            cacheManager.put(
                    TechresEnum.RESTAUNRANT_ID.toString(),
                    listData.restaurant_id.toString()
            )
            cacheManager.put(
                    TechresEnum.RESTAUNRANT_BRANCH_ID.toString(),
                    listData.restaurant_brand_id.toString()
            )
            cacheManager.put(TechresEnum.BRANCH_ID.toString(), listData.branch_id.toString())
            Handler(Looper.getMainLooper()).postDelayed(
                    {
                        activity?.supportFragmentManager
                                ?.beginTransaction()
                                ?.replace(R.id.nav_host, UpdatePaymentRequestFragment())
                                ?.addToBackStack(updatePaymentRequestFragment.tagName)
                                ?.commit()
                    }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    private fun setAdapter() {
        adapter = mainActivity?.let { DetailPaymentRequestAdapter() }
        binding!!.rcvListDebt.adapter = adapter
        binding!!.rcvListDebt.layoutManager = LinearLayoutManager(context)
        binding!!.rcvListDebt.setHasFixedSize(true)
    }

    /**
     * Chi tiet phieu
     */
    private fun getAPIDetailPaymentRequest() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-restaurant-debt-payment-requests/$idPayment"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getDetailPaymentRequest(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DetailPaymentRequestResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mainActivity!!.setBackClick(true)
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: DetailPaymentRequestResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listData = response.data
                            binding!!.nameRestaurant.text = listData.restaurant_name
                            binding!!.nameBranch.text = listData.branch_name
                            binding!!.dateCreateFrom.text = listData.from_date
                            binding!!.dateCreateTo.text = listData.to_date
                            if (listData.total_amount != null) {
                                binding!!.totail.text = currencyFormatVND(listData.total_amount.toString())
                            }

                            getAPIDetailBillPaymentRequest()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    /**
     * List chi tiết phiếu
     */
    private fun getAPIDetailBillPaymentRequest() {
        var supplierOrderIds = ""
        if (listData.supplier_order_ids.size == 0) {
            supplierOrderIds = ""
        } else {
            for (i in listData.supplier_order_ids.indices) {

                supplierOrderIds = listData.supplier_order_ids.joinToString(separator = ",")

            }
        }
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = String.format(
                "/api/supplier-orders/by-ids?supplier_order_ids=$supplierOrderIds"
        )

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getDetailBillPaymentRequest(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DetailBillPaymentRequestResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mainActivity!!.setBackClick(true)
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: DetailBillPaymentRequestResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDataBill = response.data
                            adapter!!.setDataList(listDataBill)
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }


    /**
     * Xoa phieu yeu cau
     */
    private fun getAPIDeletePaymentRequest() {
        val params = BaseParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-restaurant-debt-payment-requests/$idPayment/is-delete"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getDetailPaymentRequest(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DetailPaymentRequestResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: DetailPaymentRequestResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            binding!!.btnDelete.isEnabled = true
                            Toast.makeText(requireActivity(), "Đã xóa phiếu", Toast.LENGTH_SHORT).show()
                            mainActivity!!.supportFragmentManager.popBackStack()
                        } else {
                            binding!!.btnDelete.isEnabled = true
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    /**
     * Gui phieu yeu cau
     */
    private fun getAPIChangePaymentRequest() {
        val params = ChangePaymentRequestParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.params.status = TechresEnum.STATUS_1.toString().toInt()
        params.request_url =
                "/api/supplier-restaurant-debt-payment-requests/$idPayment/change-status"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getChangePaymentRequest(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DetailPaymentRequestResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: DetailPaymentRequestResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            binding!!.btnCofirm.isEnabled = true
                            Toast.makeText(
                                    requireActivity(),
                                    "Thay đổi trạng thái thành công",
                                    Toast.LENGTH_SHORT
                            ).show()
                            mainActivity!!.supportFragmentManager.popBackStack()
                        } else {
                            binding!!.btnCofirm.isEnabled = true
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }

}