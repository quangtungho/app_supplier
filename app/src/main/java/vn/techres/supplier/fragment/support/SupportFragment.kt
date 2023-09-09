package vn.techres.supplier.fragment.support

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentSupportBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.params.FeedBackParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class SupportFragment :
    BaseBindingFragment<FragmentSupportBinding>(FragmentSupportBinding::inflate) {
    val tagName: String = SupportFragment::class.java.name
    var items = arrayOf(0, 1, 2)
    var idTypeSupport = 0
    var projectID = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        mainActivity!!.setHeader(getString(R.string.headsupport))
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        dataUser()
        spinnerSupport()
        binding!!.btnSend.setOnClickListener {
            if (checkValid()) {
                feedBackSupplier(
                    binding!!.txtSupportUser.text.toString(),
                    CurrentUser.getCurrentUser(mainActivity!!)!!.email,
                    binding!!.txtSupportPhone.text.toString(),
                    binding!!.txtSupportTitle.text.toString(),
                    idTypeSupport,
                    projectID
                )
            }
        }

    }
    private fun checkValid(): Boolean {
        if (binding!!.txtSupportTitle.text.toString().isEmpty()) {
            val snack = Snackbar.make(
                requireView(),
                "Vui lòng nhâp nội dung phản hồi",
                Snackbar.LENGTH_LONG
            )
            snack.show()
            return false
        }
        return true
    }
    private fun dataUser() {
        binding!!.txtSupportUser.text = CurrentUser.getCurrentUser(mainActivity!!)!!.name
        binding!!.txtSupportPhone.text = CurrentUser.getCurrentUser(mainActivity!!)!!.phone
        projectID = TechresEnum.PROJECT_ID.toString()
    }

    private fun spinnerSupport() {
        idTypeSupport = 0
        val options = arrayOf(
            getString(R.string.sp_Title_1),
            getString(R.string.sp_Title_2),
            getString(R.string.sp_Title_3)
        )
        binding!!.spinnerData.adapter =
            ArrayAdapter(mainActivity!!, android.R.layout.simple_list_item_1, options)
        binding!!.spinnerData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in items.indices) {
                    if (items[p2] == 0) {
                        idTypeSupport = 0
                        binding!!.txtSupport.text = getString(R.string.sp_Title_1)
                        break
                    } else if (items[p2] == 1) {
                        idTypeSupport = 1
                        binding!!.txtSupport.text = getString(R.string.sp_Title_2)
                        break
                    } else {
                        idTypeSupport = 2
                        binding!!.txtSupport.text = getString(R.string.sp_Title_3)
                        break
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

    }

    private fun feedBackSupplier(
        name: String,
        email: String,
        phone: String,
        content: String, type: Int, project: String
    ) {
        val params = FeedBackParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees/feedback"
        params.params.name = name
        params.params.email = email
        params.params.phone = phone
        params.params.content = content
        params.params.type = type
        params.params.project_id = project
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .feedBack(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    Toast.makeText(
                        requireActivity(), getString(R.string.onError),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onNext(response: BaseResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        spinnerSupport()
                        mainActivity!!.supportFragmentManager.popBackStack()
                        Toast.makeText(mainActivity, "Đã gửi phản hồi", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }


    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}



