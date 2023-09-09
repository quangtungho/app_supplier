package vn.techres.supplier.fragment.customermanager

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils.TruncateAt.MARQUEE
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.customeradapter.ContactCustomerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentCustomerManagerBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.CustomerGetByIdResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class CustomerProfileFragment :
    BaseBindingFragment<FragmentCustomerManagerBinding>(FragmentCustomerManagerBinding::inflate) {
    val tagName: String = CustomerProfileFragment::class.java.name
    private var adapter: ContactCustomerAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        binding!!.viewPagerNV.adapter = adapter
        binding!!.tablayoutNV.setupWithViewPager(binding!!.viewPagerNV)
        binding!!.txtAddressCustomer.ellipsize = MARQUEE
        binding!!.txtAddressCustomer.marqueeRepeatLimit = -1
        binding!!.txtAddressCustomer.isSelected = true
        binding!!.txtEmailCustomer.ellipsize = MARQUEE
        binding!!.txtEmailCustomer.marqueeRepeatLimit = -1
        binding!!.txtEmailCustomer.isSelected = true
        fragmentCreate()
        AppUtils.callPhotoAvatar(
            cacheManager.get(TechresEnum.GET_BY_AVATAR.toString()), binding!!.imgAccountDetail
        )
        onClickViewAvatar()
        getAPIRestaurant()
    }
    override fun onResume() {
        super.onResume()

        getAPIRestaurant()
    }
    private fun fragmentCreate() {
        mainActivity!!.setHeader(getString(R.string.txt_restaurants_information))

        if (cacheManager.get(TechresEnum.KEY_BACK.toString()) == TechresEnum.KEY_BACK.toString()) {
            cacheManager.put(
                TechresEnum.KEY_BACK.toString(),
                TechresEnum.CHAT_ID_FRAGMENT.toString()
            )
            val animFragmen = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
            binding!!.constrainLayoutMain.startAnimation(animFragmen)
        } else {
            val animFragmen = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
            binding!!.constrainLayoutMain.startAnimation(animFragmen)
        }
    }
    private fun onClickViewAvatar() {
        binding!!.avatarProfile.setOnClickListener {
            openDialogViewAvatar()
        }
    }
    /**
     * goi  API get chi tiet khach hang
     */
    private fun getAPIRestaurant() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/restaurants/" + cacheManager.get(TechresEnum.GET_BY_ID.toString())

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getCustomerGetById(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CustomerGetByIdResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: CustomerGetByIdResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        binding!!.txtNameCustomer.text = response.data?.name
                        binding!!.txtAddressCustomer.text = response.data?.address
                        binding!!.txtPhoneCustomer.text = response.data?.phone
                        binding!!.txtEmailCustomer.text = response.data?.email
                    } else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    //View full avatar
    private fun openDialogViewAvatar() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_showavatar_profile)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}