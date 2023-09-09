package vn.techres.supplier.activity

import android.view.LayoutInflater
import vn.techres.supplier.R
import vn.techres.supplier.adapter.chatadapter.archiveadapter.ArchiveMainAdapter
import vn.techres.supplier.base.BaseBindingActivity
import vn.techres.supplier.databinding.ArchiveActivityBinding
import vn.techres.supplier.helper.TechresEnum
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class ArchiveMainChatActivity : BaseBindingActivity<ArchiveActivityBinding>() {
    override val bindingInflater: (LayoutInflater) -> ArchiveActivityBinding
        get() = ArchiveActivityBinding::inflate
    private var groupID = ""
    private var adapter: ArchiveMainAdapter? = null

    override fun onSetBodyView() {
        groupID = intent.getStringExtra(TechresEnum.ID_GROUP.toString())!!
        binding.header.toolbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.header.toolbarTitle.text = getString(R.string.archive)
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
        adapter = ArchiveMainAdapter(supportFragmentManager, this)
        binding.viewPagerArchive.adapter = adapter
        binding.tabLayoutArchive.setupWithViewPager(binding.viewPagerArchive)
    }

}