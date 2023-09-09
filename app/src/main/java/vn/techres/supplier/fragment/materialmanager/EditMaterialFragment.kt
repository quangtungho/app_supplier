package vn.techres.supplier.fragment.materialmanager

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentEditMaterialBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataCategory
import vn.techres.supplier.model.datamodel.DataUnits
import vn.techres.supplier.model.datamodel.MaterialManager
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.CreateMaterialParams
import vn.techres.supplier.model.response.CategoryActiveResponse
import vn.techres.supplier.model.response.CreateMaterialResponse
import vn.techres.supplier.model.response.UnitsResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.DecimalFormat

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */

class EditMaterialFragment :
        BaseBindingFragment<FragmentEditMaterialBinding>(FragmentEditMaterialBinding::inflate) {
    val tagName: String = EditMaterialFragment::class.java.name
    private var idCategory = 0
    private var idUnits = 0
    private var idSpecification = 0
    private var dataMaterial = MaterialManager()

    private val listDataMaterial = ArrayList<MaterialManager>()
    private var listDataCategory = ArrayList<DataCategory>()
    private var listDataUnits = ArrayList<DataUnits>()
    private var listDataUnitsSpecification = ArrayList<DataUnits>()

    private var listSpinnerCategoryString = ArrayList<String>()
    private var listSpinnerUnitsString = ArrayList<String>()
    private var listSpinnerSpecificationString = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constraintLayoutMain.startAnimation(animFragment)
        mainActivity!!.setHeader(getString(R.string.txt_header_material))
        getAPIListCategoryActive()
        getAPIListUnits()

        dataMaterial = Gson().fromJson(
                cacheManager.get(TechresEnum.DATA_MATERIAL.toString()),
                MaterialManager::class.java
        )

        dataMaterial()

        binding!!.btnEdit.setOnClickListener {
            binding!!.btnEdit.isEnabled = false
            Handler().postDelayed({ binding!!.btnEdit.isEnabled = true }, 5000)
            binding!!.btnEdit.showLoading()
            createAPIMaterial(
                    binding!!.editTextNameMaterial.text.toString(),
                    binding!!.editTextNameMaterial.text.toString(),
                    idCategory,
                    idUnits,
                    idSpecification,
                    binding!!.editTextCostPrice.text.toString(),
                    binding!!.editTextRetailPrice.text.toString(),
                    binding!!.editTextWholesalePrice.text.toString(),
                    binding!!.editTextNumberWholesalePrice.text.toString(),
                    binding!!.editTextNumberOutOfStock.text.toString(),
                    binding!!.editTextAmountOfLoss.text.toString()

            )

        }


    }

    private fun dataSpinnerCategory() {

        var dataCategory = DataCategory()

        for (i in listDataCategory.indices) {
            if (listDataCategory[i].name == dataMaterial.material_category_name) {
                dataCategory = listDataCategory[i]
                listDataCategory.remove(dataCategory)
                break
            }
        }
        listDataCategory.add(0, dataCategory)

        for (i in listDataCategory.indices) {
            listSpinnerCategoryString.add(listDataCategory[i].name)
        }

        //Lấy danh sách DANH MỤC add vào spinner
        binding!!.spinnerCategory.adapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                listSpinnerCategoryString
        )

        binding!!.spinnerCategory.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        for (i in listDataCategory.indices) {
                            if (listSpinnerCategoryString[p2] == listDataCategory[i].name) {
                                idCategory = listDataCategory[i].id
                                binding!!.txtCategory.text = listDataCategory[i].name
                                break
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
    }

    private fun dataSpinnerUnits() {//Don vi
        var dataUnits = DataUnits()
        /**
         * listDataUnits
         */
        for (i in listDataUnits.indices) {
            if (listDataUnits[i].name == dataMaterial.material_unit_name) {
                dataUnits = listDataUnits[i]
                listDataUnits.remove(dataUnits)
                break
            }
        }
        listDataUnits.add(0, dataUnits)
        listDataUnitsSpecification.add(0, dataUnits)

        for (i in listDataUnits.indices) {
            listSpinnerUnitsString.add(listDataUnits[i].name)
        }
        binding!!.spinnerUnits.adapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                listSpinnerUnitsString
        )

        binding!!.spinnerUnits.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        for (i in listDataUnits.indices) {
                            if (listSpinnerUnitsString[p2] == listDataUnits[i].name) {
                                idUnits = listDataUnits[i].id
                                binding!!.txtUnits.text = listDataUnits[i].name
                                if (listDataUnitsSpecification[0].id == idUnits) {
                                    listSpinnerSpecificationString = ArrayList()
                                    for (i in listDataUnitsSpecification[0].material_unit_specification.indices) {
                                        listSpinnerSpecificationString.add(listDataUnitsSpecification[0].material_unit_specification[i].name)
                                    }
                                    binding!!.spinnerSpecification.adapter = ArrayAdapter(
                                            mainActivity!!,
                                            android.R.layout.simple_list_item_1,
                                            listSpinnerSpecificationString
                                    )
                                } else {
                                    listSpinnerSpecificationString.clear()
                                    listDataUnitsSpecification.clear()
                                    listDataUnitsSpecification.add(listDataUnits[i])

                                    for (i in listDataUnitsSpecification[0].material_unit_specification.indices) {
                                        listSpinnerSpecificationString.add(listDataUnitsSpecification[0].material_unit_specification[i].name)
                                    }

                                    binding!!.spinnerSpecification.adapter = ArrayAdapter(
                                            mainActivity!!,
                                            android.R.layout.simple_list_item_1,
                                            listSpinnerSpecificationString
                                    )
                                }
                                break
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }

        /**
         * Lấy danh sách Quy Cách add vào spinner
         */
        var dataUnitsQC = DataUnits()
        for (i in listDataUnitsSpecification.indices) {
            if (listDataUnitsSpecification[i].name == dataMaterial.material_unit_specification_name) {
                dataUnitsQC = listDataUnitsSpecification[i]
                listDataUnitsSpecification.removeAt(i)
            }
        }
        listDataUnitsSpecification.add(0, dataUnitsQC)

        listSpinnerSpecificationString = ArrayList()
        for (i in listDataUnitsSpecification.indices) {
            listSpinnerSpecificationString.add(listDataUnitsSpecification[i].name)
        }

        binding!!.spinnerSpecification.adapter = ArrayAdapter(
                mainActivity!!,
                android.R.layout.simple_list_item_1,
                listSpinnerSpecificationString
        )
        binding!!.spinnerSpecification.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        for (i in listDataUnitsSpecification[0].material_unit_specification.indices) {
                            if (listSpinnerSpecificationString[p2] == listDataUnitsSpecification[0].material_unit_specification[i].name) {
                                idSpecification =
                                        listDataUnitsSpecification[0].material_unit_specification[i].id
                                binding!!.txtSpecification.text = listSpinnerSpecificationString[p2]
                                break
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }


    }

    private fun dataMaterial() {
        binding!!.editTextNameMaterial.setText(dataMaterial.name)
        binding!!.editTextAmountOfLoss.setText(formatFloatToString(dataMaterial.wastage_rate))
        binding!!.editTextCodeMaterial.setText(dataMaterial.code)
        binding!!.editTextCostPrice.setText(currencyFormat(dataMaterial.price.toString()))
        binding!!.editTextRetailPrice.setText(currencyFormat(dataMaterial.retail_price.toString()))
        binding!!.editTextWholesalePrice.setText(currencyFormat(dataMaterial.wholesale_price.toString()))
        binding!!.editTextNumberWholesalePrice.setText(formatFloatToString(dataMaterial.wholesale_price_quantity))
        binding!!.editTextNumberOutOfStock.setText(formatFloatToString(dataMaterial.out_stock_alert_quantity))
        binding!!.txtCategory.text = dataMaterial.material_category_name
        binding!!.txtUnits.text = dataMaterial.material_unit_name
        binding!!.txtSpecification.text = dataMaterial.material_unit_specification_name

    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    private fun formatFloatToString(f: Float): String {
        val i = f.toInt()
        return if (f == i.toFloat()) i.toString() else f.toString()
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
                            }
                            dataSpinnerCategory()
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
                            listDataUnits = ArrayList()
                            listDataUnitsSpecification = ArrayList()
                            for (i in response.data.indices) {
                                if (response.data[i].status == 1) {
                                    listDataUnits.add(response.data[i])
                                }
                            }
                            dataSpinnerUnits()
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

    //API chỉnh sửa nguyên liệu
    private fun createAPIMaterial(
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
            wastageRate: String
    ) {
        val params = CreateMaterialParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/materials/${dataMaterial.id}/update"

        params.params.name = name.trim()
        params.params.code = code
        params.params.material_category_id = idCategory
        params.params.material_unit_id = idUnits
        params.params.material_unit_specification_id = idSpecification
        params.params.cost_price = AppUtils.trimCommaOfString(costPrice).toFloat()
        params.params.retail_price = AppUtils.trimCommaOfString(retailPrice).toFloat()
        params.params.wholesale_price = AppUtils.trimCommaOfString(wholesakePrice).toFloat()
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
                            binding!!.btnEdit.hideLoading()
                            mainActivity!!.supportFragmentManager.popBackStack()
                            val snack = Snackbar.make(
                                    binding!!.constraintLayoutMain,
                                    getString(R.string.toast_edit_success),
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()
                        } else {
                            binding!!.btnEdit.hideLoading()
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

    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}