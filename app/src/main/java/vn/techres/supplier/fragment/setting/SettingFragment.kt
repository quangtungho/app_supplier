package vn.techres.supplier.fragment.setting

import android.os.Bundle
import android.view.View
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentSettingBinding

class SettingFragment :
    BaseBindingFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    val tagName: String = SettingFragment::class.java.name


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        fragmentCreate()
        onClickFragmentLanguage()
        onClickFragmentNotify()
        onClickFragmentFingerprint()
    }

    /**
     *  onCreate
     */
    private fun fragmentCreate() {
        mainActivity!!.setHeader(getString(R.string.text_setting_title))
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
    }

    /**
     *  Intent Language fragment
     */
    private fun onClickFragmentLanguage() {
        val languageFragment = LanguageFragment()
        binding!!.linearLanguageSetting.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.nav_host, LanguageFragment())
                ?.addToBackStack(languageFragment.tagName)
                ?.commit()
        }
    }

    private fun onClickFragmentNotify() {
        val notificationSettingFragment = NotificationSettingsFragment()
        binding!!.linearNotifySetting.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.nav_host, NotificationSettingsFragment())
                ?.addToBackStack(notificationSettingFragment.tagName)
                ?.commit()
        }
    }

    /**
     *  Intent Language fragment
     */
    private fun onClickFragmentFingerprint() {
        val fingerprintFragment = SettingFingerprintFragment()
        binding!!.linearFingerprint.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.nav_host, SettingFingerprintFragment())
                ?.addToBackStack(fingerprintFragment.tagName)
                ?.commit()
        }
    }

    /**
     * onBack
     */
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}