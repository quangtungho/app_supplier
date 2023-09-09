package vn.techres.supplier.fragment.ordermanager

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import vn.techres.supplier.R
import vn.techres.supplier.adapter.ordermanageradapterr.OrderManagerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentOrderManagerBinding
import vn.techres.supplier.helper.TechresEnum

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class OrderManagerFragment :
    BaseBindingFragment<FragmentOrderManagerBinding>(FragmentOrderManagerBinding::inflate) {
    val tagName: String = OrderManagerFragment::class.java.name
    private var adapter: OrderManagerAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setLoading(false)
        adapter = OrderManagerAdapter(childFragmentManager, mainActivity!!)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        mainActivity!!.setBackClick(false)
        mainActivity!!.setHideHeader(false)
        mainActivity!!.setHeader(getString(R.string.btn_order))
        binding!!.viewPageChat.adapter = adapter
        binding!!.tabLayout.setupWithViewPager(binding!!.viewPageChat)


    }
    override fun onResume() {
        super.onResume()
        when {
            cacheManager.get(TechresEnum.KEY_TAB_ORDER_BILL.toString())
                ?.toInt() == 1 -> binding!!.viewPageChat.currentItem = 1
            cacheManager.get(TechresEnum.KEY_TAB_ORDER_BILL.toString())
                ?.toInt() == 2 -> binding!!.viewPageChat.currentItem = 2
            cacheManager.get(TechresEnum.KEY_TAB_ORDER_BILL.toString())
                ?.toInt() == 3 -> binding!!.viewPageChat.currentItem = 3
            else -> {
                binding!!.viewPageChat.currentItem = 0
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = OrderManagerFragment().apply {
            arguments = Bundle().apply { }
        }
    }
    override fun onBackPress() {
    }
}