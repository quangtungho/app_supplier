package vn.techres.supplier.fragment.setting

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentNationBinding

class NationFragment : BaseBindingFragment<FragmentNationBinding>(FragmentNationBinding::inflate) {
    val tagName: String = NationFragment::class.java.name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        mainActivity!!.setHeader(getString(R.string.txt_Nation))
        mainActivity!!.setBackClick(true)
    }

    /**
     * onBack
     */
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}