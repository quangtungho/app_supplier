package vn.techres.supplier.fragment.collectionfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentEntertainmentBinding
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class IntroduceFragment :
    BaseBindingFragment<FragmentEntertainmentBinding>(FragmentEntertainmentBinding::inflate) {
    val tagName: String = IntroduceFragment::class.java.name
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constraintLayoutMain.startAnimation(animFragmentNext)
        mainActivity!!.setHeader(getString(R.string.txt_entertainment))
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        onTms()
        onAloLine()
    }
    private fun onTms() {
        binding!!.linearTmsAccountmenu.setOnClickListener {
            val uri =
                Uri.parse(getString(R.string.app_tms)) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
    private fun onAloLine() {
        binding!!.linearAlolineAccountmenu.setOnClickListener {
            val uri =
                Uri.parse(getString(R.string.supplier_link_aloline)) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}