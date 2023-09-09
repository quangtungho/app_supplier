package vn.techres.supplier.fragment.units

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.unitsadapter.SpinnerSpecificationInUnitsAdapter
import vn.techres.supplier.adapter.unitsadapter.UnitsActiveAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentUnitsActiceAndPauseBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.interfaces.OnClickRefreshRecyclerView
import vn.techres.supplier.interfaces.OnClickTransID
import vn.techres.supplier.interfaces.OnClickTransIdAndModelUnits
import vn.techres.supplier.model.datamodel.DataSpecification
import vn.techres.supplier.model.datamodel.DataUnitSpecification
import vn.techres.supplier.model.datamodel.DataUnits
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.CreateUnitsParams
import vn.techres.supplier.model.params.UpdateUnitsParams
import vn.techres.supplier.model.response.ChangeStatusUnitsResponse
import vn.techres.supplier.model.response.CreateAndUpdateUnitsResponse
import vn.techres.supplier.model.response.SpecificationResponse
import vn.techres.supplier.model.response.UnitsResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.TechResTextView
import java.util.*
import java.util.stream.Collectors

class UnitsActiveFragment :
    BaseBindingFragment<FragmentUnitsActiceAndPauseBinding>(FragmentUnitsActiceAndPauseBinding::inflate),
    OnClickTransID, OnClickTransIdAndModelUnits,
    OnClickRefreshRecyclerView {

    private var adapter: UnitsActiveAdapter? = null
    private var adapterSpinner: SpinnerSpecificationInUnitsAdapter? = null
    var listData: ArrayList<DataUnits> = ArrayList()
    var listSpecification: ArrayList<DataSpecification> = ArrayList()
    val listSpinnerString: ArrayList<String> = ArrayList()
    var listSpinnerData: ArrayList<String> = ArrayList()
    var position = 0
    private var positionItemDialog = 0
    private var idUnits: Int = 0
    var idSpecification = ArrayList<Int>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        val animScaleShowTopToBottom =
            AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerView.startAnimation(animScaleShowTopToBottom)

        setAdapter()
        fragmentCreate()
        searchUnits()
        binding!!.btnCreate.setOnClickListener {
            openDialogCreate()
        }
    }

    override fun onResume() {
        super.onResume()
        listData.clear()
        getAPIUnits()
        listSpecification.clear()
        getAPISpecification()
    }

    private fun fragmentCreate() {
        binding!!.txtTotalStatusSpecification.text = getString(R.string.txt_total_units_active)
    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = UnitsActiveAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)

        adapter!!.setClickRecyclerDelete(this)
        adapter!!.setClickRecyclerEdit(this)
    }

    private fun searchUnits() {
        binding!!.btnSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList: ArrayList<DataUnits> = ArrayList()
                for (key in listData) {
                    if (listData.size > 0) {
                        val name: String = key.name
                        if (name.lowercase(Locale.getDefault())
                                .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapter!!.setDataListUnits(newList)
                binding!!.recyclerView.setHasFixedSize(true)
                binding!!.recyclerView.adapter = adapter
                return false
            }
        })
    }

    //API get list units
    private fun getAPIUnits() {
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

                @SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: UnitsResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        for (item in response.data.indices) {
                            if (response.data[item].status == 1) {
                                listData.add(response.data[item])
                            }
                        }
                        adapter!!.setDataListUnits(listData)
                        adapter!!.notifyDataSetChanged()
                        binding!!.txtTotalSpecification.text = listData.size.toString()
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
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
                override fun onNext(response: SpecificationResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        for (item in response.data!!.indices) {
                            if (response.data!![item].status == 1) {
                                listSpecification = response.data!!
                                listSpinnerString.add(response.data!![item].name)
                            }
                        }
                    } else
                        Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                            .show()
                }
            })
    }

    //API change status units
    private fun changeAPIStatusUnits() {
        val params = BaseParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/material-units/$idUnits/change-status"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .changeStatusUnits(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ChangeStatusUnitsResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: ChangeStatusUnitsResponse) {
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
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    //API create new units
    private fun createUnits(name: String, description: String, id: ArrayList<Int>, dialog: Dialog) {
        val params = CreateUnitsParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/material-units/create"
        params.params.name = name.trim()
        params.params.description = description
        params.params.unit_specifications = id
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .createUnits(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CreateAndUpdateUnitsResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(response: CreateAndUpdateUnitsResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listData.clear()
                        getAPIUnits()
                        dialog.dismiss()
                        val snack = Snackbar.make(
                            requireView(),
                            getString(R.string.toast_create_success),
                            Snackbar.LENGTH_LONG
                        )
                        snack.show()

                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {}

            })
    }

    //API edit units
    private fun editUnits(name: String, description: String, id: ArrayList<Int>, dialog: Dialog) {
        val params = UpdateUnitsParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/material-units/$idUnits"

        params.params.name = name
        params.params.description = description
        params.params.specificationIds = id
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .updateUnits(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CreateAndUpdateUnitsResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(response: CreateAndUpdateUnitsResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listData.clear()
                        getAPIUnits()
                        dialog.dismiss()
                        val snack = Snackbar.make(
                            requireView(),
                            getString(R.string.toast_edit_success),
                            Snackbar.LENGTH_LONG
                        )
                        snack.show()

                    }else {
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
            changeAPIStatusUnits()
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    //Dialog edit units
    private fun openDialogEdit(
        position: Int,
        model: ArrayList<DataUnitSpecification>
    ) {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_units)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val spinnerData: Spinner = dialog.findViewById(R.id.spinnerData)
        val txtTitle: TechResTextView = dialog.findViewById(R.id.txtTitle)
        val editTextNameUnits: EditText = dialog.findViewById(R.id.editTextNameUnits)
        val editTextDescribe: EditText = dialog.findViewById(R.id.editTextDescribe)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val recyclerViewSpinner: RecyclerView = dialog.findViewById(R.id.recyclerViewSpinner)
        val txtListSpecification: TechResTextView = dialog.findViewById(R.id.txtListSpecification)

        recyclerViewSpinner.layoutManager =
            GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        adapterSpinner = SpinnerSpecificationInUnitsAdapter(mainActivity!!.baseContext)
        recyclerViewSpinner.adapter = adapterSpinner
        recyclerViewSpinner.setHasFixedSize(true)
        adapterSpinner!!.setClickRecycler(this)

        listSpinnerData.clear()
        idSpecification.clear()

        txtTitle.setText(R.string.txt_edit_units)
        editTextNameUnits.setText(listData[position].name)
        editTextDescribe.setText(listData[position].description)

        idSpecification =
            model.stream().map { x -> x.id }.collect(Collectors.toList()) as ArrayList<Int>
        listSpinnerData =
            model.stream().map { x -> x.name }.collect(Collectors.toList()) as ArrayList<String>

        adapterSpinner!!.setDataList(listSpinnerData)

        for (item in listSpecification.indices) {
            spinnerData.adapter = ArrayAdapter(
                mainActivity!!,
                android.R.layout.simple_list_item_1,
                listSpinnerString
            )
        }

        spinnerData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    txtListSpecification.text = ""
                    for (i in listSpecification.indices) {
                        if (listSpinnerString[p2] == listSpecification[i].name) {
                            idSpecification.add(listSpecification[i].id)
                            listSpinnerData.add(listSpecification[i].name)
                            adapterSpinner!!.setDataList(listSpinnerData)
                            break
                        }
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btnYes.setOnClickListener {
            val name = editTextNameUnits.text.toString()
            val description = editTextDescribe.text.toString()
            when {
                name.isEmpty() -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_name_units_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                description.isEmpty() -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_description_units_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                listSpinnerData.isEmpty() -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_specification_units_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    editUnits(name, description, idSpecification, dialog)
                }
            }
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    //Dialog create units
    private fun openDialogCreate() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_units)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        listSpinnerData.clear()
        idSpecification.clear()

        val spinnerData: Spinner = dialog.findViewById(R.id.spinnerData)
        val editTextNameUnits: EditText = dialog.findViewById(R.id.editTextNameUnits)
        val editTextDescribe: EditText = dialog.findViewById(R.id.editTextDescribe)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val recyclerViewSpinner: RecyclerView = dialog.findViewById(R.id.recyclerViewSpinner)

        recyclerViewSpinner.layoutManager =
            GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        adapterSpinner = SpinnerSpecificationInUnitsAdapter(mainActivity!!.baseContext)
        recyclerViewSpinner.adapter = adapterSpinner
        recyclerViewSpinner.setHasFixedSize(true)
        adapterSpinner!!.setClickRecycler(this)

        for (item in listSpecification.indices) {
            spinnerData.adapter = ArrayAdapter(
                mainActivity!!,
                android.R.layout.simple_list_item_1,
                listSpinnerString
            )
        }

        spinnerData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listSpecification.indices) {
                    if (listSpinnerString[p2] == listSpecification[i].name) {
                        idSpecification.add(listSpecification[i].id)
                        listSpinnerData.add(listSpecification[i].name)
                        adapterSpinner!!.setDataList(listSpinnerData)
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        btnYes.setOnClickListener {
            val name = editTextNameUnits.text.toString()
            val description = editTextDescribe.text.toString()

            when {
                name.isEmpty() -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_name_units_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                description.isEmpty() -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_description_units_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                listSpinnerData.isEmpty() -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_specification_units_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    createUnits(name, description, idSpecification, dialog)
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
        this.idUnits = id

        openDialogNotify()
    }

    override fun onClick(
        position: Int,
        id: Int,
        model: ArrayList<DataUnitSpecification>
    ) {
        this.position = position
        this.idUnits = id

        openDialogEdit(position, model)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(position: Int) {
        this.positionItemDialog = position
        listSpinnerData.removeAt(positionItemDialog)
        idSpecification.removeAt(positionItemDialog)
        adapterSpinner!!.notifyDataSetChanged()
    }

    override fun onBackPress() {
        val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
        binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}