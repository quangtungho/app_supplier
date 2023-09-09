package vn.techres.supplier.fragment.inventorymanagement

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.inventoryadapter.DetailMaterialAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentDetailBillWareHouseBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataMaterialSelected
import vn.techres.supplier.model.WareHouse
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.StatusWareHouseParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.DetailWarehouseResponse
import vn.techres.supplier.model.response.MaterialSelectedResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat

class DetailBillWareHouseFragment :
        BaseBindingFragment<FragmentDetailBillWareHouseBinding>(FragmentDetailBillWareHouseBinding::inflate) {
    var tagName: String = DetailWareHouseFragment::class.java.name
    private var listDataMaterial = DataMaterialSelected()
    private var dataDetail = WareHouse()
    private var adapter: DetailMaterialAdapter? = null
    private var idWareHouse = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        idWareHouse = cacheManager.get(TechresEnum.WARE_HOUSE_ID.toString())!!.toInt()
        getAPIListMaterial(idWareHouse)
        getAPIDetailWareHouseById(idWareHouse)
        binding!!.btnEdit.setOnClickListener {
            cacheManager.put(TechresEnum.WARE_HOUSE_ID.toString(), idWareHouse.toString())
            val detailWareHouseFragment = DetailWareHouseFragment()
            Handler(Looper.getMainLooper()).postDelayed(
                    {
                        activity?.supportFragmentManager
                                ?.beginTransaction()
                                ?.replace(R.id.nav_host, DetailWareHouseFragment())
                                ?.addToBackStack(detailWareHouseFragment.tagName)
                                ?.commit()
                    }, TechresEnum.TIME_100.toString().toLong()
            )
        }
        binding!!.btnCofirm.setOnClickListener {
            binding!!.btnCofirm.isEnabled = false
            Handler().postDelayed({ binding!!.btnCofirm.isEnabled = true }, 5000)
            getAPIConfirmWareHouse(idWareHouse)
        }
    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = DetailMaterialAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)
    }

    //Xác nhận phiếu nhập kho
    private fun getAPIConfirmWareHouse(id: Int) {
        val params = StatusWareHouseParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-warehouse-sessions/$id/change-status"
        params.params.status = 2
        ServiceFactory.createRetrofitService(
                TechResService::class.java, mainActivity!!
        )
                .getConfirmWareHouse(params)
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
                            val snack = Snackbar.make(
                                    binding!!.constraintLayoutMain,
                                    "Xác nhận phiếu thành công",
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()

                        } else {
                            val snack = Snackbar.make(
                                    binding!!.constraintLayoutMain,
                                    response.status,
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()
                        }

                    }
                })
    }

    //API lấy danh sách nguyên liệu đã chọn
    private fun getAPIListMaterial(id: Int) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-warehouse-sessions/details?id=$id&limit=100&page=0"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, mainActivity!!
        )
                .getListMaterialSelected(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MaterialSelectedResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: MaterialSelectedResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDataMaterial = response.data
                            adapter!!.setDataList(listDataMaterial)

                        } else {
                            val snack = Snackbar.make(
                                    binding!!.constraintLayoutMain,
                                    response.status,
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()
                        }

                    }
                })
    }

    /**
     * API lấy danh sách chi tiết sản phẩm của 1 phiếu kho
     */
    private fun getAPIDetailWareHouseById(id: Int) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-warehouse-sessions/$id"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, mainActivity!!
        )
                .getDetailMaterialWareHouse(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DetailWarehouseResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("SetTextI18n")
                    override fun onNext(response: DetailWarehouseResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            dataDetail = response.data
                            if (dataDetail.status == 0 && dataDetail.type == 0 && dataDetail.payment_status == 0)
                                binding!!.linearLayoutBtn.visibility = View.VISIBLE
                            else
                                binding!!.linearLayoutBtn.visibility = View.GONE

                            binding!!.code.text = dataDetail.code
                            binding!!.dateCreated.text = dataDetail.created_at
                            binding!!.note.text = dataDetail.note
                            binding!!.txtTotal.text = dataDetail.total_material.toString()
                            binding!!.discount.text =
                                    currencyFormatVND(dataDetail.discount_amount.toString())
                            binding!!.vat.text = currencyFormatVND(dataDetail.vat_amount.toString())
                            binding!!.txtTotalMoney.text =
                                    currencyFormatVND(dataDetail.amount.toString())
                            binding!!.totail.text =
                                    currencyFormatVND(dataDetail.total_amount.toString())
                            when (dataDetail.status) {
                                0 -> {
                                    binding!!.status.text = "Đang xử lý"
                                    binding!!.status.setTextColor(Color.parseColor("#FFA233"))
                                }
                                1 -> {
                                    binding!!.status.text = "Đang xử lý"
                                    binding!!.status.setTextColor(Color.parseColor("#FFA233"))
                                }
                                2 -> {
                                    binding!!.status.text = "Đã hoàn tất"
                                    binding!!.status.setTextColor(Color.parseColor("#35AC02"))
                                }
                                3 -> {
                                    binding!!.status.text = "Đã hủy"
                                    binding!!.status.setTextColor(Color.parseColor("#FF0000"))
                                }
                            }
                            when (dataDetail.payment_status) {
                                0 -> {
                                    binding!!.statusTotail.text = "Chờ thanh toán"
                                    binding!!.statusTotail.setTextColor(Color.parseColor("#FFA233"))
                                }
                                1 -> {
                                    binding!!.statusTotail.text = "Chờ xác nhận thanh toán"
                                    binding!!.statusTotail.setTextColor(Color.parseColor("#FFA233"))
                                }
                                2 -> {
                                    binding!!.statusTotail.text = "Đã thanh toán"
                                    binding!!.statusTotail.setTextColor(Color.parseColor("#35AC02"))
                                }
                                4 -> {
                                    binding!!.statusTotail.text = "Đã hủy"
                                    binding!!.statusTotail.setTextColor(Color.parseColor("#FF0000"))
                                }
                            }

                        } else {
                            val snack = Snackbar.make(
                                    binding!!.constraintLayoutMain,
                                    response.status,
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()
                        }

                    }
                })
    }

    private fun currencyFormatVND(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    override fun onBackPress() {
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    mainActivity!!.supportFragmentManager.popBackStack()
                }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}