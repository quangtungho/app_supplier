package vn.techres.supplier.fragment.revenuemanagement.genusitems.check

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
import vn.techres.supplier.adapter.revenueadapter.CheckItemAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentDetailAdditionFeesBinding
import vn.techres.supplier.fragment.inventorymanagement.DetailBillWareHouseFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.datamodel.DataDetailBill
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.DetailBillResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class DetailAdditionFeesCheckFragment :
    BaseBindingFragment<FragmentDetailAdditionFeesBinding>(FragmentDetailAdditionFeesBinding::inflate),
    OnClickTransID {
    val tagName: String = DetailAdditionFeesCheckFragment::class.java.name
    var idBill = 0
    var dataDetailBill = DataDetailBill()
    private var adapter: CheckItemAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    fun onBody() {
        setAdapter()
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constraintLayoutMain.startAnimation(animFragment)
        mainActivity!!.setBackClick(true)
        mainActivity!!.setHeader("Chi tiết phiếu chi")
        idBill = cacheManager.get(TechresEnum.ID_BILL.toString())!!.toInt()
        getAPIListAdditionFees()
    }
    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }
    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = CheckItemAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)
        adapter!!.setClickDetail(this)
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
                        binding!!.code.text = dataDetailBill.code
                        binding!!.dateCreated.text = dataDetailBill.fee_month
                        binding!!.nameCreate.text =
                            dataDetailBill.supplier_employee_create_full_name
                        binding!!.total.text = currencyFormat(dataDetailBill.amount.toString())
                        when (dataDetailBill.object_type) {
                            2 -> binding!!.couponType.text = "Chi nhập kho"
                            1 -> binding!!.couponType.text = "Thu tự động từ đơn hàng"
                            else -> binding!!.couponType.text = "Khác"
                        }
                        when (dataDetailBill.status) {
                            0 -> {
                                binding!!.status.text = "Chờ xác nhận"
                                binding!!.status.setTextColor(Color.parseColor("#FFA233"))
                            }

                            1 -> {
                                binding!!.status.text = "Đã xác nhận "
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
                            dataDetailBill.supplier_warehouse_session_data.size.toString()

//                        if (dataDetailBill.supplier_warehouse_session_data.size == 0) {
//                            binding!!.recyclerView.visibility = View.GONE
//                            binding!!.linearDataNull.visibility = View.VISIBLE
//                        } else {
//                            binding!!.recyclerView.visibility = View.VISIBLE
//                            binding!!.linearDataNull.visibility = View.GONE
//                        }
                        adapter!!.setDataCheckItem(dataDetailBill.supplier_warehouse_session_data)
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
        val detailBillWareHouseFragment = DetailBillWareHouseFragment()
        cacheManager.put(TechresEnum.WARE_HOUSE_ID.toString(), id.toString())
        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.nav_host, DetailBillWareHouseFragment())
                    ?.addToBackStack(detailBillWareHouseFragment.tagName)
                    ?.commit()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}