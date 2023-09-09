package vn.techres.supplier.fragment.specification

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.specificationadapter.SpecificationActiveAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentSpecificationActiceAndPauseBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.Currency
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.interfaces.OnClickTransIdAndModelSpecification
import vn.techres.supplier.model.datamodel.DataSpecification
import vn.techres.supplier.model.datamodel.DataUnitsExchange
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.CreateAndUpdateSpecificationParams
import vn.techres.supplier.model.response.ChangeStatusSpecificationResponse
import vn.techres.supplier.model.response.CreateAndUpdateSpecificationResponse
import vn.techres.supplier.model.response.SpecificationResponse
import vn.techres.supplier.model.response.UnitsExchangeResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.TechResTextView
import java.util.*
import java.util.stream.Collectors

class SpecificationActiveFragment : BaseBindingFragment<FragmentSpecificationActiceAndPauseBinding>(
        FragmentSpecificationActiceAndPauseBinding::inflate
), OnClickTransID,
        OnClickTransIdAndModelSpecification {
    private var adapter: SpecificationActiveAdapter? = null
    val listData: ArrayList<DataSpecification> = ArrayList()
    var listUnitExchange: ArrayList<DataUnitsExchange> = ArrayList()
    var listSpinnerString: ArrayList<String> = ArrayList()
    var position = 0
    private var idSpecification = 0
    var idUnitsExchange = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()

        //onCreate
        fragmentCreate()
        searchSpecificationActive()
        binding!!.btnCreate.setOnClickListener {
            openDialogCreate()
        }
    }

    override fun onResume() {
        super.onResume()
        listData.clear()
        getAPISpecification()
        listUnitExchange.clear()
        getAPIUnitsExchange()
    }

    private fun fragmentCreate() {
        binding!!.txtTotalStatusSpecification.text = getString(R.string.txt_total_exchange_active)
    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = SpecificationActiveAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)

        adapter!!.setClickRecyclerDelete(this)
        adapter!!.setClickRecyclerEdit(this)
    }

    private fun searchSpecificationActive() {
        binding!!.txtSearchHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList: ArrayList<DataSpecification> = ArrayList()
                for (key in listData) {
                    if (listData.size > 0) {
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

    //API get list specification
    private fun getAPISpecification() {
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

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onNext(response: SpecificationResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            for (item in response.data!!.indices) {
                                if (response.data!![item].status == 1) {
                                    listData.add(response.data!![item])
                                }
                            }
                            adapter!!.setDataList(listData)
                            adapter!!.notifyDataSetChanged()
                            binding!!.txtTotalSpecification.text = listData.size.toString()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    //API get list units exchange
    private fun getAPIUnitsExchange() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/material-unit-specification-exchange-name"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListUnitsExchange(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UnitsExchangeResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: UnitsExchangeResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listUnitExchange = response.data!!
                            for (item in response.data!!.indices) {
                                listSpinnerString.add(response.data!![item].name)
                            }
                        } else
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                    }
                })
    }

    //API change status specification
    private fun changeAPIStatusSpecification() {
        val params = BaseParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
                "/api/supplier-material-unit-specifications/$idSpecification/change-status"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .changeStatusSpecification(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ChangeStatusSpecificationResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onNext(response: ChangeStatusSpecificationResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listData.removeAt(position)
                            adapter!!.notifyDataSetChanged()
                            binding!!.txtTotalSpecification.text = listData.size.toString()
                            val snack = Snackbar.make(
                                    requireView(),
                                    getString(R.string.toast_change_status_success),
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    //API create new specification
    private fun createSpecification(name: String, valueExchange: String, id: Int, dialog: Dialog) {
        val params = CreateAndUpdateSpecificationParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-material-unit-specifications/create"

        params.params.name = name
        params.params.exchange_value = AppUtils.trimCommaOfString(valueExchange).toFloat()
        params.params.material_unit_specification_exchange_name_id = id
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .createAndUpdateSpecification(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CreateAndUpdateSpecificationResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: CreateAndUpdateSpecificationResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listData.clear()
                            getAPISpecification()
                            dialog.dismiss()
                            val snack = Snackbar.make(
                                    requireView(),
                                    getString(R.string.toast_create_success),
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

    //API edit specification
    private fun editSpecification(
            name: String,
            valueExchange: String,
            id: Int,
            dialog: Dialog,
    ) {
        val params = CreateAndUpdateSpecificationParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-material-unit-specifications/$idSpecification"

        params.params.name = name
        params.params.exchange_value = AppUtils.trimCommaOfString(valueExchange).toFloat()
        params.params.material_unit_specification_exchange_name_id = id
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .createAndUpdateSpecification(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CreateAndUpdateSpecificationResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: CreateAndUpdateSpecificationResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {

                            val snack = Snackbar.make(
                                    requireView(),
                                    getString(R.string.toast_edit_success),
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()
                            listData.clear()
                            getAPISpecification()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {}

                })
    }

    //Dialog notify
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
            changeAPIStatusSpecification()

            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    //Dialog create specification
    private fun openDialogCreate() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_specification)
        val window = dialog.window
        window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        idUnitsExchange = 0
        val spinnerData: Spinner = dialog.findViewById(R.id.spinnerData)
        val editTextNameSpecification: EditText =
                dialog.findViewById(R.id.editTextNameSpecification)
        val editTextValueExchange: EditText = dialog.findViewById(R.id.editTextValueExchange)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val txtUnitsExchange: TechResTextView = dialog.findViewById(R.id.txtRolesExchange)

        for (item in listUnitExchange.indices) {
            spinnerData.adapter = ArrayAdapter(
                    mainActivity!!,
                    android.R.layout.simple_list_item_1,
                    listSpinnerString
            )
        }
        spinnerData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listUnitExchange.indices) {
                    if (listSpinnerString[p2] == listUnitExchange[i].name) {
                        idUnitsExchange = listUnitExchange[i].id
                        txtUnitsExchange.text = listUnitExchange[i].name
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        btnYes.setOnClickListener {
            val name = editTextNameSpecification.text.toString()
            val valueExchange = editTextValueExchange.text.toString()
            when {
                name.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_name_specification_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                valueExchange.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_value_exchange_specification_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                txtUnitsExchange.text == getString(R.string.txt_please_choose) -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_units_exchange_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                else -> {
                    createSpecification(
                            name,
                            valueExchange, idUnitsExchange, dialog
                    )
                }
            }
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    //Dialog edit specification
    private fun openDialogEdit(model: DataSpecification) {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_specification)
        val window = dialog.window
        window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtTitle: TechResTextView = dialog.findViewById(R.id.txtTitle)
        val spinnerData: Spinner = dialog.findViewById(R.id.spinnerData)
        val editTextNameSpecification: EditText =
                dialog.findViewById(R.id.editTextNameSpecification)
        val editTextValueExchange: EditText = dialog.findViewById(R.id.editTextValueExchange)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val txtUnitsExchange: TechResTextView = dialog.findViewById(R.id.txtRolesExchange)

        idUnitsExchange = 0

        txtTitle.setText(R.string.txt_edit_specification)
        editTextNameSpecification.setText(model.name)
        editTextValueExchange.setText(Currency.formatCurrencyDecimal(model.exchange_value.toFloat()))

        listSpinnerString = listSpinnerString.stream()
                .filter { x -> !x.equals(model.material_unit_specification_exchange_name) }
                .collect(Collectors.toList()) as ArrayList<String>
        val swap = listUnitExchange.stream()
                .filter { x -> x.name.equals(model.material_unit_specification_exchange_name) }
                .collect(Collectors.toList()) as ArrayList<DataUnitsExchange>
        listUnitExchange = listUnitExchange.stream()
                .filter { x -> !x.name.equals(model.material_unit_specification_exchange_name) }
                .collect(Collectors.toList()) as ArrayList<DataUnitsExchange>
        listSpinnerString.add(0, model.material_unit_specification_exchange_name)
        listUnitExchange.add(0, swap[0])
        spinnerData.adapter = ArrayAdapter(
                mainActivity!!,
                android.R.layout.simple_list_item_1,
                listSpinnerString
        )
        spinnerData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listUnitExchange.indices) {
                    if (listSpinnerString[p2] == listUnitExchange[i].name) {
                        idUnitsExchange = listUnitExchange[i].id
                        txtUnitsExchange.text = listUnitExchange[i].name
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btnYes.setOnClickListener {
            val name = editTextNameSpecification.text.toString()
            val valueExchange = editTextValueExchange.text.toString()

            when {
                name.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_name_specification_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                valueExchange.isEmpty() -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_value_exchange_specification_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                txtUnitsExchange.text == getString(R.string.txt_please_choose) -> {
                    Toast.makeText(
                            context,
                            getString(R.string.toast_units_exchange_empty),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
                else -> {
                    editSpecification(
                            name,
                            valueExchange, idUnitsExchange, dialog
                    )
                }
            }
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onClick(position: Int, id: Int) {
        this.position = position
        this.idSpecification = id
        openDialogNotify()
    }

    override fun onClick(position: Int, id: Int, model: DataSpecification) {
        this.position = position
        this.idSpecification = id

        openDialogEdit(model)
    }

    override fun onBackPress() {
        val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
        binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}