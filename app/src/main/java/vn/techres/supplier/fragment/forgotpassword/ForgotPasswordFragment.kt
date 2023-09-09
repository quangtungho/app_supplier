package vn.techres.supplier.fragment.forgotpassword

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentForgotPasswordBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.entity.CurrentConfigJava
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.ForgotPasswordParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.ConfigJavaResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */

class ForgotPasswordFragment :
        BaseBindingFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {
    val tagName: String = ForgotPasswordFragment::class.java.name
    private var phone = ""
    private val digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-_."
    private val names = "1234567890"
    private var supplierName: String? = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        binding!!.header.toolbarTitle.text = resources.getString(R.string.forgotpass_title)
        binding!!.header.toolbarBack.setOnClickListener { onBack() }
        mainActivity!!.setBackClick(true)
        mainActivity!!.setHideHeader(false)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.linearLayout.startAnimation(animFragmentNext)
        binding!!.btnContinue.setOnClickListener {
            binding!!.btnContinue.isEnabled = false
            Handler().postDelayed({ binding!!.btnContinue.isEnabled = true }, 5000)
            hideKeyboard()
            if (checkValid()) {
                cacheManager.put(
                        TechresEnum.KEY_PHONE.toString(), binding!!.edtUsername.text.toString()
                )
                getConfigsJava(binding!!.edtCompany.text.toString())
            }
        }
        binding!!.edtCompany.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding!!.edtUsername.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding!!.edtCompany.addTextChangedListener(textSupplierNameWatcher)
        binding!!.edtUsername.addTextChangedListener(textPhoneWatcher)
    }

    private fun checkInput(): Boolean? {
        return if (supplierName == "") false else phone != ""
    }

    private fun Fragment.hideKeyboard() {
        val activity = this.activity
        if (activity is AppCompatActivity) {
            activity.hideKeyboard()
        }
    }

    private fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }

    private fun checkValid(): Boolean {
        if (binding!!.edtCompany.text.toString().isEmpty()) {
            val snack = Snackbar.make(
                    requireView(),
                    getString(R.string.login_label_supplier_name_must_not_be_empty),
                    Snackbar.LENGTH_LONG
            )
            snack.show()

            return false
        }
        if (binding?.edtUsername?.text!!.length < 4) {
            val snack = Snackbar.make(
                    requireView(),
                    getString(R.string.invalid_phone_number),
                    Snackbar.LENGTH_LONG
            )
            snack.show()

            return false
        }
        if (binding?.edtUsername?.text.toString().isEmpty()) {
            val snack = Snackbar.make(
                    requireView(),
                    getString(R.string.toast_phones_staff_empty),
                    Snackbar.LENGTH_LONG
            )
            snack.show()

            return false
        }
        return true
    }

    private val textSupplierNameWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun afterTextChanged(s: Editable) {
            supplierName = s.toString()
            if (s.isNotEmpty()) {
                for (i in 0 until supplierName!!.length) {
                    if (!digits.contains(supplierName!![i].toString() + "")) {
                        supplierName = supplierName!!.replace(supplierName!![i].toString() + "", "")
                        binding!!.edtCompany.setText(supplierName)
                        binding!!.edtCompany.setSelection(i)
                    }
                    if (!checkInput()!!) {
                        binding!!.btnContinue.setBackgroundResource(R.drawable.border_button_gray_color)
                        binding!!.btnContinue.isEnabled = false
                    } else {
                        binding!!.btnContinue.setBackgroundResource(R.drawable.border_button_main_color)
                        binding!!.btnContinue.isEnabled = true
                    }
                }
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    private val textPhoneWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {
            phone = s.toString()
            if (s.isNotEmpty()) {
                for (i in phone.indices) {
                    if (!names.contains(phone[i].toString() + "")) {
                        phone = phone.replace(phone[i].toString() + "", "")
                        binding!!.edtUsername.setText(phone)
                        binding!!.edtUsername.setSelection(i)
                    }
                }
            }
            if (!checkInput()!!) {
                binding!!.btnContinue.setBackgroundResource(R.drawable.border_button_gray_color)
                binding!!.btnContinue.isEnabled = false
            } else {
                binding!!.btnContinue.setBackgroundResource(R.drawable.border_button_main_color)
                binding!!.btnContinue.isEnabled = true
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    private fun getConfigsJava(supplierName: String) {
        val baseParams = BaseParams()
        baseParams.project_id = AppConfig.PROJECT_OAUTH
        baseParams.request_url =
                "/api/configs?project_id=${TechresEnum.PROJECT_ID}&restaurant_name&supplier_name=$supplierName"
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .configsJava(baseParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ConfigJavaResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: ConfigJavaResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            CurrentConfigJava.saveConfigJava(SupplierApplication.context, response.data)
                            forgotPassword(binding!!.edtUsername.text.toString())
                        } else {
                            val snack = Snackbar.make(
                                    requireView(),
                                    response.message!!,
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()
                        }

                    }
                })
    }

    private fun forgotPassword(userName: String) {
        val params = ForgotPasswordParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_OAUTH
        params.params.username = userName
        params.params.baisic_token = CurrentUser.getAccessToken(requireActivity())
        params.request_url = "/api/suppliers/forgot-password"
        ServiceFactory.createRetrofitService(TechResService::class.java, requireActivity())
                .getForgotPassword(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        val snack = Snackbar.make(
                                requireView(),
                                R.string.onError,
                                Snackbar.LENGTH_LONG
                        )
                        snack.show()

                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: BaseResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            val bundle = Bundle()
                            bundle.putString("data", phone)
                            activity?.supportFragmentManager?.beginTransaction()
                                    ?.replace(R.id.nav_host, OtpVerificationFragment())?.commitNow()
                        } else {
                            val snack = Snackbar.make(
                                    requireView(),
                                    response.message!!,
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()

                        }
                    }
                })
    }

    override fun onBackPress() {
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host, LoginFragment())?.commitNow()
    }
}