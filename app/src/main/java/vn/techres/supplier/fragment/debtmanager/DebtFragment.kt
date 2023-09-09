package vn.techres.supplier.fragment.debtmanager

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.adapter.debtadapter.DebtManagerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentDebtBinding

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
open class DebtFragment : BaseBindingFragment<FragmentDebtBinding>(FragmentDebtBinding::inflate) {
    private var adapter: DebtManagerAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        mainActivity!!.setLoading(false)
        mainActivity!!.setHideHeader(false)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        mainActivity!!.setHeader(getString(R.string.debt_manager))
        adapter = DebtManagerAdapter(childFragmentManager, mainActivity!!)
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        binding!!.viewPager.adapter = adapter
        binding!!.tabLayout.setupWithViewPager(binding!!.viewPager)
    }
    companion object {
        @JvmStatic
        fun newInstance() = DebtFragment().apply {
            arguments = Bundle().apply { }
        }
    }

    override fun onBackPress() {
    }
}