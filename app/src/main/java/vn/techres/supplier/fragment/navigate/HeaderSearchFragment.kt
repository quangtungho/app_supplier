package vn.techres.supplier.fragment.navigate

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentHeaderSearchBinding
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class HeaderSearchFragment :
    BaseBindingFragment<FragmentHeaderSearchBinding>(FragmentHeaderSearchBinding::inflate) {
    val tagName: String = HeaderSearchFragment::class.java.name
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setLoading(false)
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        mainActivity!!.setHideHeader(false)
        binding!!.btnBack.setOnClickListener {
            onBackPress()
        }

    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}