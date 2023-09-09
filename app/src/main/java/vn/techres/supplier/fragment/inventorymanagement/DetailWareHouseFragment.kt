package vn.techres.supplier.fragment.inventorymanagement

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.materialadapter.MaterialCreateAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentDetailWareHouseBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.MaterialHandler
import vn.techres.supplier.model.datamodel.DataMaterialWareHouse
import vn.techres.supplier.model.datamodel.MaterialManager
import vn.techres.supplier.model.WareHouse
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.UpdateWareHouseParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.DetailWarehouseResponse
import vn.techres.supplier.model.response.ListMaterialManagerResponse
import vn.techres.supplier.model.response.MaterialSelectedResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat

class DetailWareHouseFragment :
        BaseBindingFragment<FragmentDetailWareHouseBinding>(FragmentDetailWareHouseBinding::inflate),
        MaterialHandler {
    var tagName: String = DetailWareHouseFragment::class.java.name

    private val listDataMaterial = ArrayList<MaterialManager>()
    private val listDataMaterialSelected = ArrayList<MaterialManager>()
    private var listSpinnerCategoryString = ArrayList<String>()
    private var listIdRequestMaterialWareHouse = ArrayList<DataMaterialWareHouse>()
    private var dataDetail = WareHouse()
    private var adapter: MaterialCreateAdapter? = null
    private var idWareHouse = 0
    private var typeWareHouse = 0

    private var listDataMaterialTemp = ArrayList<MaterialManager>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()

        idWareHouse = cacheManager.get(TechresEnum.WARE_HOUSE_ID.toString())!!.toInt()

        getAPIListMaterialSelected(idWareHouse)

        getAPIDetailWareHouseById(idWareHouse)

        fragmentCreate()

        setClick()

        binding!!.btnUpdate.visibility = View.VISIBLE

        getAPIListMaterial()

        binding!!.txtDate.isEnabled = true
        binding!!.cbVAT.isEnabled = true
        binding!!.edtNote.isEnabled = true

        //Lấy danh sách DANH MỤC add vào spinner
        binding!!.spinnerMaterial.adapter = context?.let {
            ArrayAdapter(
                    it,
                    R.layout.color_spinner_transparent,
                    listSpinnerCategoryString
            )
        }

        (binding!!.spinnerMaterial.adapter as ArrayAdapter<*>).setDropDownViewResource(
                android.R.layout.simple_list_item_1
        )
        binding!!.spinnerMaterial.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        val selectedItem = parent.getItemAtPosition(position).toString()
                        if (selectedItem == "Tung") {
                            val materialManager = listDataMaterial[position]
                            materialManager.supplier_material_id =
                                    listDataMaterial[position].id
                            materialManager.id = 0

                            listDataMaterialSelected.add(materialManager)
                            listDataMaterial.removeAt(position)
                            listSpinnerCategoryString.removeAt(position)

                            binding!!.spinnerMaterial.adapter = context?.let {
                                ArrayAdapter(
                                        it,
                                        R.layout.color_spinner_transparent,
                                        listSpinnerCategoryString
                                )
                            }

                            adapter!!.notifyDataSetChanged()

                            binding!!.txtTotalMaterial.text =
                                    listDataMaterialSelected.size.toString()

                            var totalMoney = 0.0
                            for (i in listDataMaterialSelected.indices) {
                                totalMoney =
                                        (totalMoney + (listDataMaterialSelected[i].quantity * listDataMaterialSelected[i].retail_price))
                            }
                            binding!!.txtTotalMoney.text = currencyFormatVND(totalMoney.toString())
                        }
                    } // to close the onItemSelected

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
    }

    private fun setClick() {
        binding!!.btnUpdate.setOnClickListener {
            binding!!.btnUpdate.isEnabled = false
            Handler().postDelayed({ binding!!.btnUpdate.isEnabled = true }, 5000)
            updateWareHouse()
        }
        binding!!.cbVAT.setOnClickListener {
            if (binding!!.cbVAT.isChecked) {
                binding!!.lnInputPercent.visibility = View.VISIBLE
            } else {
                binding!!.lnInputPercent.visibility = View.GONE
            }
        }
    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = MaterialCreateAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)
        adapter?.setMaterialHandler(this)
    }


    private fun fragmentCreate() {
        if (typeWareHouse == TechresEnum.TYPE_IN_WAREHOUSE.toString().toInt()) {
            mainActivity!!.setHeader(getString(R.string.detail_ware_house_in))
        } else if (typeWareHouse == TechresEnum.TYPE_OUT_WAREHOUSE.toString().toInt()) {
            mainActivity!!.setHeader(getString(R.string.detail_ware_house_out))
        }
    }

    /**
     * API lấy danh sách nguyên liệu
     * Add những nguyên liệu có trạng thái = 1 vào model local listdata
     */
    //API lấy danh sách nguyên liệu
    private fun getAPIListMaterial() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/materials"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, mainActivity!!
        )
                .getListMaterial(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ListMaterialManagerResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: ListMaterialManagerResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            val listData = response.data
                            listDataMaterialTemp = response.data
                            var count = 0
                            for (i in listData.indices) {
                                for (j in listDataMaterialSelected.indices) {
                                    if (count == listDataMaterialSelected.size) {
                                        break
                                    }
                                    if (listData[i].id == listDataMaterialSelected[j].supplier_material_id) {
                                        listData.removeAt(i)
                                        count++
                                    }
                                }
                            }

                            listDataMaterial.clear()
                            listDataMaterial.addAll(listData)

                            listSpinnerCategoryString.clear()
                            for (i in listData.indices) {
                                listSpinnerCategoryString.add(listData[i].name)
                            }

                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
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
                            binding!!.txtDate.text = dataDetail.created_at
                            binding!!.edtNote.setText(dataDetail.note)
                            if (dataDetail.vat != 0f) {
                                binding!!.cbVAT.isChecked = true
                                binding!!.lnInputPercent.visibility = View.VISIBLE
                                binding!!.edtInputPercent.setText(dataDetail.vat.toString())

                            } else {
                                binding!!.lnInputPercent.visibility = View.GONE
                            }
                            binding!!.txtTotalMoney.text =
                                    currencyFormatVND(dataDetail.total_amount.toString())
                            binding!!.txtTotalMaterial.text = dataDetail.total_material.toString()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }

                    }
                })
    }

    /**
     * API lấy danh sách nguyên liệu
     * Add những nguyên liệu có trạng thái = 1 vào model local listdata
     */
    //API lấy danh sách nguyên liệu đã chọn
    private fun getAPIListMaterialSelected(id: Int) {
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
                            val responseData = response.data.list
                            for (i in responseData.indices) {
                                val materialSelected = MaterialManager()
                                materialSelected.id = responseData[i].id
                                materialSelected.supplier_material_id =
                                        responseData[i].supplier_material_id
                                materialSelected.name = responseData[i].supplier_material_name
                                materialSelected.material_unit_name = responseData[i].unit_name
                                materialSelected.material_unit_specification_name =
                                        responseData[i].unit_specification_exchange_name
                                materialSelected.material_unit_id =
                                        responseData[i].supplier_input_unit_type

                                materialSelected.quantity = responseData[i].supplier_input_quantity
                                materialSelected.retail_price = responseData[i].supplier_input_price
                                materialSelected.note = responseData[i].note
                                listDataMaterialSelected.add(materialSelected)
                            }
                            adapter!!.setDataList(listDataMaterialSelected)
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }

                    }
                })
    }

    /**
     *
     * API cập nhật phiếu nhập kho
     */
    private fun updateWareHouse() {
        val params = UpdateWareHouseParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-warehouse-sessions/$idWareHouse/update"

        params.params.discount_type = 0
        params.params.discount_percent = 0

        if (binding!!.cbVAT.isChecked) {
            params.params.vat_percent = binding!!.edtInputPercent.text.toString().toFloat()
        } else {
            params.params.vat_percent = 0f
        }

        params.params.note = binding!!.edtNote.text.toString()

        listIdRequestMaterialWareHouse.clear()
        for (i in listDataMaterialSelected.indices) {
            val dataMaterialWareHouse = DataMaterialWareHouse()
            dataMaterialWareHouse.supplier_warehouse_session_detail_id =
                    listDataMaterialSelected[i].id
            dataMaterialWareHouse.supplier_material_id =
                    listDataMaterialSelected[i].supplier_material_id
            dataMaterialWareHouse.supplier_input_quantity = listDataMaterialSelected[i].quantity
            dataMaterialWareHouse.supplier_input_unit_type = 1
            dataMaterialWareHouse.supplier_input_price =
                    listDataMaterialSelected[i].retail_price.toFloat()
            dataMaterialWareHouse.note = listDataMaterialSelected[i].note
            dataMaterialWareHouse.quantity =
                    listDataMaterialSelected[i].retail_price * listDataMaterialSelected[i].quantity
            listIdRequestMaterialWareHouse.add(dataMaterialWareHouse)
        }
        params.params.material_datas = listIdRequestMaterialWareHouse

        params.let {
            ServiceFactory.createRetrofitService(
                    TechResService::class.java, mainActivity!!
            )
                    .updateWareHouse(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseResponse> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onNext(response: BaseResponse) {
                            if (response.status == AppConfig.SUCCESS_CODE) {
                                cacheManager.put(TechresEnum.UPDATE_LIST_WARE_HOUSE.toString(), "true")
                                val snack = Snackbar.make(
                                        binding!!.constraintLayoutMain,
                                        resources.getString(R.string.toast_edit_success),
                                        Snackbar.LENGTH_LONG
                                )
                                snack.show()

                                onBackPress()

                            } else {

                                val snack = Snackbar.make(
                                        binding!!.constraintLayoutMain,
                                        response.status,
                                        Snackbar.LENGTH_LONG
                                )
                                snack.show()

                            }
                        }

                        override fun onError(e: Throwable) {
                        }

                        override fun onComplete() {}

                    })
        }
    }


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onClick(position: Int, id: Int, model: MaterialManager) {
        listDataMaterialSelected.removeAt(position)
        adapter!!.notifyDataSetChanged()

        for (i in listDataMaterialTemp.indices) {
            if (listDataMaterialTemp[i].id == model.supplier_material_id) {
                listDataMaterial.add(listDataMaterialTemp[i])
                break
            }
        }

        (binding!!.spinnerMaterial.adapter as ArrayAdapter<*>).notifyDataSetChanged()

        binding!!.txtTotalMaterial.text = listDataMaterialSelected.size.toString()
        var totalMoney = 0.0
        for (i in listDataMaterialSelected.indices) {
            totalMoney =
                    (totalMoney + (listDataMaterialSelected[i].quantity * listDataMaterialSelected[i].retail_price))
        }
        binding!!.txtTotalMoney.text =
                currencyFormatVND(totalMoney.toString()) + " " + this.getString(R.string.txt_vnd)

    }

    @SuppressLint("UseRequireInsteadOfGet", "SetTextI18n")
    override fun inputMaterial(position: Int, model: MaterialManager, type: String) {
        val dialog: Dialog? = this.context?.let { Dialog(it) }!!
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_input_quantity)

        val txtTitle: TextView = dialog.findViewById(R.id.txtTitle)
        val edtInputQuantity: EditText = dialog.findViewById(R.id.edtInputQuantity)
        val edtInputPrice: EditText = dialog.findViewById(R.id.edtInputPrice)
        val edtInputNote: EditText = dialog.findViewById(R.id.edtInputNote)
        val btnConfirm: Button = dialog.findViewById(R.id.btnConfirm)
        when (type) {

            "quantity" -> {
                edtInputQuantity.visibility = View.VISIBLE
                edtInputPrice.visibility = View.GONE
                edtInputNote.visibility = View.GONE
                txtTitle.setText(R.string.input_quantity)

                btnConfirm.setOnClickListener {
                    model.quantity = edtInputQuantity.text.toString().toDouble()
                    adapter!!.notifyItemChanged(position)
                    binding!!.txtTotalMoney.text =
                            currencyFormatVND((model.retail_price * model.quantity).toString()) + " " + this.getString(
                                    R.string.txt_vnd
                            )
                    dialog.dismiss()
                }
            }
            "price" -> {
                edtInputPrice.visibility = View.VISIBLE
                edtInputQuantity.visibility = View.GONE
                edtInputNote.visibility = View.GONE
                txtTitle.setText(R.string.input_price)

                btnConfirm.setOnClickListener {
                    model.retail_price = edtInputPrice.text.toString().toDouble()
                    adapter!!.notifyItemChanged(position)
                    binding!!.txtTotalMoney.text =
                            currencyFormatVND((model.retail_price * model.quantity).toString()) + " " + this.getString(
                                    R.string.txt_vnd
                            )
                    dialog.dismiss()
                }
            }
            "note" -> {
                edtInputNote.visibility = View.VISIBLE
                edtInputQuantity.visibility = View.GONE
                edtInputQuantity.visibility = View.GONE
                txtTitle.setText(R.string.input_note)

                btnConfirm.setOnClickListener {
                    model.note = edtInputNote.text.toString()
                    adapter!!.notifyItemChanged(position)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
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