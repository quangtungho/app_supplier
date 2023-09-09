package vn.techres.supplier.fragment.revenuemanagement.genusitems.receipts

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.adapter.revenueadapter.ReceiptsManagerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentMaterialUnitsManagerBinding
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class ReceiptsManagerFragment :
    BaseBindingFragment<FragmentMaterialUnitsManagerBinding>(FragmentMaterialUnitsManagerBinding::inflate) {
    val tagName: String = ReceiptsManagerFragment::class.java.name
    private var adapter: ReceiptsManagerAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setHeader(getString(R.string.receipts_items))
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        adapter = ReceiptsManagerAdapter(childFragmentManager, mainActivity!!)
        binding!!.viewPagerUnits.adapter = adapter
        binding!!.tabLayoutUnits.setupWithViewPager(binding!!.viewPagerUnits)
    }
    override fun onBackPress() {
    }
}