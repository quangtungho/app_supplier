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
import vn.techres.supplier.databinding.FragmentCreateWareHouseBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.MaterialHandler
import vn.techres.supplier.model.datamodel.DataMaterialWareHouse
import vn.techres.supplier.model.datamodel.MaterialManager
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.CreateWareHouseParams
import vn.techres.supplier.model.response.CreateWareHouseResponse
import vn.techres.supplier.model.response.ListMaterialManagerResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat

class CreateWareHouseFragment :
        BaseBindingFragment<FragmentCreateWareHouseBinding>(FragmentCreateWareHouseBinding::inflate),
        MaterialHandler {
    var tagName: String = CreateWareHouseFragment::class.java.name
    private val listDataMaterial = ArrayList<MaterialManager>()
    private val listDataMaterialSelected = ArrayList<MaterialManager>()
    private var listSpinnerCategory = ArrayList<String>()
    private var listIdRequestMaterialWareHouse = ArrayList<DataMaterialWareHouse>()
    private var checkCategory = false
    private var adapter: MaterialCreateAdapter? = null
    private var typeWareHouse = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        fragmentCreate()
        setClick()
    }

    private fun setClick() {
        binding!!.btnClose.setOnClickListener {
            onBackPress()
        }
        binding!!.btnCreate.setOnClickListener {
            binding!!.btnCreate.isEnabled = false
            Handler().postDelayed({ binding!!.btnCreate.isEnabled = true }, 5000)
            createWareHouse()
        }
        getAPIListMaterial()
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
        typeWareHouse = cacheManager.get(TechresEnum.TYPE_WARE_HOUSE.toString())!!.toInt()
        if (typeWareHouse == TechresEnum.TYPE_IN_WAREHOUSE.toString().toInt()) {
            mainActivity!!.setHeader(getString(R.string.create_ware_house_in))
        } else if (typeWareHouse == TechresEnum.TYPE_OUT_WAREHOUSE.toString().toInt()) {
            mainActivity!!.setHeader(getString(R.string.create_ware_house_out))
        }
        mainActivity!!.setLoading(false)
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
                TechResService::class.java, requireActivity()
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
                            for (i in response.data.indices) {
                                listDataMaterial.add(response.data[i])
                                listSpinnerCategory.add(response.data[i].name)
                            }

//                        //Lấy danh sách DANH MỤC add vào spinner
                            binding!!.spinnerMaterial.adapter = ArrayAdapter(
                                    requireActivity(),
                                    R.layout.color_spinner_transparent,
                                    listSpinnerCategory
                            )
                            (binding!!.spinnerMaterial.adapter as ArrayAdapter<*>).setDropDownViewResource(
                                    android.R.layout.simple_list_item_1
                            )
                            binding!!.spinnerMaterial.setSelection(0, true)
                            binding!!.spinnerMaterial.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        @SuppressLint("SetTextI18n")
                                        override fun onItemSelected(
                                                parent: AdapterView<*>?,
                                                view: View?,
                                                position: Int,
                                                id: Long,
                                        ) {
                                            listDataMaterialSelected.add(listDataMaterial[position])
                                            if (!checkCategory) {
                                                listDataMaterialSelected.removeAt(0)
                                                checkCategory = true
                                            } else {
                                                adapter!!.setDataList(listDataMaterialSelected)
                                            }
                                            (binding!!.spinnerMaterial.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                                            binding!!.txtTotalMaterial.text =
                                                    listDataMaterialSelected.size.toString()
                                            var totalMoney = 0.0
                                            for (i in listDataMaterialSelected.indices) {
                                                totalMoney =
                                                        (totalMoney + (listDataMaterialSelected[i].quantity * listDataMaterialSelected[i].retail_price))
                                            }
                                            binding!!.txtTotalMoney.text =
                                                    currencyFormatVND(totalMoney.toString())
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>?) {
                                        }
                                    }

                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }

                    }
                })
    }

    /**
     *
     * API tạo mới phiếu xuất nhập kho
     */
    private fun createWareHouse() {
        val params = CreateWareHouseParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-warehouse-sessions/create"

        params.params.type = typeWareHouse
        params.params.discount_type = 0
        params.params.discount_amount = 0
        params.params.discount_percent = 0
        if (binding!!.cbVAT.isChecked) {
            params.params.vat_percent = binding!!.edtInputPercent.text.toString().toFloat()
        } else {
            params.params.vat_percent = 0f
        }
        params.params.note = binding!!.edtNote.text.toString()


        listIdRequestMaterialWareHouse.clear()
        for (i in listDataMaterialSelected.indices) {
            val idRequestMaterialWareHouse = DataMaterialWareHouse()
            idRequestMaterialWareHouse.supplier_material_id = listDataMaterialSelected[i].id
            idRequestMaterialWareHouse.supplier_input_quantity =
                    listDataMaterialSelected[i].quantity
            idRequestMaterialWareHouse.supplier_input_unit_type = 1
            idRequestMaterialWareHouse.supplier_input_price =
                    listDataMaterialSelected[i].retail_price.toFloat()
            idRequestMaterialWareHouse.note = listDataMaterialSelected[i].note
            listIdRequestMaterialWareHouse.add(idRequestMaterialWareHouse)
        }
        params.params.material_datas = listIdRequestMaterialWareHouse

        params.let {
            ServiceFactory.createRetrofitService(
                    TechResService::class.java, mainActivity!!
            )
                    .createWareHouse(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CreateWareHouseResponse> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onNext(response: CreateWareHouseResponse) {
                            if (response.status == AppConfig.SUCCESS_CODE) {
                                cacheManager.put(TechresEnum.UPDATE_LIST_WARE_HOUSE.toString(), "true")
                                val snack = Snackbar.make(
                                        binding!!.constraintLayoutMain,
                                        resources.getString(R.string.toast_create_success),
                                        Snackbar.LENGTH_LONG
                                )
                                snack.show()
                                mainActivity!!.supportFragmentManager.popBackStack()
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