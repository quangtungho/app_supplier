package vn.techres.supplier.helper

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


/**
 * open app details setting
 */
fun Context.openAppDetailSettings() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

/**
 * set first time asking multiple permissions
 */
fun Context.firstTimeAskingPermissions(permissions: Array<String>, isFirstTime: Boolean) {
    val sharedPreference: SharedPreferences? = getSharedPreferences(packageName, MODE_PRIVATE)
    for (permission in permissions) {
        sharedPreference?.edit()?.putBoolean(permission, isFirstTime)?.apply()
    }
}

/**
 * check if first time asking multiple permissions
 */
fun Context.isFirstTimeAskingPermissions(permissions: Array<String>): Boolean {
    val sharedPreference: SharedPreferences? = getSharedPreferences(packageName, MODE_PRIVATE)
    for (permission in permissions) {
        if (sharedPreference?.getBoolean(permission, true) == true) {
            return true
        }
    }
    return false
}

/**
 * Check if version is marshmallow and above. deciding to request runtime permission
 */
fun shouldRequestRuntimePermission(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}

/**
 * check grandResults are granted
 */
fun isGrantedGrantResults(grantResults: IntArray): Boolean {
    if (grantResults.isEmpty()) return false
    for (grantResult in grantResults) {
        if (grantResult != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

/**
 * check if multiple permissions are granted or not
 */
fun Context.shouldAskPermissions(permissions: Array<String>): Boolean {

    if (shouldRequestRuntimePermission()) {
        for (permission in permissions) {

            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
        }
    }

    return false
}

/**
 * Get name permission
 * */
fun Context.getNamePermission(permission: String): String {
    val packageManager = applicationContext.packageManager
    val permissionInfo: PermissionInfo =
        packageManager.getPermissionInfo(permission, 0)
    return packageManager.getPermissionGroupInfo(permissionInfo.group ?: "", 0)
        .loadLabel(packageManager).toString()
}

/**
 * Get name permission deny
 * */
fun Context.getNamePermissionDeny(permissions: Array<out String>): String {
    val permissionsDeny: ArrayList<String> = ArrayList()
    var string = ""
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsDeny.add(permission)
            string = if (string.isEmpty()) {
                getNamePermission(permission)
            } else {
                string + ", " + getNamePermission(permission)
            }
        }
    }
    return string
}

/**
 * check if should show request permissions rationale in activity
 */
@TargetApi(Build.VERSION_CODES.M)
fun <T : Activity> T.shouldShowRequestPermissionsRationale(permissions: Array<out String>): Boolean {
    for (permission in permissions) {
        if (shouldShowRequestPermissionRationale(permission)) {
            return true
        }
    }
    return false
}

/**
 * check if should show request permissions rationale in fragment
 */
fun <T : Fragment> T.shouldShowRequestPermissionsRationale(permissions: Array<out String>): Boolean {
    for (permission in permissions) {
        if (shouldShowRequestPermissionRationale(permission)) {
            return true
        }
    }
    return false
}

/**
 * request permissions in activity
 */
@TargetApi(Build.VERSION_CODES.M)
fun <T : Activity> T.requestPermissions(
    permissions: Array<String>,
    permissionRequestCode: Int,
    requestPermissionListener: RequestPermissionListener,
) {
    // permissions is not granted
    if (shouldAskPermissions(permissions)) {
        // permissions denied previously
        if (shouldShowRequestPermissionsRationale(permissions)) {
            requestPermissionListener.onPermissionRationaleShouldBeShown(getNamePermissionDeny(
                permissions)) {
                requestPermissions(permissions, permissionRequestCode)
            }
        } else {
            // Permission denied or first time requested
            if (isFirstTimeAskingPermissions(permissions)) {
                requestPermissionListener.onCallPermissionFirst {
                    firstTimeAskingPermissions(permissions, false)
                    // request permissions
                    requestPermissions(permissions, permissionRequestCode)
                }
            } else {
                // permission disabled
                // Handle the feature without permission or ask user to manually allow permission
                requestPermissionListener.onPermissionPermanentlyDenied(getNamePermissionDeny(
                    permissions)) {
                    openAppDetailSettings()
                }
            }
        }
    } else {
        // permission granted
        requestPermissionListener.onPermissionGranted()
    }
}

/**
 * request permissions in fragment
 */
fun <T : Fragment> T.requestPermissions(
    permissions: Array<String>,
    permissionRequestCode: Int,
    requestPermissionListener: RequestPermissionListener,
) {
    val context = context ?: return

    // permissions is not granted
    if (context.shouldAskPermissions(permissions)) {
        // permissions denied previously
        if (shouldShowRequestPermissionsRationale(permissions)) {

            requestPermissionListener.onPermissionRationaleShouldBeShown(context.getNamePermissionDeny(
                permissions)) {
                requestPermissions(permissions, permissionRequestCode)
            }
        } else {
            // Permission denied or first time requested
            if (context.isFirstTimeAskingPermissions(permissions)) {
                requestPermissionListener.onCallPermissionFirst {
                    context.firstTimeAskingPermissions(permissions, false)
                    // request permissions
                    requestPermissions(permissions, permissionRequestCode)
                }
            } else {
                // permission disabled
                // Handle the feature without permission or ask user to manually allow permission
                requestPermissionListener.onPermissionPermanentlyDenied(context.getNamePermissionDeny(
                    permissions)) {
                    context.openAppDetailSettings()
                }
            }
        }
    } else {
        // permission granted
        requestPermissionListener.onPermissionGranted()
    }
}

/**
 * Callback on various cases on checking permission
 *
 * 1.  Below M, runtime permission not needed. In that case onPermissionGranted() would be called.
 * If permission is already granted, onPermissionGranted() would be called.
 *
 * 2.  Equal and Above M, if the permission is being asked first time onNeedPermission() would be called.
 *
 * 3.  Equal and Above M, if the permission is previously asked but not granted, onPermissionPreviouslyDenied()
 * would be called.
 *
 * 4.  Equal and Above M, if the permission is disabled by device policy or the user checked "Never ask again"
 * check box on previous request permission, onPermissionDisabled() would be called.
 */
interface RequestPermissionListener {
    /**
     * first time asking multiple permissions
     * */
    fun onCallPermissionFirst(requestPermission: () -> Unit)

    /**
     * Callback on permission previously denied
     * should show permission rationale and continue permission request
     */
    fun onPermissionRationaleShouldBeShown(namePermission: String, requestPermission: () -> Unit)

    /**
     * Callback on permission "Never show again" checked and denied
     * should show message and open app setting
     */
    fun onPermissionPermanentlyDenied(namePermission: String, openAppSetting: () -> Unit)

    /**
     * Callback on permission granted
     */
    fun onPermissionGranted()
}

/**
 * handle request permission result with listener in activity
 */
fun <T : Activity> T.handleOnRequestPermissionResult(
    requestPermissionCode: Int,
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray,
    permissionResultListener: PermissionResultListener,
) {
    if (requestPermissionCode == requestCode) {
        if (isGrantedGrantResults(grantResults)) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            permissionResultListener.onPermissionGranted()
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            if (shouldShowRequestPermissionsRationale(permissions)) {
                // permission denied
                permissionResultListener.onPermissionRationaleShouldBeShown(getNamePermissionDeny(
                    permissions)) {
                    requestPermissions(permissions, requestCode)
                }
            } else {
                // permission disabled or never ask again
                permissionResultListener.onPermissionPermanentlyDenied(getNamePermissionDeny(
                    permissions)) {
                    openAppDetailSettings()
                }
            }
        }
    }
}

