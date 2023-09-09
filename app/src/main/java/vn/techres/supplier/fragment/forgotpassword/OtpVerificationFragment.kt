package vn.techres.supplier.fragment.forgotpassword

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOtpVerificationBinding
import vn.techres.supplier.fragment.navigate.MainFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.PreferenceHelper
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.entity.CurrentConfigNode
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.eventbussms.EvenBusSMS
import vn.techres.supplier.model.params.*
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.ConfigNodeJsResponse
import vn.techres.supplier.model.response.UserNodeResponse
import vn.techres.supplier.model.response.UserResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.io.UnsupportedEncodingException
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.random.Random

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */

open class OtpVerificationFragment :
        BaseBindingFragment<FragmentOtpVerificationBinding>(FragmentOtpVerificationBinding::inflate) {
    val tagName: String = OtpVerificationFragment::class.java.name
    private var password: String? = ""
    private var phone: String? = ""
    private var pin = ""
    private var passWord = ""
    private var passConfirmWord = ""
    private var isVisiblePass = false
    private var isVisibleConfirmPass = false
    private val mCallbacks: OnVerificationStateChangedCallbacks? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun onBody() {
        mainActivity!!.setBackClick(true)
        mainActivity!!.setHideHeader(true)
        getConfigsNodeJs()
        phone = cacheManager.get(TechresEnum.KEY_PHONE.toString()).toString()
        binding!!.cvPasswordForgetCountdown.start()
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.linearLayout.startAnimation(animFragmentNext)
        mainActivity!!.setHeader(getString(R.string.forgotpass_title))
        mainActivity!!.setLoading(false)
        binding!!.btnConfirm.setOnClickListener {
            binding!!.btnConfirm.isEnabled = false
            Handler().postDelayed({ binding!!.btnConfirm.isEnabled = true }, 5000)
            hideKeyboard()
            if (checkValidate())
                verifyForgotPassword(
                        binding!!.edtPin.text.toString(),
                        binding!!.edtPass.text.toString()
                )


        }
        binding!!.cvPasswordForgetCountdown.setOnClickListener {
            forgotPassword(phone!!)
            binding!!.cvPasswordForgetCountdown.start()
        }
        binding!!.edtPin.addTextChangedListener(pinWatcher)
        binding!!.edtPass.addTextChangedListener(textPassWatcher)
        binding!!.edtConfirmPass.addTextChangedListener(textPassConfirmWatcher)
        if (binding!!.edtPass.length() > 0) {
            binding!!.imgVisibilityPass.visibility = View.VISIBLE
        } else {
            binding!!.imgVisibilityPass.visibility = View.GONE
        }
        binding!!.imgVisibilityConfirmPass.setOnClickListener {
            val selection = binding!!.edtConfirmPass.selectionEnd
            if (isVisibleConfirmPass) {
                isVisibleConfirmPass = false
                binding!!.imgVisibilityConfirmPass.setImageDrawable(resources.getDrawable(R.drawable.ic_unhide_line_24, null))
                binding!!.edtConfirmPass.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL or InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            } else {
                isVisibleConfirmPass = true
                binding!!.imgVisibilityConfirmPass.setImageDrawable(resources.getDrawable(R.drawable.ic_hide_line_24, null))
                binding!!.edtConfirmPass.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding!!.edtConfirmPass.setSelection(selection)
        }

        binding!!.imgVisibilityPass.setOnClickListener { view ->
            val selection = binding!!.edtPass.selectionEnd
            if (isVisiblePass) {
                isVisiblePass = false
                binding!!.imgVisibilityPass.setImageDrawable(resources.getDrawable(R.drawable.ic_unhide_line_24, null))
                binding!!.edtPass.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL or InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            } else {
                isVisiblePass = true
                binding!!.imgVisibilityPass.setImageDrawable(resources.getDrawable(R.drawable.ic_hide_line_24, null))
                binding!!.edtPass.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding!!.edtPass.setSelection(selection)
        }
        startPhoneNumberVerification(phone!!)
        binding!!.edtPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty()) {
                    binding!!.imgVisibilityPass.visibility = View.VISIBLE
                } else {
                    binding!!.imgVisibilityPass.visibility = View.GONE
                }
            }
        })
        binding!!.edtConfirmPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty()) {
                    binding!!.imgVisibilityConfirmPass.visibility = View.VISIBLE
                } else {
                    binding!!.imgVisibilityConfirmPass.visibility = View.GONE
                }
            }
        })
    }

    fun checkInput(): Boolean {
        return if (pin == "") false else if (passWord == "") false else passConfirmWord != ""
    }

    private val pinWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        @SuppressLint("UseCompatLoadingForDrawables")

        override fun afterTextChanged(s: Editable) {
            pin = s.toString()

            if (!checkInput()) {
                binding!!.btnConfirm.setBackgroundResource(R.drawable.border_button_gray_color)
                binding!!.btnConfirm.isEnabled = false
            } else {
                binding!!.btnConfirm.setBackgroundResource(R.drawable.border_button_main_color)
                binding!!.btnConfirm.isEnabled = true
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }
    private val textPassWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        @SuppressLint("UseCompatLoadingForDrawables")

        override fun afterTextChanged(s: Editable) {
            passWord = s.toString()

            if (!checkInput()) {
                binding!!.btnConfirm.setBackgroundResource(R.drawable.border_button_gray_color)
                binding!!.btnConfirm.isEnabled = false
            } else {
                binding!!.btnConfirm.setBackgroundResource(R.drawable.border_button_main_color)
                binding!!.btnConfirm.isEnabled = true
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }
    private val textPassConfirmWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun afterTextChanged(s: Editable) {
            passConfirmWord = s.toString()
            if (!checkInput()) {
                binding!!.btnConfirm.setBackgroundResource(R.drawable.border_button_gray_color)
                binding!!.btnConfirm.isEnabled = false
            } else {
                binding!!.btnConfirm.setBackgroundResource(R.drawable.border_button_main_color)
                binding!!.btnConfirm.isEnabled = true
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    open fun onReadMessage(event: EvenBusSMS) {
        val codeSMS: String = parseCode(event.message)!!
        binding!!.edtPin.setText(codeSMS)
    }


    private fun parseCode(message: String): String? {
        val p = Pattern.compile("\\b\\d{4}\\b")
        val m = p.matcher(message)
        var code: String? = ""
        while (m.find()) {
            code = m.group(0)
        }
        return code
    }

    open fun startPhoneNumberVerification(phoneNumber: String) {
        val phoneUtil = PhoneNumberUtil.getInstance()
        var numberProto: PhoneNumber? = null
        try {
            numberProto = phoneUtil.parse(phoneNumber, "VN")
        } catch (e: NumberParseException) {
            e.printStackTrace()
        }
        mCallbacks?.let {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneUtil.format(
                            numberProto,
                            PhoneNumberUtil.PhoneNumberFormat.E164
                    ),
                    60,
                    TimeUnit.SECONDS,
                    requireActivity(),
                    it
            )
        }
        binding!!.cvPasswordForgetCountdown.start()
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

    private fun checkValidate(): Boolean {

        if (Objects.requireNonNull(binding!!.edtPin.text).toString().isEmpty()) {
            Toast.makeText(requireActivity(), getString(R.string.empty_pin), Toast.LENGTH_SHORT)
                    .show()
            binding!!.edtPin.isFocusable = true
            return false
        }
        if (Objects.requireNonNull(binding!!.edtPass.text).toString().isEmpty()) {
            Toast.makeText(requireActivity(), getString(R.string.empty_pass), Toast.LENGTH_SHORT)
                    .show()
            binding!!.edtPass.isFocusable = true
            return false
        }
        if (Objects.requireNonNull(binding!!.edtPass.text).length ?: 0 < 4) {
            Toast.makeText(requireActivity(), getString(R.string.login_label_password_must_not_be_password), Toast.LENGTH_SHORT)
                    .show()
            binding!!.edtPass.isFocusable = true

            return false
        }
        if (Objects.requireNonNull(binding!!.edtConfirmPass.text).toString().isEmpty()) {
            Toast.makeText(
                    requireActivity(),
                    getString(R.string.empty_pass_again),
                    Toast.LENGTH_SHORT
            ).show()
            binding!!.edtConfirmPass.isFocusable = true

            return false
        }
        if (binding!!.edtConfirmPass.text.toString() != binding!!.edtPass.text.toString()) {
            Toast.makeText(requireActivity(), getString(R.string.unmatch_pass), Toast.LENGTH_SHORT)
                    .show()
            return false
        }
        return true
    }

    private fun verifyForgotPassword(verifyCode: String, newPassword: String) {
        val params = VerifyForgotParams()
        val data: ByteArray
        var base64: String? = ""
        val stringTest: String = newPassword
        try {
            data = stringTest.toByteArray(charset("UTF-8"))
            password = stringTest
            base64 = Base64.encodeToString(
                    data,
                    Base64.NO_WRAP or Base64.URL_SAFE
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_OAUTH
        params.params.username = phone
        params.params.verify_code = verifyCode
        params.params.new_password = base64.toString()
        params.request_url = "/api/suppliers/verify-change-password"
        ServiceFactory.createRetrofitService(TechResService::class.java, requireActivity())
                .getVerifyForgotPassword(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: BaseResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            val snack = Snackbar.make(
                                    requireView(),
                                    getString(R.string.txt_Password_Newcofim),
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()

                            onLoginJava(
                                    phone!!,
                                    binding!!.edtPass.text.toString()
                            )
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
        params.request_url = "/api/suppliers/forgot-password"
        ServiceFactory.createRetrofitService(TechResService::class.java, requireActivity())
                .getForgotPassword(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: BaseResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            mainActivity!!.setLoading(false)
                        }
                    }
                })
    }

    private fun getConfigsNodeJs() {
        val baseParams = BaseParams()

        baseParams.http_method = 0
        baseParams.project_id = AppConfig.PROJECT_OAUTH_NODE
        baseParams.request_url =
                "/api/oauth-configs-nodejs/get-configs" + "?secret_key=cHJvamVjdC5zdXBwbGllci5rZXk=" + "&type=demo"
        ServiceFactory.createRetrofitServiceNode(
                TechResService::class.java, requireActivity()
        )
                .configsNodeJs(baseParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ConfigNodeJsResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {

                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: ConfigNodeJsResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            if (response.data != null) {
                                CurrentConfigNode.saveConfigNode(mainActivity!!, response.data)
                            }
                        }
                    }
                })
    }

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }

    private fun capitalize(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }

    private fun onLoginJava(userName: String, password_login: String) {
        val params = UserJavaParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_OAUTH
        params.request_url = "/api/suppliers/login"

        val data: ByteArray
        var base64: String? = ""
        val stringTest: String = password_login
        try {
            data = stringTest.toByteArray(charset("UTF-8"))
            password = stringTest
            base64 = Base64.encodeToString(
                    data,
                    Base64.NO_WRAP or Base64.URL_SAFE
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        params.params.username = userName
        params.params.password = base64.toString()
        params.params.device_uid = generateID()
        params.params.os_name = "android"

        params.let {
            ServiceFactory.createRetrofitService(
                    TechResService::class.java, requireActivity()
            )
                    .loginJava(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<UserResponse> {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {
                        }

                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(response: UserResponse) {
                            if (response.status == AppConfig.SUCCESS_CODE) {
                                val user = response.data
                                CurrentUser.saveUserInfo(context!!, user)
                                onLoginNodeJs(binding?.edtPass?.text.toString())
                                supplierDevice()
                                pushToken()
                                val snack = Snackbar.make(
                                        requireView(),
                                        getString(R.string.welcome_login_success) + user!!.name,
                                        Snackbar.LENGTH_LONG
                                )
                                snack.show()


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
    }

    private fun onLoginNodeJs(password_login: String) {
        val params = UserNodeJsParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_OAUTH_NODE
        params.request_url = "/api/oauth-login-nodejs/login"

        val data: ByteArray
        var base64: String? = ""
        val stringTest: String = password_login
        try {
            data = stringTest.toByteArray(charset("UTF-8"))
            password = stringTest
            base64 = Base64.encodeToString(
                    data,
                    Base64.NO_WRAP or Base64.URL_SAFE
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        params.params.username = CurrentUser.getCurrentUser(mainActivity!!)!!.username
        params.params.password = base64.toString()
        params.params.device_uid = generateID().toString()
        params.params.os_name = "android"
        params.params.type_user = TechresEnum.TYPE_USER.toString().toInt()

        params.params.user_id = CurrentUser.getCurrentUser(mainActivity!!)!!.id
        params.params.supplier_id = CurrentUser.getCurrentUser(mainActivity!!)!!.supplier_id
        params.params.role_name =
                CurrentUser.getCurrentUser(mainActivity!!)!!.supplier_role_name
        params.params.role_id =
                CurrentUser.getCurrentUser(mainActivity!!)!!.supplier_role_id

        params.params.full_name = CurrentUser.getCurrentUser(mainActivity!!)!!.name
        params.params.phone = CurrentUser.getCurrentUser(mainActivity!!)!!.phone
        params.params.avatar = CurrentUser.getCurrentUser(mainActivity!!)!!.avatar
        params.params.device_name = getDeviceName()

        params.let {
            ServiceFactory.createRetrofitLoginNode(
                    TechResService::class.java, requireActivity()
            )
                    .loginNodeJs(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<UserNodeResponse> {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {
                        }

                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(response: UserNodeResponse) {
                            if (response.status == AppConfig.SUCCESS_CODE) {
                                val user = CurrentUser.getCurrentUser(mainActivity!!)
                                user!!._id = response.data!!._id
                                user.nodeAccessToken = response.data!!.node_access_token
                                CurrentUser.saveUserInfo(context!!, user)
                                SupplierApplication.initializationSocket()
                                activity?.supportFragmentManager?.beginTransaction()
                                        ?.replace(R.id.nav_host, MainFragment())?.commitNow()
                            } else {
                                mainActivity?.setLoading(false)
                                Toast.makeText(
                                        requireActivity(),
                                        response.message,
                                        Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
        }
    }

    private fun supplierDevice() {
        val sharedPreference = PreferenceHelper(requireContext())
        val params = DeviceParams()
        params.http_method = 1 /*post 1/get 0*/
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/register-supplier-device"
        params.params.restaurant_id = CurrentUser.getCurrentUser(mainActivity!!)!!.restaurant_id
        params.params.device_uid = generateID()
        params.params.push_token =
                sharedPreference.getValueString(TechresEnum.PUSH_TOKEN.toString())
        params.params.os_name = "Android"
        params.params.customer_id = CurrentUser.getCurrentUser(mainActivity!!)!!.id

        params.let {
            ServiceFactory.createRetrofitService(
                    TechResService::class.java, requireActivity()
            )
                    .getSupplierDevice(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseResponse> {
                        override fun onComplete() {}
                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(response: BaseResponse) {
                            if (response.status != AppConfig.SUCCESS_CODE) {
                                Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                        .show()
                            }
                        }

                        override fun onError(e: Throwable) {
                        }
                    })
        }
    }

    private fun pushToken() {
        val sharedPreference = PreferenceHelper(requireContext())
        val params = PushOutTokenParams()
        params.http_method = 1 /*post 1/get 0*/
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url = "/api/notification/push-token-supplier"
        params.params.push_token =
                sharedPreference.getValueString(TechresEnum.PUSH_TOKEN.toString())
        params.params.os_name = TechresEnum.OS_NAME.toString()
        params.params.device_uid = generateID()
        ServiceFactory.createRetrofitServiceNode(
                TechResService::class.java, requireActivity()
        )
                .getPushOutToken(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResponse> {
                    override fun onComplete() {}
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: BaseResponse) {
                        if (response.status != AppConfig.SUCCESS_CODE) {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                        }
                    }

                    override fun onError(e: Throwable) {}
                })
    }

    override fun onBackPress() {
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host, ForgotPasswordFragment())?.commitNow()
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    @SuppressLint("HardwareIds")
    fun generateID(): String? {
        @SuppressLint("HardwareIds")
        var deviceId =
                Settings.Secure.getString(
                        requireActivity().contentResolver,
                        Settings.Secure.ANDROID_ID
                )
        if ("9774d56d682e549c" == deviceId || deviceId == null) {
            if (ActivityCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
            }
            deviceId =
                    (requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
                            .deviceId
            if (deviceId == null) {
                val tmpRand = Random
                deviceId = java.lang.String.valueOf(tmpRand.nextLong())
            }

            return deviceId
        }
        return deviceId
    }
}