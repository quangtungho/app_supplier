package vn.techres.supplier.adapter.ordermanageradapterr

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.ordermanager.OrderCompletedFragment
import vn.techres.supplier.fragment.ordermanager.OrderProcessingFragment
import vn.techres.supplier.fragment.ordermanager.OrderReturnsFragment
import vn.techres.supplier.fragment.ordermanager.OrderSynthesiseFragment


class OrderManagerAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var tabProcessing = OrderProcessingFragment()
    private var tabCompleted = OrderCompletedFragment()
    private var tabReturns = OrderReturnsFragment()
    private var tabSynthesise = OrderSynthesiseFragment()

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.txt_processing_order)
            1 -> return context.getString(R.string.txt_completed_order)
            2 -> return context.getString(R.string.txt_returns_order)
            3 -> return context.getString(R.string.tab_synthesise)
        }
        return ""
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        fragment = when (position) {
            0 -> tabProcessing
            1 -> tabCompleted
            2 -> tabReturns
            3 -> tabSynthesise
            else -> tabProcessing
        }
        return fragment!!
    }
}
