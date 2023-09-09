package vn.techres.supplier.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.chatadapter.detailadapter.DetailGroupAdapter
import vn.techres.supplier.base.BaseBindingActivity
import vn.techres.supplier.databinding.ActivityDetailGroupChatBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.PreCachingLayoutManager
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickCall
import vn.techres.supplier.interfaces.RoleClick
import vn.techres.supplier.model.chat.data.DetailGroup
import vn.techres.supplier.model.chat.data.Members
import vn.techres.supplier.model.chat.params.RemoverMemberParams
import vn.techres.supplier.model.datamodel.DataListEmployee
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.DetailGroupResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.TechResTextView
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class DetailGroupChatActivity : BaseBindingActivity<ActivityDetailGroupChatBinding>(), RoleClick,
    OnClickCall {
    override val bindingInflater: (LayoutInflater) -> ActivityDetailGroupChatBinding
        get() = ActivityDetailGroupChatBinding::inflate
    private var groupID = ""
    private var restaurantID = 0
    var adapter: DetailGroupAdapter? = null
    private var detailGroup: DetailGroup? = null
    override fun onSetBodyView() {
        //Get data to groupFragment
        onSearch()
        groupID = intent.getStringExtra(TechresEnum.ID_GROUP.toString())!!
        adapter = DetailGroupAdapter(this)
        binding.rcvMember.adapter = adapter
        binding.rcvMember.layoutManager =
            PreCachingLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rcvMember.setHasFixedSize(true)
        binding.rcvMember.itemAnimator = DefaultItemAnimator()
        adapter!!.setRoleClick(this)
        adapter!!.setGroupOrderCall(this)
        adapter!!.setGroupOrderCallVideo(this)
        binding.header.toolbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.actionGroup.btnSearchEmployee.setOnClickListener {
            onClickSearchEmployee()
        }
        binding.actionGroup.addMembers.setOnClickListener {
            val intent = Intent(this, AddMemberGroupChatActivity::class.java)
            intent.putExtra(TechresEnum.ID_GROUP.toString(), groupID)
            startActivity(intent)
        }
        binding.actionGroup.btnArchive.setOnClickListener {
            val intent = Intent(this, ArchiveMainChatActivity::class.java)
            intent.putExtra(TechresEnum.ID_GROUP.toString(), groupID)
            startActivity(intent)
        }
        binding.header.toolbarTitle.text = getString(R.string.txt_detail_group)
        getDetailGroup()
    }

    private fun onClickSearchEmployee() {
        if (binding.searchStaff.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(binding.searchStaff)
            binding.searchStaff.visibility = View.VISIBLE

        } else {
            TransitionManager.beginDelayedTransition(binding.searchStaff)
            binding.searchStaff.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        getDetailGroup()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun onSearch() {
        binding.searchStaff.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList: ArrayList<Members> = ArrayList()
                for (key in detailGroup!!.members) {
                    if (detailGroup!!.members.size > 0) {
                        val name: String = key.full_name
                        if (name.lowercase(Locale.getDefault())
                                .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapter!!.setDataSource(newList)
                binding.rcvMember.setHasFixedSize(true)
                binding.rcvMember.adapter = adapter
                return false
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun successGetDetailGroup(data: DetailGroup) {
        detailGroup = data
        binding.informationGroup.chatContactName.text = data.restaurant_name
        AppUtils.callGroupAvatar(
            data.avatar,
            binding.informationGroup.chatContactImage
        )
        restaurantID = data.restaurant_id

        adapter!!.setDataSource(detailGroup!!.members)
    }

    /**
     * goi API get chi tiet danh sach group chat
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
                        if (response.data != null) {
                            successGetDetailGroup(response.data!!)
                        }else {
                            Toast.makeText(this@DetailGroupChatActivity, response.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })

    }

    /**
     * goi API kick nhan vien ra khoi group chat
     */
    private fun removeMember(memberID: Int) {
        val params = RemoverMemberParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_CHAT
        params.params.member_id = memberID
        params.request_url = "/api/groups-tms-supplier/$groupID/remove-user"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, this
        )
            .removeMember(params)
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
                        detailGroup!!.members.clear()
                        getDetailGroup()
                    }else {
                        Toast.makeText(this@DetailGroupChatActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun dialogRemoveMember(memberID: Int) {
        val dialog = Dialog(this)
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
        txtNotify.setText(R.string.remove_member)

        btnYes.setOnClickListener {
            removeMember(memberID)
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }

    override fun onItemTag(user: DataListEmployee?, position: Int, isTag: Boolean) {
    }

    override fun onItemTag(id: Int, position: Int, isTag: Boolean) {
        dialogRemoveMember(id)
    }

    override fun onClickCall(data: Members) {


    }

    override fun onClickCallVideo(data: Members) {

    }
}