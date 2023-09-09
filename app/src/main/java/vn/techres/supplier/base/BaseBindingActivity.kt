package vn.techres.supplier.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewbinding.ViewBinding
import net.gotev.uploadservice.data.UploadNotificationAction
import net.gotev.uploadservice.data.UploadNotificationConfig
import net.gotev.uploadservice.data.UploadNotificationStatusConfig
import net.gotev.uploadservice.extensions.getCancelUploadIntent
import vn.techres.supplier.R
import vn.techres.supplier.helper.CacheManager
import kotlin.random.Random

abstract class BaseBindingActivity<VB : ViewBinding> : AppCompatActivity() {
    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater) -> VB
    val cacheManager: CacheManager = CacheManager.getInstance()

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        onSetBodyView()
    }

    abstract fun onSetBodyView()

    fun closeKeyboard(edt: EditText) {
        edt.requestFocus()
        edt.isCursorVisible = false
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edt.windowToken, 0)

    }

    fun showKeyboard(edt: EditText) {
        edt.requestFocus()
        edt.isCursorVisible = true
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        edt.postDelayed({
            edt.requestFocus()
            imm.showSoftInput(edt, InputMethodManager.SHOW_FORCED)
        }, 100)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun getNotificationConfig(uploadId: String?, @StringRes title: Int): UploadNotificationConfig {
        val clickIntent = PendingIntent.getActivity(
            this, 1, Intent(this, this::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        val autoClear = true
        val clearOnAction = true
        val ringToneEnabled = true
        val noActions = ArrayList<UploadNotificationAction>(1)
        val cancelAction = UploadNotificationAction(
            R.drawable.ic_cancelled,
            getString(R.string.cancel_upload),
            this.getCancelUploadIntent(uploadId!!)
        )
        val progressActions = ArrayList<UploadNotificationAction>(1)
        progressActions.add(cancelAction)
        val progress = UploadNotificationStatusConfig(
            getString(title),
            getString(R.string.uploading),
            R.drawable.ic_upload,
            Color.BLUE,
            null,
            clickIntent,
            progressActions,
            clearOnAction,
            autoClear
        )
        val success = UploadNotificationStatusConfig(
            getString(title),
            getString(R.string.upload_success),
            R.drawable.ic_upload_success,
            Color.GREEN,
            null,
            clickIntent,
            noActions,
            clearOnAction,
            autoClear
        )
        val error = UploadNotificationStatusConfig(
            getString(title),
            getString(R.string.upload_error),
            R.drawable.ic_upload_error,
            Color.RED,
            null,
            clickIntent,
            noActions,
            clearOnAction,
            autoClear
        )
        val cancelled = UploadNotificationStatusConfig(
            getString(title),
            getString(R.string.upload_cancelled),
            R.drawable.ic_cancelled,
            Color.YELLOW,
            null,
            clickIntent,
            noActions,
            clearOnAction
        )
        return UploadNotificationConfig(
            "UploadServiceDemoChannel",
            ringToneEnabled,
            progress,
            success,
            error,
            cancelled
        )
    }
    @SuppressLint("HardwareIds")
    fun generateID(): String? {
        @SuppressLint("HardwareIds")
        var deviceId =
                Settings.Secure.getString(
                        this.contentResolver,
                        Settings.Secure.ANDROID_ID
                )
        if ("9774d56d682e549c" == deviceId || deviceId == null) {
            if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
            }
            deviceId =
                    (this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
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