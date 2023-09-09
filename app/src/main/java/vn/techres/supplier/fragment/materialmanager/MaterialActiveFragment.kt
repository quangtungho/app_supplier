package vn.techres.supplier.fragment.materialmanager

import android.R.attr.editable
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.view.CurrencyEditText
import vn.techres.supplier.R
import vn.techres.supplier.adapter.materialadapter.MaterialActiveAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentMaterialActiceAndPauseBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.interfaces.OnClickTransIdAndModelMaterial
import vn.techres.supplier.model.datamodel.DataCategory
import vn.techres.supplier.model.datamodel.DataSpecification
import vn.techres.supplier.model.datamodel.DataUnits
import vn.techres.supplier.model.datamodel.MaterialManager
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.CreateMaterialParams
import vn.techres.supplier.model.response.*
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.TechResTextView
import java.util.*


/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class MaterialActiveFragment :
        BaseBindingFragment<FragmentMaterialActiceAndPauseBinding>(FragmentMaterialActiceAndPauseBinding::inflate),
        OnClickTransID, OnClickTransIdAndModelMaterial {

    private var adapter: MaterialActiveAdapter? = null
    private val listDataMaterial = ArrayList<MaterialManager>()
    private var listDataCategory = ArrayList<DataCategory>()
    private var listDataUnits = ArrayList<DataUnits>()
    private val listDataSpecification = ArrayList<DataSpecification>()

    private var listSpinnerCategoryString = ArrayList<String>()
    private var listSpinnerUnitsString = ArrayList<String>()
    private var listSpinnerSpecificationString = ArrayList<String>()

    private var position = 0
    private var idMaterial = 0
    private var idCategory = 0
    private var idUnits = 0
    private var idSpecification = 0
    private var status = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        //onCreate
        fragmentCreate()
        getAPIListMaterial()
        getAPIListCategoryActive()
        getAPIListSpecification()
        getAPIListUnits()
        searchMaterialActive()
        binding!!.btnCreate.setOnClickListener {
            openDialogCreate()
        }
    }

    override fun onResume() {
        super.onResume()
        listDataMaterial.clear()
        getAPIListMaterial()
        listDataCategory.clear()
        getAPIListCategoryActive()
        listDataUnits.clear()
        getAPIListUnits()
        listDataSpecification.clear()
        getAPIListSpecification()
    }

    private fun fragmentCreate() {
        binding!!.txtTotalStatusMaterial.text = getString(R.string.txt_total_material_active)

    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = MaterialActiveAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)
        adapter?.setClickRecyclerDelete(this)
        adapter?.setClickRecyclerEdit(this)
    }


    private fun searchMaterialActive() {
        binding!!.btnSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList = ArrayList<MaterialManager>()
                for (key in listDataMaterial) {
                    if (listDataMaterial.size > 0) {
                        val name: String = key.name
                        if (name.lowercase(Locale.getDefault())
                                        .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapter!!.setDataList(newList)
                binding!!.recyclerView.setHasFixedSize(true)
                binding!!.recyclerView.adapter = adapter

                return false
            }
        })
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

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onNext(response: ListMaterialManagerResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDataMaterial.clear()
                            for (item in response.data.indices) {
                                if (response.data[item].status == 1) {
                                    listDataMaterial.add(response.data[item])
                                }
                            }
                            adapter!!.setDataList(listDataMaterial)
                            adapter!!.notifyDataSetChanged()
                            binding!!.txtTotalMaterial.text = listDataMaterial.size.toString()

                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                        }

                    }
                })
    }

    /**
     * API lấy danh sách danh mục trạng thái đang hoạt động
     * Trạng thái hoạt động: status = 1
     */
    private fun getAPIListCategoryActive() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/material-categories?status=${TechresEnum.MATERIAL_ACTIVE_MANAGER}"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListCategoryActive(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CategoryActiveResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {

                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: CategoryActiveResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDataCategory.clear()
                            for (i in response.data.indices) {
                                listDataCategory.add(response.data[i])
                                listSpinnerCategoryString.add(response.data[i].name)
                            }
                        } else
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                    }
                })
    }

    /**
     * API lấy danh sách đơn vị trạng thái đang hoạt động
     * Trạng thái hoạt động: status = 1
     */
    private fun getAPIListUnits() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/material-units/"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListUnits(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UnitsResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: UnitsResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDataUnits.clear()
                            for (i in response.data.indices) {
                                if (response.data[i].status == 1) {
                                    listDataUnits.add(response.data[i])
                                    listSpinnerUnitsString.add(response.data[i].name)
                                }
                            }
                        } else
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                    }
                })
    }

    /**
     * API lấy danh sách quy cách trạng thái đang hoạt động
     * Trạng thái hoạt động: status = 1
     */
    private fun getAPIListSpecification() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-material-unit-specifications/"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListSpecification(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SpecificationResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: SpecificationResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDataSpecification.clear()
                            for (i in response.data!!.indices) {
                                if (response.data!![i].status == 1) {
                                    listDataSpecification.add(response.data!![i])
                                    listSpinnerSpecificationString.add(response.data!![i].name)
                                }
                            }
                        } else
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                    }
                })
    }

    //Tạo nguyên liệu mới
    private fun createAPIMaterial(
            name: String,
            code: String,
            status: Int,
            idCategory: Int,
            idUnits: Int,
            idSpecification: Int,
            costPrice: String,
            retailPrice: String,
            wholesakePrice: String,
            numberWholesakePrice: String,
            numberOutOfStock: String,
            wastageRate: String,
            dialog: Dialog,
    ) {
        val params = CreateMaterialParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/materials/create"

        params.params.name = name
        params.params.code = code
        params.params.status = TechresEnum.STATUS_1.toString().toInt()
        params.params.material_category_id = idCategory
        params.params.material_unit_id = idUnits
        params.params.material_unit_specification_id = idSpecification
        params.params.cost_price = AppUtils.trimCommaOfString(costPrice).toFloat()
        params.params.retail_price = AppUtils.trimCommaOfString(retailPrice).toFloat()
        params.params.wholesale_price =AppUtils.trimCommaOfString(wholesakePrice).toFloat()
        params.params.wholesale_price_quantity = numberWholesakePrice.toFloat()
        params.params.out_stock_alert_quantity = numberOutOfStock.toFloat()
        params.params.wastage_rate = wastageRate.toFloat()

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .createMaterial(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CreateMaterialResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: CreateMaterialResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDataMaterial.clear()
                            getAPIListMaterial()
                            mainActivity!!.setLoading(false)
                            dialog.dismiss()
                            val snack = Snackbar.make(
                                    requireView(),
                                    getString(R.string.toast_create_success),
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()
                        } else {
                            Toast.makeText(
                                    requireActivity(),
                                    response.message,
                                    Toast.LENGTH_SHORT
                            )
                                    .show()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {}

                })
    }

    //API chỉnh sửa nguyên liệu
    private fun editAPIMaterial(
            name: String,
            code: String,
            idCategory: Int,
            idUnits: Int,
            idSpecification: Int,
            costPrice: String,
            retailPrice: String,
            wholesakePrice: String,
            numberWholesakePrice: String,
            numberOutOfStock: String,
            dialog: Dialog,
    ) {
        val params = CreateMaterialParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/materials/$idMaterial"

        params.params.name = name
        params.params.code = code
        params.params.material_category_id = idCategory
        params.params.material_unit_id = idUnits
        params.params.material_unit_specification_id = idSpecification
        params.params.cost_price = costPrice.toFloat()
        params.params.retail_price = retailPrice.toFloat()
        params.params.wholesale_price = wholesakePrice.toFloat()
        params.params.wholesale_price_quantity = numberWholesakePrice.toFloat()
        params.params.out_stock_alert_quantity = numberOutOfStock.toFloat()
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .createMaterial(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CreateMaterialResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: CreateMaterialResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDataMaterial.clear()
                            getAPIListMaterial()
                            dialog.dismiss()
                            val snack = Snackbar.make(
                                    requireView(),
                                    getString(R.string.toast_edit_success),
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

    //API thay đổi trạng thái hoạt động của nguyên liệu
    private fun changeAPIStatus() {
        val params = BaseParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/materials/$idMaterial/change-status"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .changeStatusMaterial(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ChangeStatusMaterialResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onNext(response: ChangeStatusMaterialResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listDataMaterial.removeAt(position)
                            adapter!!.notifyDataSetChanged()
                            binding!!.txtTotalMaterial.text = listDataMaterial.size.toString()
                            val snack = Snackbar.make(
                                    requireView(),
                                    getString(R.string.toast_change_status_success),
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()
                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                        }
                    }
                })
    }

    //Dialog thông báo khi bấm nút thay đổi trạng thái của 1 nguyên liệu
    private fun openDialogNotify() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_notify)
        val window = dialog.window
        window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtTitle: TechResTextView = dialog.findViewById(R.id.txtTitle)
        val txtNotify: TechResTextView = dialog.findViewById(R.id.txtContent)
        val btnYes: Button = dialog.findViewById(R.id.yes)
        val btnNo: Button = dialog.findViewById(R.id.no)

        txtTitle.setText(R.string.dialog_notify_title)
        txtNotify.setText(R.string.dialog_notify_content_change_status)

        btnYes.setOnClickListener {
            changeAPIStatus()

            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    //Dialog tạo mới nguyên liệu
    private fun openDialogCreate() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_material)
        val window = dialog.window
        window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        idCategory = 0
        idUnits = 0
        idSpecification = 0

        val txtTitle: TechResTextView = dialog.findViewById(R.id.txtTitle)
        val spinnerCategory: Spinner = dialog.findViewById(R.id.spinnerCategory)
        val spinnerUnits: Spinner = dialog.findViewById(R.id.spinnerUnits)
        val spinnerSpecification: Spinner = dialog.findViewById(R.id.spinnerSpecification)
        val editTextNameMaterial: EditText =
                dialog.findViewById(R.id.editTextNameMaterial)
        val editTextCodeMaterial: EditText = dialog.findViewById(R.id.editTextCodeMaterial)
        val editTextCostPrice: CurrencyEditText = dialog.findViewById(R.id.editTextCostPrice)
        val editTextRetailPrice: CurrencyEditText = dialog.findViewById(R.id.editTextRetailPrice)
        val editTextWholesalePrice: CurrencyEditText = dialog.findViewById(R.id.editTextWholesalePrice)
        val editTextNumberWholesalePrice: EditText =
                dialog.findViewById(R.id.editTextNumberWholesalePrice)
        val editTextNumberOutOfStock: EditText = dialog.findViewById(R.id.editTextNumberOutOfStock)
        val editTextAmountOfLoss: EditText = dialog.findViewById(R.id.editTextAmountOfLoss)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val txtCategory: TechResTextView = dialog.findViewById(R.id.txtCategory)
        val txtUnits: TechResTextView = dialog.findViewById(R.id.txtUnits)
        val txtSpecification: TechResTextView = dialog.findViewById(R.id.txtSpecification)

        txtTitle.text = getString(R.string.txt_create_material)

        //Lấy danh sách DANH MỤC add vào spinner
        for (item in listDataCategory.indices) {
            spinnerCategory.adapter = ArrayAdapter(
                    mainActivity!!,
                    android.R.layout.simple_list_item_1,
                    listSpinnerCategoryString
            )
        }
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listDataCategory.indices) {
                    if (listSpinnerCategoryString[p2] == listDataCategory[i].name) {
                        idCategory = listDataCategory[i].id
                        txtCategory.text = listDataCategory[i].name
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        //Lấy danh sách ĐƠN VỊ add vào spinner
        for (item in listDataUnits.indices) {
            spinnerUnits.adapter = ArrayAdapter(
                    mainActivity!!,
                    android.R.layout.simple_list_item_1,
                    listSpinnerUnitsString
            )
        }
        spinnerUnits.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listDataUnits.indices) {
                    if (listSpinnerUnitsString[p2] == listDataUnits[i].name) {
                        idUnits = listDataUnits[i].id
                        txtUnits.text = listDataUnits[i].name
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        //Lấy danh sách QUY CÁCH add vào spinner
        for (item in listDataSpecification.indices) {
            spinnerSpecification.adapter = ArrayAdapter(
                    mainActivity!!,
                    android.R.layout.simple_list_item_1,
                    listSpinnerSpecificationString
            )
        }
        spinnerSpecification.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listDataSpecification.indices) {
                    if (listSpinnerSpecificationString[p2] == listDataSpecification[i].name) {
                        idSpecification = listDataSpecification[i].id
                        txtSpecification.text = listDataSpecification[i].name
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        btnYes.setOnClickListener {
            val name = editTextNameMaterial.text.toString()
            val code = editTextNameMaterial.text.toString()
            val costPrice = editTextCostPrice.text.toString()
            val retailPrice = editTextRetailPrice.text.toString()
            val wholesakePrice = editTextWholesalePrice.text.toString()
            val numberWholesakePrice = editTextNumberWholesalePrice.text.toString()
            val numberOutOfStock = editTextNumberOutOfStock.text.toString()
            val wastageRate = editTextAmountOfLoss.text.toString()
            when {
                name.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_name_material_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                code.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_code_material_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                costPrice.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_cost_price_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                retailPrice.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_retail_price_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                wholesakePrice.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_wholesake_price_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                numberWholesakePrice.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_number_wholesake_price_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                numberOutOfStock.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_number_out_of_stock_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                wastageRate.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_number_wastage_rate),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                else -> {
                    createAPIMaterial(
                            name.trim(),
                            code,
                            status,
                            idCategory,
                            idUnits,
                            idSpecification,
                            costPrice,
                            retailPrice,
                            wholesakePrice,
                            numberWholesakePrice,
                            numberOutOfStock,
                            wastageRate,
                            dialog
                    )
                }
            }
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    //interface của nút thay đổi trạng thái
    override fun onClick(position: Int, id: Int) {
        this.position = position
        idMaterial = id

        openDialogNotify()
    }

    //interface của nút chỉnh sửa nguyên liệu
    override fun onClick(position: Int, id: Int, model: MaterialManager) {
        this.position = position
        idMaterial = id
        val json = Gson().toJson(model)
        cacheManager.put(TechresEnum.DATA_MATERIAL.toString(), json.toString())
        cacheManager.put(TechresEnum.ID_MATERIAL.toString(), id.toString())
        val editMaterialFragment = EditMaterialFragment()
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.nav_host, EditMaterialFragment())
                            ?.addToBackStack(editMaterialFragment.tagName)
                            ?.commit()

                }, TechresEnum.TIME_100.toString().toLong()
        )
    }

    override fun onBackPress() {
        val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
        binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
        cacheManager.put(
                TechresEnum.KEY_BACK.toString(),
                TechresEnum.ACCOUNTMENU_ID_FRAGMENT.toString()
        )

        mainActivity!!.supportFragmentManager.popBackStack()

    }
}