package vn.techres.supplier.fragment.setting

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentLanguageBinding
import vn.techres.supplier.helper.TechresEnum

class LanguageFragment :
    BaseBindingFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {
    val tagName: String = LanguageFragment::class.java.name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        mainActivity!!.setHeader(getString(R.string.txt_Language))
        buttonVietnam()
        buttonEngland()
    }

    /**
     * buttonEngland()
     */
    private fun buttonEngland() {
        binding!!.btnButtonEngland.setOnClickListener {
            mainActivity!!.setLoading(true)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    binding!!.imgTickVietnam.visibility = View.GONE
                    binding!!.imgTickEngland.visibility = View.VISIBLE
                    mainActivity!!.setLoading(false)
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * buttonVietnam()
     */
    private fun buttonVietnam() {
        binding!!.btnButtonVietnam.setOnClickListener {
            mainActivity!!.setLoading(true)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    binding!!.imgTickEngland.visibility = View.GONE
                    binding!!.imgTickVietnam.visibility = View.VISIBLE
                    mainActivity!!.setLoading(false)
                }, TechresEnum.TIME_100.toString().toLong()
            )

        }
    }

    /**
     * onBack
     */
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}