package vn.techres.supplier.fragment.inventorymanagement

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.inventoryadapter.InventoryWareHouseAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentExportBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.model.WareHouse
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.WareHouseResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

class ExportFragment : BaseBindingFragment<FragmentExportBinding>(FragmentExportBinding::inflate),
    OnClickTransID {
    val tagName: String = ExportFragment::class.java.name

    private var listData = ArrayList<WareHouse>()
    private var adapter: InventoryWareHouseAdapter? = null
    private var dateTo = ""
    private var dateFrom = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        fragmentCreate()
        setAdapter()
        dataFrom()
        dataTo()
        val animScaleShowTopToBottom =
            AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.rcvWarehouse.startAnimation(animScaleShowTopToBottom)

        //  swipeRefreshLayout
        binding!!.swipeRefreshLayout.setOnRefreshListener {
            getAPIListExport(
                binding!!.fromData.text.toString(),
                binding!!.toData.text.toString()
            )
            binding!!.swipeRefreshLayout.isRefreshing = false
        }

        binding!!.btnCreate.setOnClickListener {
            val createWareHouseFragment = CreateWareHouseFragment()
            cacheManager.put(
                TechresEnum.TYPE_WARE_HOUSE.toString(),
                TechresEnum.TYPE_OUT_WAREHOUSE.toString()
            )
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, CreateWareHouseFragment())
                        ?.addToBackStack(createWareHouseFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }

        binding!!.fromData.setOnClickListener {
            dialogFromDatePicker()
        }
        binding!!.toData.setOnClickListener {
            dialogToDatePicker()
        }
        searchExport()
    }

    override fun onResume() {
        super.onResume()
        getAPIListExport(
            binding!!.fromData.text.toString(),
            binding!!.toData.text.toString()
        )
    }

    private fun fragmentCreate() {
        mainActivity!!.setHeader(getString(R.string.manager_ware_house))

        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
        binding!!.constrainLayoutMain.startAnimation(animFragment)

        binding!!.txtTotalStatusWareHouse.text = getString(R.string.txt_status_tatol_export)
    }

    private fun searchExport() {
        binding!!.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList = java.util.ArrayList<WareHouse>()
                for (key in listData) {
                    if (listData.size > 0) {
                        val name: String = key.code!!
                        if (name.lowercase(Locale.getDefault())
                                .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapter!!.setDataList(newList)
                binding!!.rcvWarehouse.setHasFixedSize(true)
                binding!!.rcvWarehouse.adapter = adapter

                return false
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun dataFrom() {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
        binding!!.toData.text = dateTo
    }

    @SuppressLint("SetTextI18n")
    fun dataTo() {
        val sdfTo = SimpleDateFormat(getString(R.string.mmyyyy), Locale.getDefault())
        dateFrom = "01/" + sdfTo.format(Date())
        binding!!.fromData.text = dateFrom

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
            getAPIListExport(
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
            getAPIListExport(
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

    private fun setAdapter() {
        adapter = InventoryWareHouseAdapter(mainActivity!!.baseContext)
        binding!!.rcvWarehouse.adapter = adapter
        binding!!.rcvWarehouse.layoutManager = LinearLayoutManager(context)
        binding!!.rcvWarehouse.setHasFixedSize(true)
        adapter!!.setClickDetail(this)
    }

    /**
     * Lấy danh sách phiếu nhập kho theo khoảng thời gian từ ngày (txtFromDate) -> ngày (txtToDate)
     */
    private fun getAPIListExport(formDate: String, txtToDate: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-warehouse-sessions?type=${TechresEnum.STATUS_1},${TechresEnum.STATUS_4}&from_date=$formDate&to_date=$txtToDate&limit=50&page=1"

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
                override fun onNext(response: WareHouseResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listData = response.data.list
                        if (listData.size == 0) {
                            binding!!.linearDataNull.visibility = View.VISIBLE
                            binding!!.rcvWarehouse.visibility = View.GONE
                        } else {
                            binding!!.linearDataNull.visibility = View.GONE
                            binding!!.rcvWarehouse.visibility = View.VISIBLE
                        }

                        adapter!!.setDataList(listData)
                        binding!!.txtTotalWareHouse.text = listData.size.toString()
                    } else {
                        Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })
    }

    override fun onClick(position: Int, id: Int) {
        val detailBillWareHouseFragment = DetailBillWareHouseFragment()
        cacheManager.put(TechresEnum.WARE_HOUSE_ID.toString(), id.toString())
        cacheManager.put(
            TechresEnum.TYPE_WARE_HOUSE.toString(),
            TechresEnum.TYPE_OUT_WAREHOUSE.toString()
        )
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

    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}