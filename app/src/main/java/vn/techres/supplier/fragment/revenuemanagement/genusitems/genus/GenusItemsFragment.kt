package vn.techres.supplier.fragment.revenuemanagement.genusitems.genus

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.revenueadapter.GenusItemsAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentGenusItemsBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.interfaces.ClickGensItemsID
import vn.techres.supplier.model.datamodel.DataGenusItems
import vn.techres.supplier.model.datamodel.DataListReason
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.CreateGenusItemsParams
import vn.techres.supplier.model.params.EditGenusItemsParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.CreateGenusItemsResponse
import vn.techres.supplier.model.response.GenusItemsResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.TechResTextView
import java.util.*
import java.util.stream.Collectors

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class GenusItemsFragment :
    BaseBindingFragment<FragmentGenusItemsBinding>(FragmentGenusItemsBinding::inflate),
    ClickGensItemsID {
    val tagName: String = GenusItemsFragment::class.java.name
    private var adapter: GenusItemsAdapter? = null
    var listData = ArrayList<DataGenusItems>()
    var listReason = ArrayList<DataListReason>()
    var position = 0
    var idReasonExchange = 0
    private var idGenus: Int = 0
    var items = arrayOf(0, 1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        setAdapter()
        //onCreate
        fragmentCreate()
        searchGenusItems()
        binding!!.btnCreate.setOnClickListener {
            openDialogCreate()
        }
    }
    override fun onResume() {
        super.onResume()
        listData.clear()
        getAPIGenusItems()
        listReason.clear()
    }
    private fun fragmentCreate() {
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        mainActivity!!.setHeader(getString(R.string.genus_items))
        mainActivity!!.setBackClick(true)
        binding!!.txtTotalStatusSpecification.text = getString(R.string.total_genus_items)
    }
    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = GenusItemsAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)
        adapter!!.setClickRecyclerDelete(this)
        adapter!!.setClickRecyclerEdit(this)
    }
    private fun searchGenusItems() {
        binding!!.txtSearchHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList: ArrayList<DataGenusItems> = ArrayList()
                for (key in listData) {
                    if (listData.size > 0) {
                        val name: String = key.name
                        if (name.lowercase(Locale.getDefault())
                                .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapter!!.setDataListGenusItems(newList)
                binding!!.recyclerView.setHasFixedSize(true)
                binding!!.recyclerView.adapter = adapter
                return false
            }
        })
    }
    //-----------------GOI API_____________________//
    private fun getAPIGenusItems() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-addition-fee-reasons"
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListGenusItems(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<GenusItemsResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: GenusItemsResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listData = response.data
                        listData = listData.stream()
                            .filter { x -> x.is_hidden != 1 }
                            .collect(Collectors.toList()) as ArrayList<DataGenusItems>
                        binding!!.txtTotal.text = listData.size.toString()
                        adapter?.setDataListGenusItems(listData)
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    //API delete GenusItems
    private fun changeAPIChange() {
        val params = BaseParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-addition-fee-reasons/$idGenus/change-hidden"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getDelete(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: BaseResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listData.removeAt(position)
                        adapter!!.notifyDataSetChanged()
                        val snack = Snackbar.make(
                            requireView(),
                            getString(R.string.toast_change_success),
                            Snackbar.LENGTH_LONG
                        )
                        snack.show()
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    //API create new
    private fun createGenusItems(
        name: String,
        description: String,
        id: Int,
        dialog: Dialog
    ) {
        val params = CreateGenusItemsParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-addition-fee-reasons/create"
        params.params.name = name.trim()
        params.params.description = description
        params.params.supplier_addition_fee_type = id

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .createGensItems(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CreateGenusItemsResponse> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                }

                override fun onNext(response: CreateGenusItemsResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listData.clear()
                        getAPIGenusItems()
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
            })
    }
    private fun editGenusItems(name: String, description: String, dialog: Dialog) {
        val params = EditGenusItemsParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-addition-fee-reasons/${idGenus}/update"
        params.params.name = name.trim()
        params.params.description = description
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getEditGenusItems(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CreateGenusItemsResponse> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: CreateGenusItemsResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        val snack = Snackbar.make(
                            requireView(),
                            getString(R.string.toast_edit_success),
                            Snackbar.LENGTH_LONG
                        )
                        snack.show()
                        listData.clear()
                        getAPIGenusItems()
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {}
            })
    }
    //-------------------DIALOG________________//
    //Dialog create
    private fun openDialogCreate() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_genus_items)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val spinnerData: Spinner = dialog.findViewById(R.id.spinnerData)
        val editName: EditText =
            dialog.findViewById(R.id.editName)
        val editDescription: EditText =
            dialog.findViewById(R.id.editDescription)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val txtGenusItems: TechResTextView = dialog.findViewById(R.id.txtGenusItems)
        idReasonExchange = 0
        val options = arrayOf(
            getString(R.string.receipts_items),
            getString(R.string.check_items),
        )
        spinnerData.adapter = ArrayAdapter(
            mainActivity!!,
            android.R.layout.simple_list_item_1,
            options
        )
        spinnerData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in items.indices) {
                    if (items[p2] == 0) {
                        idReasonExchange = 0
                        txtGenusItems.text = getString(R.string.receipts_items)
                    } else {
                        idReasonExchange = 1
                        txtGenusItems.text = getString(R.string.check_items)
                    }
                    break
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        btnYes.setOnClickListener {
            val name = editName.text.toString()
            val description = editDescription.text.toString()
            when {
                name.isEmpty() -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_name_genusitems_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                description.isEmpty() -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_description_genusitems_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                txtGenusItems.text == getString(R.string.txt_please_choose) -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_genusitems_exchange_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                else -> {
                    createGenusItems(name, description, idReasonExchange, dialog)
                    dialog.dismiss()
                }
            }
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    //Dialog edit
    private fun openDialogEdit(position: Int) {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_genus_items)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val txtTitle: TechResTextView = dialog.findViewById(R.id.txtTitle)
        val editName: EditText = dialog.findViewById(R.id.editName)
        val editDescription: EditText = dialog.findViewById(R.id.editDescription)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val txtGenusItems: TechResTextView = dialog.findViewById(R.id.txtGenusItems)
        val spinnerData: Spinner = dialog.findViewById(R.id.spinnerData)
        txtTitle.text = getString(R.string.edit_genus_item)
        editName.setText(listData[position].name)
        editDescription.setText(listData[position].description)
        if (listData[position].supplier_addition_fee_type == 1)
            txtGenusItems.text = getString(R.string.check_items)
        else
            txtGenusItems.text = getString(R.string.receipts_items)
        btnYes.setOnClickListener {
            val name = editName.text.toString()
            val description = editDescription.text.toString()
            when {
                name.isEmpty() -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_name_genusitems_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                description.isEmpty() -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_description_genusitems_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    editGenusItems(name, description, dialog)
                    dialog.dismiss()
                }
            }
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    //Dialog notify
    private fun openDialogNotifyChange() {
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
        txtNotify.setText(R.string.dialog_notify_content)

        btnYes.setOnClickListener {
            changeAPIChange()
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
    override fun clickDelete(position: Int, id: Int) {
        this.position = position
        this.idGenus = id
        openDialogNotifyChange()
    }
    override fun clickEdit(position: Int, id: Int, model: DataGenusItems) {
        this.position = position
        this.idGenus = id
        openDialogEdit(position)
    }
}
