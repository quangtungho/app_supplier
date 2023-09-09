package vn.techres.supplier.fragment.inventorymanagement

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.adapter.inventoryadapter.InventoryManagerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentInventoryManagementBinding

class InventoryManagementFragment :
    BaseBindingFragment<FragmentInventoryManagementBinding>(FragmentInventoryManagementBinding::inflate) {
    val tagName: String = InventoryManagementFragment::class.java.name

    private var adapter: InventoryManagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        adapter = InventoryManagerAdapter(childFragmentManager, mainActivity!!)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        mainActivity!!.setHeader(getString(R.string.txt_Inventory))
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        binding!!.viewPager.adapter = adapter
        binding!!.tabLayout.setupWithViewPager(binding!!.viewPager)

    }

    override fun onBackPress() {
    }
}
