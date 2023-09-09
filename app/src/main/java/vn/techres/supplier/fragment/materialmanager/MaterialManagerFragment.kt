package vn.techres.supplier.fragment.materialmanager

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.adapter.materialadapter.MaterialManagerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentMaterialManagerBinding
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class MaterialManagerFragment : BaseBindingFragment<FragmentMaterialManagerBinding>(FragmentMaterialManagerBinding::inflate) {
    val tagName: String = MaterialManagerFragment::class.java.name
    private var adapter: MaterialManagerAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        adapter = MaterialManagerAdapter(childFragmentManager, mainActivity!!)
        mainActivity!!.setHeader(getString(R.string.btn_material))
        mainActivity!!.setBackClick(true)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        binding!!.viewPagerMaterial.adapter = adapter
        binding!!.tablayoutMaterial.setupWithViewPager(binding!!.viewPagerMaterial)

    }
    override fun onBackPress() {}

}