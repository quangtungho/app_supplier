package vn.techres.supplier.fragment.units

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.adapter.unitsadapter.UnitsManagerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentMaterialUnitsManagerBinding

class UnitsManagerFragment :
    BaseBindingFragment<FragmentMaterialUnitsManagerBinding>(FragmentMaterialUnitsManagerBinding::inflate) {
    val tagName: String = UnitsManagerFragment::class.java.name
    private var adapter: UnitsManagerAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        mainActivity!!.setHeader(getString(R.string.title_units_manager))
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        adapter = UnitsManagerAdapter(childFragmentManager, mainActivity!!)

        binding!!.viewPagerUnits.adapter = adapter
        binding!!.tabLayoutUnits.setupWithViewPager(binding!!.viewPagerUnits)
    }

    override fun onBackPress() {
    }
}