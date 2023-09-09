package vn.techres.supplier.activity

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal
import vn.techres.supplier.BuildConfig
import vn.techres.supplier.R
import vn.techres.supplier.databinding.DialogUpdateVersionBinding
import vn.techres.supplier.fragment.ordermanager.orderbilldetail.OrderBillDetailFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppConfig.cacheManager
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.helper.sticker.UI
import vn.techres.supplier.interfaces.OnBackClick
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.entity.User
import vn.techres.supplier.model.chat.data.StickerData
import vn.techres.supplier.model.chat.response.StickerDataResponse
import vn.techres.supplier.model.datamodel.DataGetVersion
import vn.techres.supplier.model.eventbussms.EventBusStatus
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.VersionResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.TechResTextView

private const val REQUEST_PERMISSION = 126

class MainActivity : AppCompatActivity(), View.OnClickListener, EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {
    private var txtHeader: TextView? = null
    private var txtVersion: TextView? = null
    private var linearLayoutVersion: LinearLayout? = null
    private var btnBack: RelativeLayout? = null
    private var header: Toolbar? = null
    private var rlLoading: FrameLayout? = null
    private var onBackClick: OnBackClick? = null
    private var btnAdd: TechResTextView? = null
    private var user = User()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION) {
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        }
    }
    override fun onRationaleAccepted(requestCode: Int) {
    }
    override fun onRationaleDenied(requestCode: Int) {
    }
    @AfterPermissionGranted(REQUEST_PERMISSION)
    private fun syncPermission() {
        if (EasyPermissions.hasPermissions(
                this,
                CAMERA,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
        ) {
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(
                this,
                "Vui lòng cấp quyền để ứng dụng hoạt động",
                REQUEST_PERMISSION,
                CAMERA, READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapping()
        syncPermission()
        getAllCategorySticker()
        checkVersionUpdate()
        if (intent.getBooleanExtra(TechresEnum.MOVE.toString(), false)) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host, OrderBillDetailFragment())
                .commit()
        }


        user = CurrentUser.getCurrentUser(applicationContext)!!
        btnBack?.setOnClickListener(this)
        onInternet()
        btnAdd?.setOnClickListener {
            EventBus.getDefault().post(EventBusStatus(1))
        }
        btnBack!!.visibility = View.GONE
        txtVersion!!.text = BuildConfig.VERSION_NAME
    }
    @SuppressLint("CutPasteId")
    private fun mapping() {
        txtHeader = findViewById(R.id.toolbar_title)
        btnBack = findViewById(R.id.toolbar_back)
        header = findViewById(R.id.toolbar)
        rlLoading = findViewById(R.id.rlLoading)
        txtVersion = findViewById(R.id.version)
        linearLayoutVersion = findViewById(R.id.linearLayoutVersion)
    }
    private fun onInternet() {
        // No Internet Dialog: Pendulum
        NoInternetDialogPendulum.Builder(this, lifecycle).apply {
            dialogProperties.apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {}
                }
                cancelable = false // Optional
                noInternetConnectionTitle = getString(R.string.no_internet) // Optional
                noInternetConnectionMessage = getString(R.string.check_internet)// Optional
                showInternetOnButtons = true // Optional
                pleaseTurnOnText = getString(R.string.please_turn_on) // Optional
                wifiOnButtonText = getString(R.string.wifi) // Optional
                mobileDataOnButtonText = getString(R.string.mobile_data)// Optional
                onAirplaneModeTitle = getString(R.string.no_internet) // Optional
                onAirplaneModeMessage = getString(R.string.on_the_airplance) // Optional
                pleaseTurnOffText = getString(R.string.please_turn_off) // Optional
                airplaneModeOffButtonText = getString(R.string.airplane_mode) // Optional
                showAirplaneModeOffButtons = true // Optional
            }
        }.build()
        // No Internet Dialog: Signal
        NoInternetDialogSignal.Builder(this, lifecycle).apply {
            dialogProperties.apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {}
                }
                cancelable = false // Optional
                noInternetConnectionTitle = getString(R.string.no_internet) // Optional
                noInternetConnectionMessage = getString(R.string.check_internet) // Optional
                showInternetOnButtons = true // Optional
                pleaseTurnOnText = getString(R.string.please_turn_on) // Optional
                wifiOnButtonText = getString(R.string.wifi) // Optional
                mobileDataOnButtonText = getString(R.string.mobile_data) // Optional
                onAirplaneModeTitle = getString(R.string.no_internet) // Optional
                onAirplaneModeMessage = getString(R.string.on_the_airplance) // Optional
                pleaseTurnOffText = getString(R.string.please_turn_off) // Optional
                airplaneModeOffButtonText = getString(R.string.airplane_mode) // Optional
                showAirplaneModeOffButtons = true // Optional
            }
        }.build()
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.toolbar_back -> onBackClick!!.onBack()
        }
    }
    fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            rlLoading?.visibility = View.VISIBLE

        } else if (!isLoading) {
            run {
                rlLoading?.visibility = View.GONE
            }
        }
    }
    fun setOnBackClick(onBackClick: OnBackClick) {
        this.onBackClick = onBackClick
    }
    fun setHeader(content: String) {
        txtHeader!!.text = content
        header!!.visibility = View.VISIBLE
    }
    fun setBackClick(active: Boolean) {
        if (active) {
            btnBack!!.visibility = View.VISIBLE
        } else {
            btnBack!!.visibility = View.GONE
        }
    }
    fun setHideHeader(active: Boolean) {
        if (active) {
            header!!.visibility = View.VISIBLE
        } else {
            header!!.visibility = View.GONE
        }
    }
    fun setLinearLayoutVersion(active: Boolean) {
        if (active) {
            linearLayoutVersion!!.visibility = View.VISIBLE
        } else {
            linearLayoutVersion!!.visibility = View.GONE
        }
    }
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyUp(keyCode, event)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            when (cacheManager.get(TechresEnum.KEY_BACK.toString())) {
                TechresEnum.KEY_BACK.toString() -> {
                    this.finish()
                }
                else -> {
                    onBackClick!!.onBack()
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    /**
     * goi API get sticker group chat
     */
    private fun getAllCategorySticker() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url = "/api/stickers"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, this
        )
            .getAllCategorySticker(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<StickerDataResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: StickerDataResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        getAllStickerSuccess(response.data)
                    } else
                        Toast.makeText(
                            this@MainActivity,
                            response.message,
                            Toast.LENGTH_LONG
                        ).show()
                }
            })
    }
    fun getAllStickerSuccess(data: StickerData?) {
        val gson = Gson()
        AppUtils.writeFileSticker(
            this,
            gson.toJson(data)
        )
        UI.setCategoryStickers(AppUtils.readFileSticker(this))
    }
    /**
     * goi API get check phiên ban cap nhat cho app
     */
    private fun checkVersionUpdate() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_OAUTH
        params.request_url = "/api/check-version?os_name=supplier_android"
        ServiceFactory.createRetrofitService(
            TechResService::class.java, this
        )
            .getVersion(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<VersionResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: VersionResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        val dataVersion = response.data
                        if (dataVersion.version != BuildConfig.VERSION_NAME) {
                            if (dataVersion.is_require_update == AppConfig.IS_REQUIRE_UPDATE) {
                                openDialogVersion(dataVersion)
                            }
                        }

                    }
                }
            })
    }
    /**
     * dialog show check phien ban cap nhat app
     */
    private fun openDialogVersion(dataVersion: DataGetVersion) {
        val bindingDialog = DialogUpdateVersionBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(bindingDialog.root)
        val animFragment =
            AnimationUtils.loadAnimation(SupplierApplication.context, R.anim.fade_out)
        bindingDialog.linearUpdate.startAnimation(animFragment)
        bindingDialog.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialog.btnUpdate.setOnClickListener {
            val urlRateApp = getString(R.string.supplier_link)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlRateApp)
            startActivity(intent)
        }
        dialog.setCancelable(false)
        bindingDialog.tvDescription.text = dataVersion.message
        dialog.show()
    }
}