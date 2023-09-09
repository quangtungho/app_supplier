package vn.techres.supplier.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import org.greenrobot.eventbus.EventBus
import vn.techres.supplier.R
import vn.techres.supplier.adapter.chatadapter.ContractAdapter
import vn.techres.supplier.base.BaseBindingActivity
import vn.techres.supplier.databinding.ContractActivityBinding
import vn.techres.supplier.model.eventbussms.EventBusPermission

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class ContractActivity : BaseBindingActivity<ContractActivityBinding>() {
    override val bindingInflater: (LayoutInflater) -> ContractActivityBinding
        get() = ContractActivityBinding::inflate
    private var adapter: ContractAdapter? = null
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    override fun onSetBodyView() {
        binding.imvBack.setOnClickListener {
            onBackPressed()
        }
        onTabLayout()
    }
    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
    /**
     * Xu ly du lieu TabLayout
     */
    private fun onTabLayout() {
        adapter = ContractAdapter(supportFragmentManager, this)
        binding.viewPagerArchive.adapter = adapter
        binding.tabLayoutArchive.setupWithViewPager(binding.viewPagerArchive)
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            Handler().postDelayed({
                EventBus.getDefault().post(EventBusPermission(0))
            }, 2000)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {

//                        binding.loading.loadingData.setVisibility(View.VISIBLE);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                    EventBus.getDefault().post(EventBusPermission(0))
            } else {
//                Toast.makeText(
//                    this,
//                    getString(R.string.notification_permission),
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }
}