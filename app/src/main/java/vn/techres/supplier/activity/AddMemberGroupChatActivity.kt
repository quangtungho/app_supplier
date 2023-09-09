package vn.techres.supplier.activity

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.chatadapter.detailadapter.AddMemberGroupAdapter
import vn.techres.supplier.base.BaseBindingActivity
import vn.techres.supplier.databinding.ActivityAddMemberGroupChatBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.PreCachingLayoutManager
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.RoleClick
import vn.techres.supplier.model.chat.data.EmployeeGroup
import vn.techres.supplier.model.chat.data.Members
import vn.techres.supplier.model.chat.params.AddMemberParams
import vn.techres.supplier.model.datamodel.DataListEmployee
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.DetailGroupResponse
import vn.techres.supplier.model.response.EmployeeResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class AddMemberGroupChatActivity : BaseBindingActivity<ActivityAddMemberGroupChatBinding>(),
    RoleClick {
    override val bindingInflater: (LayoutInflater) -> ActivityAddMemberGroupChatBinding
        get() = ActivityAddMemberGroupChatBinding::inflate
    var addMemberGroupAdapter: AddMemberGroupAdapter? = null
    private var groupID = ""
    var staffList: ArrayList<DataListEmployee> = ArrayList()
    private var addMember = ArrayList<Members>()
    var employeeDetail = ArrayList<Members>()
    var listMemberDetailGroup: ArrayList<Int> = ArrayList()
    private val employeeGroupList: ArrayList<EmployeeGroup> = ArrayList()
    override fun onSetBodyView() {
        binding.loadingData.rlLoading.visibility = View.GONE
        groupID = intent.getStringExtra(TechresEnum.ID_GROUP.toString())!!
        addMemberGroupAdapter = AddMemberGroupAdapter(this)
        addMemberGroupAdapter!!.setRoleClick(this)
        binding.rclTagRole.adapter = addMemberGroupAdapter
        binding.rclTagRole.layoutManager =
            PreCachingLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rclTagRole.setHasFixedSize(true)
        binding.rclTagRole.itemAnimator = DefaultItemAnimator()
        binding.rclTagRole.setItemViewCacheSize(200)
        binding.header.toolbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.header.toolbarAction.text = getString(R.string.txt_add)
        binding.header.toolbarAction.visibility = View.GONE
        binding.header.toolbarTitle.text = getString(R.string.txt_add_member)
        getDetailGroup()
        onSearch()

        binding.header.toolbarAction.setOnClickListener {
            addEmployee()
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    /**
     * Tim kiếm nhan vien them vao
     */
    private fun onSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
                addMemberGroupAdapter!!.setDataSource(newList)
                binding.rclTagRole.setHasFixedSize(true)
                binding.rclTagRole.adapter = addMemberGroupAdapter

                return false
            }
        })
    }

    private fun setTimeout(runnable: Runnable) {
        Thread {
            try {
                Thread.sleep(5000)
                runnable.run()
            } catch (ignored: Exception) {
            }
        }.start()
    }

    /**
     * goi API get danh sach nhan vien  can them vao group
     */
    private fun listEmployee() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees?status=${TechresEnum.EMPLOYEE_ON_MANAGER}"
        ServiceFactory.createRetrofitService(
            TechResService::class.java, this
        )
            .getEmployee(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<EmployeeResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: EmployeeResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        staffList = response.data
                        for (i in 0 until staffList.size) {
                            for (j in 0 until listMemberDetailGroup.size) {
                                if (listMemberDetailGroup[j] == staffList[i].id) {
                                    staffList[i].is_join = true
                                }
                            }
                        }
                        addMemberGroupAdapter!!.setDataSource(staffList)
                        setTimeout {
                            binding.loadingData.rlLoading.visibility = View.GONE
                        }

                        binding.tvTotalRecord.text = staffList.size.toString()
                    }else {
                        Toast.makeText(this@AddMemberGroupChatActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    /**
     * goi API get chi tiết group chat
     */
    private fun getDetailGroup() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url = "/api/group-tms-supplier/$groupID/detail"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, this
        )
            .getDetailGroup(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DetailGroupResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: DetailGroupResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        employeeDetail = response.data!!.members
                        listMemberDetailGroup.clear()
                        for (i in 0 until employeeDetail.size) {
                            if (employeeDetail[i].app_name == "supplier") {
                                listMemberDetailGroup.add(employeeDetail[i].member_id)
                            }
                        }
                        setTimeout {
                            binding.loadingData.rlLoading.visibility = View.GONE
                        }
                        listEmployee()

                    }else {
                        Toast.makeText(this@AddMemberGroupChatActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    /**
     * goi API post them nhan vien vao group chat
     */
    private fun addEmployee() {
        addMember.clear()
        for (i in 0 until employeeGroupList.size) {
            addMember.add(
                Members(
                    employeeGroupList[i].full_name,
                    employeeGroupList[i].role_name,
                    employeeGroupList[i].avatar,
                    employeeGroupList[i].member_id,
                    employeeGroupList[i].role_id,
                    employeeGroupList[i].app_name
                )
            )
        }
        val params = AddMemberParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url = "/api/groups-tms-supplier/$groupID/add-user"
        params.params.members = addMember
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, this
        )
            .addMember(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: BaseResponse) {
                    Toast.makeText(
                        this@AddMemberGroupChatActivity,
                        getString(R.string.add_user_group_chat),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.loadingData.rlLoading.visibility = View.GONE
                    staffList.clear()
                    finish()
                }
            })
    }

    override fun onItemTag(user: DataListEmployee?, position: Int, isTag: Boolean) {
        if (isTag) {
            employeeGroupList.add(
                EmployeeGroup(
                    user!!.avatar!!,
                    user.name!!,
                    user.id!!,
                    user.supplier_role_id!!,
                    user.supplier_employee_position!!
                )
            )
            binding.header.toolbarAction.visibility = View.VISIBLE
        } else {
            for (i in 0 until employeeGroupList.size) {
                if (employeeGroupList[i].member_id == CurrentUser.getCurrentUser(this)!!.id) {
                    employeeGroupList.removeAt(i)
                }
            }
            if (employeeGroupList.size == 0) {
                binding.header.toolbarAction.visibility = View.GONE
            }
        }
    }

    override fun onItemTag(id: Int, position: Int, isTag: Boolean) {

    }
}