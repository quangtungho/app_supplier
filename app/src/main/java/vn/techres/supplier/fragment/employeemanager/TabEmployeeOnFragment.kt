package vn.techres.supplier.fragment.employeemanager

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.employeeadapter.EmployeeOnAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentEmployeeOnandoffBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.ClickIntentProfile
import vn.techres.supplier.model.datamodel.DataListEmployee
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.ChangeResponse
import vn.techres.supplier.model.response.EmployeeResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.TechResTextView
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class TabEmployeeOnFragment :
        BaseBindingFragment<FragmentEmployeeOnandoffBinding>(FragmentEmployeeOnandoffBinding::inflate),
        ClickIntentProfile {
    val tagName: String = TabEmployeeOnFragment::class.java.name
    private var staffList = ArrayList<DataListEmployee>()
    private var adapter: EmployeeOnAdapter? = null
    private var informationStaffFragment = DetailEmployeeFragment()
    private var createEmployeeFragment = CreateEmployeeFragment()
    var position = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        binding!!.btnCreate.visibility = View.VISIBLE
        onSearch()
        binding!!.btnCreate.setOnClickListener { onCreateEmployee() }
    }

    //----------ONCLICK_____________//
    private fun onCreateEmployee() {
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.nav_host, CreateEmployeeFragment())
                            ?.addToBackStack(createEmployeeFragment.tagName)
                            ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
        )
    }

    private fun setAdapter() {
        adapter = mainActivity?.let { EmployeeOnAdapter(it) }
        staffList.removeAll(staffList.toSet())
        binding!!.rclEmployee.adapter = adapter
        binding!!.rclEmployee.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding!!.rclEmployee.setHasFixedSize(true)
        adapter?.setClickIntentProfile(this)
    }

    override fun onResume() {
        super.onResume()
        staffList.clear()
        listEmployee()

    }

    override fun onClick(position: Int, id: Int) {
        this.position = position
        cacheManager.put(TechresEnum.GET_BY_ID.toString(), id.toString())
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.nav_host, DetailEmployeeFragment())
                            ?.addToBackStack(informationStaffFragment.tagName)
                            ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
        )
    }

    override fun onCheck(position: Int, employee: DataListEmployee) {
        dialogChangeEmployee(employee.id!!)
    }

    override fun onBackPress() {
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    mainActivity!!.supportFragmentManager.popBackStack()
                }, TechresEnum.TIME_100.toString().toLong()
        )
        mainActivity!!.supportFragmentManager.popBackStack()
    }

    /**
     * Tim kiem danh sach nhan vien Supplier
     */
    private fun onSearch() {
        binding!!.seachStaff.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList: ArrayList<DataListEmployee> = ArrayList()
                for (key in staffList) {
                    if (staffList.size > 0) {
                        val name: String = key.name!!
                        if (name.lowercase(Locale.getDefault())
                                        .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapter!!.setDataSourceOn(newList)
                binding!!.rclEmployee.setHasFixedSize(true)
                binding!!.rclEmployee.adapter = adapter
                return false
            }
        })
    }
//__________________API_____________________//
    /**
     *  goi API doi trang thai nhan vien supplier */
    private fun changeEmployee(id: Int) {
        val params = BaseParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees/$id/change-status"
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getChangeEmployee(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ChangeResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: ChangeResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            staffList.clear()
                            listEmployee()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    /**
     * goi API danh sach nhan vien supplier
     */
    private fun listEmployee() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees?status=${TechresEnum.EMPLOYEE_ON_MANAGER}"
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getEmployee(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<EmployeeResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onNext(response: EmployeeResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            staffList = response.data

                            if (staffList.size == 0) {
                                binding!!.rclEmployee.visibility = View.GONE
                                binding!!.linearDataNull.visibility = View.VISIBLE
                            } else {
                                binding!!.rclEmployee.visibility = View.VISIBLE
                                binding!!.linearDataNull.visibility = View.GONE
                            }
                            adapter!!.setDataSourceOn(staffList)
                            binding!!.txtMember.text = staffList.size.toString()

                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })

    }

    //_____________DIALOG______________//
    private fun dialogChangeEmployee(id: Int) {
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
        txtNotify.text = getString(R.string.dialog_notify_content_change_status)
        btnYes.setOnClickListener {
            changeEmployee(id)
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }
}