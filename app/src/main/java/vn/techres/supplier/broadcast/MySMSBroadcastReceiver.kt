package vn.techres.supplier.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import org.greenrobot.eventbus.EventBus
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.model.eventbussms.EvenBusSMS

class MySMSBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents
                    val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    EventBus.getDefault().post(EvenBusSMS(message!!))
                    Toast.makeText(SupplierApplication.context, message, Toast.LENGTH_SHORT).show()
                }
                CommonStatusCodes.TIMEOUT -> {}
            }
        }
    }
}