/**
 * handle request permission result with listener in fragment
 */
fun <T : Fragment> T.handleOnRequestPermissionResult(
    requestPermissionCode: Int,
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray,
    permissionResultListener: PermissionResultListener,
) {
    if (requestPermissionCode == requestCode) {
        if (isGrantedGrantResults(grantResults)) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            permissionResultListener.onPermissionGranted()
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            if (shouldShowRequestPermissionsRationale(permissions)) {
                // permission denied
                permissionResultListener.onPermissionRationaleShouldBeShown(context?.getNamePermissionDeny(
                    permissions) ?: "") {
                    requestPermissions(permissions, requestCode)
                }
            } else {
                // permission disabled or never ask again
                permissionResultListener.onPermissionPermanentlyDenied(context?.getNamePermissionDeny(
                    permissions) ?: "") {
                    context?.openAppDetailSettings()
                }
            }
        }
    }
}

/**
 * request permission result listener
 */
interface PermissionResultListener {
    /**
     * Callback on permission denied
     */
    fun onPermissionRationaleShouldBeShown(namePermission: String, requestPermission: () -> Unit)

    /**
     * Callback on permission "Never show again" checked and denied
     */
    fun onPermissionPermanentlyDenied(namePermission: String, openAppSetting: () -> Unit)

    /**
     * Callback on permission granted
     */
    fun onPermissionGranted()
}