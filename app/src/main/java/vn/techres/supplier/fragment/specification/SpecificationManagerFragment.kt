package vn.techres.supplier.fragment.specification

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.adapter.specificationadapter.SpecificationManagerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentSpecificationManagerBinding

class SpecificationManagerFragment :
    BaseBindingFragment<FragmentSpecificationManagerBinding>(FragmentSpecificationManagerBinding::inflate) {
    val tagName: String = SpecificationManagerFragment::class.java.name
    var adapter: SpecificationManagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        mainActivity!!.setHeader(getString(R.string.title_specification_manager))
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        adapter = SpecificationManagerAdapter(childFragmentManager, mainActivity!!)
        binding!!.viewPagerSpecification.adapter = adapter
        binding!!.tabLayoutSpecification.setupWithViewPager(binding!!.viewPagerSpecification)
    }

    override fun onBackPress() {
    }
}