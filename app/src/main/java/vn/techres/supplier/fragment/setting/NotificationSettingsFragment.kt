package vn.techres.supplier.fragment.setting

import android.os.Bundle
import android.view.View
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentNotificationSettingsBinding


class NotificationSettingsFragment :
    BaseBindingFragment<FragmentNotificationSettingsBinding>(FragmentNotificationSettingsBinding::inflate) {
    val tagName: String = NotificationSettingsFragment::class.java.name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        mainActivity!!.setHeader(getString(R.string.txt_notificationSetting))
    }

    /**
     * onBack
     */
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}
