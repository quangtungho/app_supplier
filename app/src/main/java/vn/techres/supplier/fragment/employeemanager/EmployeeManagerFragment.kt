package vn.techres.supplier.fragment.employeemanager

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.adapter.employeeadapter.EmployeeManagerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentEmployeeManagerBinding
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class EmployeeManagerFragment :
    BaseBindingFragment<FragmentEmployeeManagerBinding>(FragmentEmployeeManagerBinding::inflate) {
    val tagName: String = EmployeeManagerFragment::class.java.name
    private var adapter: EmployeeManagerAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        adapter = EmployeeManagerAdapter(childFragmentManager, mainActivity!!)
        mainActivity!!.setHeader(getString(R.string.txt_EmployeeManagement))
        mainActivity!!.setBackClick(true)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        binding!!.viewPagerNV.adapter = adapter
        binding!!.tablayoutNV.setupWithViewPager(binding!!.viewPagerNV)
    }
    override fun onBackPress() {
    }
}