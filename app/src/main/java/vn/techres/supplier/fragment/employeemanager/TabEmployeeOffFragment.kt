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
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.employeeadapter.EmployeeOffAdapter
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
class TabEmployeeOffFragment :
    BaseBindingFragment<FragmentEmployeeOnandoffBinding>(FragmentEmployeeOnandoffBinding::inflate),
    ClickIntentProfile {
    val tagName: String = TabEmployeeOffFragment::class.java.name
    private var staffList = ArrayList<DataListEmployee>()
    private var adapter: EmployeeOffAdapter? = null
    private var informationStaffFragment = DetailEmployeeFragment()
    var position = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        setAdapter()
        binding!!.btnCreate.visibility = View.GONE
        onSearch()
    }
    private fun setAdapter() {
        adapter = mainActivity?.let { EmployeeOffAdapter(it) }
        staffList.removeAll(staffList.toSet())
        binding!!.rclEmployee.adapter = adapter
        binding!!.rclEmployee.layoutManager = LinearLayoutManager(context)
        binding!!.rclEmployee.setHasFixedSize(true)
        adapter?.setClickIntentProfile(this)
        binding!!.rclEmployee.itemAnimator = null
    }
    override fun onResume() {
        super.onResume()
        staffList.clear()
        listEmployee()
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
    /**
     * Tim kiem nhan vien supplier]
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
                adapter!!.setDataSourceOff(newList)
                binding!!.rclEmployee.setHasFixedSize(true)
                binding!!.rclEmployee.adapter = adapter

                return false
            }
        })
    }
    /**
     * ìnterfaces chi tiết nhân viên
     */
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
    //___________________API___________________//
    //Gọi API  get danh sách nhân viên off
    private fun listEmployee() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees?status=${TechresEnum.EMPLOYEE_OFF_MANAGER}"
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

                        adapter!!.setDataSourceOff(staffList)
                        adapter!!.notifyDataSetChanged()
                        binding!!.txtMember.text = staffList.size.toString()
                    } else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    /* Gọi API trạng thái nhân viên */
    private fun changeEmployee(id: Int) {
        val params = BaseParams()
        params.http_method = 1 //post 1 get 0
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
                override fun onError(e: Throwable) {
                }

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
    //______________ONCLICK INTERFACE________________//
    override fun onCheck(position: Int, employee: DataListEmployee) {
        openDialogNotify(employee.id!!)
    }
    //Dialog notify
    private fun openDialogNotify(id: Int) {
        val dialog = Dialog(requireActivity())
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
            dialog.dismiss()
        }
        dialog.show()
    }
}


