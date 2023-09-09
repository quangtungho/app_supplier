package vn.techres.supplier.fragment.revenuemanagement.genusitems.receipts

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.revenueadapter.ReceptsItemAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentDetailAdditionFeesBinding
import vn.techres.supplier.fragment.ordermanager.orderbilldetail.OrderCompletedDetailFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.datamodel.DataDetailBill
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.StatusAdditionFeesParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.DetailBillResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class DetailAdditionFeesFragment :
    BaseBindingFragment<FragmentDetailAdditionFeesBinding>(FragmentDetailAdditionFeesBinding::inflate),
    OnClickTransID {
    val tagName: String = DetailAdditionFeesFragment::class.java.name
    var idBill = 0
    var sStatus = 0
    var dataDetailBill = DataDetailBill()
    private var adapter: ReceptsItemAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    fun onBody() {
        setAdapter()
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constraintLayoutMain.startAnimation(animFragment)
        mainActivity!!.setBackClick(true)
        mainActivity!!.setHeader("Chi tiết phiếu thu")
        idBill = cacheManager.get(TechresEnum.ID_BILL.toString())!!.toInt()
        sStatus = cacheManager.get(TechresEnum.STATUS_RECEIPTS.toString())!!.toInt()
        when (sStatus) {
            0 -> {
                binding!!.linearLayoutBtn.visibility = View.VISIBLE
            }
            1 -> {
                binding!!.linearLayoutBtn.visibility = View.VISIBLE
            }
            else -> {
                binding!!.linearLayoutBtn.visibility = View.GONE
            }

        }
        getAPIListAdditionFees()
        binding!!.btnAcceptOrder.setOnClickListener {
            binding!!.btnAcceptOrder.isEnabled = false
            mainActivity!!.setLoading(true)
            changeAPIChange()
        }
    }
    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = ReceptsItemAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)
        adapter!!.setClickView(this)
    }
    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }
    private fun changeAPIChange() {
        val params = StatusAdditionFeesParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-addition-fees/$idBill/change-status"
        params.params.status = 2
        params.params.note
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getStatus(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: BaseResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE){
                        mainActivity!!.setLoading(false)
                        binding!!.btnAcceptOrder.isEnabled = true
                        mainActivity!!.supportFragmentManager.popBackStack()
                    }else {
                        binding!!.btnAcceptOrder.isEnabled = true
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }

                }
            })
    }
    private fun getAPIListAdditionFees() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-addition-fees/$idBill"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListAdditionFees(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DetailBillResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("SetTextI18n", "ResourceAsColor")
                override fun onNext(response: DetailBillResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataDetailBill = response.data

                        binding!!.txtBill.text = "Tổng đơn hàng"
                        binding!!.code.text = dataDetailBill.code
                        binding!!.dateCreated.text = dataDetailBill.fee_month
                        binding!!.total.text = currencyFormat(dataDetailBill.amount.toString())
                        when (dataDetailBill.object_type) {
                            2 -> {
                                binding!!.couponType.text = "Chi nhập kho"
                                binding!!.nameCreate.text =
                                    dataDetailBill.restaurant_name
                            }
                            1 -> {
                                binding!!.nameCreate.text =
                                    dataDetailBill.restaurant_name
                                binding!!.couponType.text = "Thu tự động từ đơn hàng"
                            }
                            else -> {
                                binding!!.couponType.text = "Khác"
                                binding!!.nameCreate.text =
                                    dataDetailBill.supplier_employee_create_full_name
                            }
                        }
                        when (dataDetailBill.status) {
                            0 -> {
                                binding!!.nameCreate.text =
                                    dataDetailBill.restaurant_name
                                binding!!.status.text = "Chờ xác nhận"
                                binding!!.status.setTextColor(Color.parseColor("#FFA233"))
                            }

                            1 -> {
                                binding!!.nameCreate.text =
                                    dataDetailBill.restaurant_name
                                binding!!.status.text = "Chờ xác nhận "
                                binding!!.status.setTextColor(Color.parseColor("#0072CB"))
                            }
                            2 -> {
                                binding!!.status.text = "Hoàn tất"
                                binding!!.status.setTextColor(Color.parseColor("#35AC02"))
                            }
                            3 -> {
                                binding!!.status.text = "Đã hủy"
                                binding!!.status.setTextColor(Color.parseColor("#FF0000"))
                            }
                        }
                        binding!!.note.text = dataDetailBill.note
                        binding!!.txtTotal.text =
                            dataDetailBill.supplier_orders.size.toString()


                        adapter!!.setDataReceiptsItem(dataDetailBill.supplier_orders)
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
    override fun onClick(position: Int, id: Int) {
        val orderCompletedDetailFragment = OrderCompletedDetailFragment()
        cacheManager.put(TechresEnum.GET_BY_ID.toString(), id.toString())
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