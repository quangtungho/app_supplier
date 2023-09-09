package vn.techres.supplier.fragment.forgotpassword

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.an.biometric.BiometricCallback
import com.an.biometric.BiometricManager
import com.an.biometric.BiometricManager.BiometricBuilder
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentLoginBinding
import vn.techres.supplier.fragment.navigate.MainFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.PreferenceHelper
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.entity.CurrentConfigJava
import vn.techres.supplier.model.entity.CurrentConfigNode
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.params.*
import vn.techres.supplier.model.response.*
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.io.UnsupportedEncodingException
import kotlin.random.Random

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class LoginFragment : BaseBindingFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate),
        BiometricCallback {
    val tagName: String = LoginFragment::class.java.name
    private var mBiometricManager: BiometricManager? = null
    private var isVisible: Boolean? = false
    private var supplierName: String? = ""
    private var userName: String? = ""
    private var password: String? = ""
    private val digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-_."
    private val names = "1234567890"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onBody() {
        mainActivity!!.setLoading(false)
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.loginFragment.startAnimation(animFragment)
        cacheManager.put(TechresEnum.KEY_BACK.toString(), TechresEnum.KEY_BACK.toString())
        mainActivity!!.setLinearLayoutVersion(false)
        mainActivity!!.setHideHeader(false)
        binding!!.btnForgotPass.setOnClickListener {
            onForget()
        }
//        binding!!.btnInformation.setOnClickListener {
//            openDialog()
//        }
        binding!!.btnLogin.setOnClickListener {
            binding!!.btnLogin.isEnabled = false
            Handler().postDelayed({ binding!!.btnLogin.isEnabled = true }, 5000)
            binding!!.btnLogin.showLoading()
            /**
             * Ẩn bàn phím khi đăng nhập
             */
            hideKeyboard()
            if (checkValid()) {
                getConfigsJava(binding!!.edtResName.text.toString())
                getConfigsNodeJs()
            }

        }
        binding!!.imgVisibility.setOnClickListener { v ->
            val selection = binding!!.edtPass.selectionEnd
            if (isVisible!!) {
                isVisible = false
                binding!!.imgVisibility.setImageDrawable(resources.getDrawable(R.drawable.ic_hide_line_24, null))
                binding!!.edtPass.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL or InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            } else {
                isVisible = true
                binding!!.imgVisibility.setImageDrawable(resources.getDrawable(R.drawable.ic_unhide_line_24, null))
                binding!!.edtPass.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding!!.edtPass.setSelection(selection)
        }
        binding!!.imgClearAccount.setOnClickListener {
            binding!!.edtUser.setText("")
            binding!!.edtUser.hint = resources.getString(R.string.user_name)
        }
        binding!!.imgClearRes.setOnClickListener {
            binding!!.edtResName.setText("")
            binding!!.edtResName.hint = resources.getString(R.string.supplier_name)
        }
        if (binding!!.edtResName.length() > 0) {
            binding!!.imgClearAccount.visibility = View.VISIBLE
        } else {
            binding!!.imgClearAccount.visibility = View.GONE
        }

        if (binding!!.edtUser.length() > 0) {
            binding!!.imgClearAccount.visibility = View.VISIBLE
        } else {
            binding!!.imgClearAccount.visibility = View.GONE
        }

        if (binding!!.edtPass.length() > 0) {
            binding!!.imgVisibility.visibility = View.VISIBLE
        } else {
            binding!!.imgVisibility.visibility = View.GONE
        }
        binding!!.edtResName.addTextChangedListener(textSupplierNameWatcher)
        binding!!.edtUser.addTextChangedListener(textUserWatcher)
        binding!!.edtPass.addTextChangedListener(textPassWatcher)
    }
    private fun onForget() {
        requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host, ForgotPasswordFragment()).commitNow()
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
        if (binding!!.edtResName.text.toString().isEmpty()) {
            val snack = Snackbar.make(
                    requireView(),
                    getString(R.string.login_label_supplier_name_must_not_be_empty),
                    Snackbar.LENGTH_LONG
            )
            snack.show()
            YoYo.with(Techniques.Shake).playOn(binding!!.edtResName)
            binding!!.btnLogin.hideLoading()
            return false
        }
        if (binding!!.edtUser.text.toString().isEmpty()) {
            val snack = Snackbar.make(
                    requireView(),
                    getString(R.string.login_label_user_must_not_be_empty),
                    Snackbar.LENGTH_LONG
            )
            snack.show()
            YoYo.with(Techniques.Shake).playOn(binding!!.edtUser)
            binding!!.btnLogin.hideLoading()
            return false
        }
        if (binding?.edtPass?.text.toString().isEmpty()) {
            val snack = Snackbar.make(
                    requireView(),
                    getString(R.string.login_label_password_must_not_be_empty),
                    Snackbar.LENGTH_LONG
            )
            snack.show()
            YoYo.with(Techniques.Shake).playOn(binding!!.edtPass)
            binding!!.btnLogin.hideLoading()
            return false
        } else if (binding?.edtPass?.text.toString().length < 4) {
            val snack = Snackbar.make(
                    requireView(),
                    getString(R.string.login_label_password_must_not_be_password),
                    Snackbar.LENGTH_LONG
            )
            snack.show()
            YoYo.with(Techniques.Shake).playOn(binding!!.edtPass)
            binding!!.btnLogin.hideLoading()
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
                        binding!!.edtResName.setText(supplierName)
                        binding!!.edtResName.setSelection(i)
                    }
                }

                binding!!.imgClearRes.visibility = View.VISIBLE
            } else {
                binding!!.imgClearRes.visibility = View.GONE
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    private val textUserWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {
            userName = s.toString()
            if (s.isNotEmpty()) {
                for (i in 0 until userName!!.length) {
                    if (!names.contains(userName!![i].toString() + "")) {
                        userName = userName!!.replace(userName!![i].toString() + "", "")
                        binding!!.edtUser.setText(userName)
                        binding!!.edtUser.setSelection(i)
                    }
                }
                binding!!.imgClearAccount.visibility = View.VISIBLE
            } else {
                binding!!.imgClearAccount.visibility = View.GONE
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    private val textPassWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {
            if (s.isNotEmpty()) {
                binding!!.imgVisibility.visibility = View.VISIBLE
            } else {
                binding!!.imgVisibility.visibility = View.GONE
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
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

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment().apply {
            arguments = Bundle().apply { }
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
                                closeKeyboard(binding?.edtUser)
                                closeKeyboard(binding?.edtPass)
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

                                binding!!.btnLogin.hideLoading()
                                //   binding!!.rlloading.visibility = View.GONE
                            } else {
                                //   binding!!.rlloading.visibility = View.GONE
                                //  YoYo.with(Techniques.Shake).playOn(binding!!.editArea2)
                                // binding!!.notice2.text = response.message!!

                                val snack = Snackbar.make(
                                        requireView(),
                                        response.message!!,
                                        Snackbar.LENGTH_LONG
                                )
                                snack.show()
                                binding!!.btnLogin.hideLoading()
                            }
                        }
                    })
        }
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
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: ConfigJavaResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            CurrentConfigJava.saveConfigJava(SupplierApplication.context, response.data)
                            onLoginJava(
                                    binding!!.edtUser.text.toString(),
                                    binding!!.edtPass.text.toString()
                            )
                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
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
                                CurrentConfigNode.saveConfigNode(requireActivity(), response.data)
                            }

                            binding!!.btnLogin.hideLoading()
                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                        }
                        binding!!.btnLogin.hideLoading()
                    }
                })
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

    private fun openDialog() {
        val dialog = Dialog(mainActivity!!)
        dialog.setContentView(R.layout.dialog_information)
        val window = dialog.window
        window?.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window?.attributes
        windowAttributes?.gravity = Gravity.NO_GRAVITY
        window?.attributes = windowAttributes

        dialog.show()
    }

    override fun onBackPress() {
        mainActivity!!.finish()
    }

    override fun onSdkVersionNotSupported() {
        val snack = Snackbar.make(
                requireView(),
                getString(R.string.biometric_error_sdk_not_supported),
                Snackbar.LENGTH_LONG
        )
        snack.show()

    }

    override fun onBiometricAuthenticationNotSupported() {
        val snack = Snackbar.make(
                requireView(),
                getString(R.string.biometric_error_hardware_not_supported),
                Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    override fun onBiometricAuthenticationNotAvailable() {
        val snack = Snackbar.make(
                requireView(),
                getString(R.string.biometric_error_fingerprint_not_available),
                Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    override fun onBiometricAuthenticationPermissionNotGranted() {
        val snack = Snackbar.make(
                requireView(),
                getString(R.string.biometric_error_permission_not_granted),
                Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    override fun onBiometricAuthenticationInternalError(error: String?) {
        Toast.makeText(mainActivity, error, Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationFailed() {
    }

    override fun onAuthenticationCancelled() {
//        Toast.makeText(
//            mainActivity,
//            getString(R.string.biometric_cancelled),
//            Toast.LENGTH_LONG
//        ).show()
//        mBiometricManager.cancelAuthentication()
    }

    override fun onAuthenticationSuccessful() {
        Toast.makeText(mainActivity, "Đang phát triển tính năng", Toast.LENGTH_SHORT).show()
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
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

