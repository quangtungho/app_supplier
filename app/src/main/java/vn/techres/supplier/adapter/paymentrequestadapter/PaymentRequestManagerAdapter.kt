package vn.techres.supplier.adapter.paymentrequestadapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.paymentrequest.PaidFragment
import vn.techres.supplier.fragment.paymentrequest.WaitForPayFragment
import vn.techres.supplier.fragment.units.UnitsActiveFragment
import vn.techres.supplier.fragment.units.UnitsPauseFragment


class PaymentRequestManagerAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var waitForPayFragment = WaitForPayFragment()
    private var paidFragment = PaidFragment()

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.wait_for_payment)
            1 -> return context.getString(R.string.txt_done_debt)
        }
        return ""
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        fragment = when (position) {
            0 -> waitForPayFragment
            1 -> paidFragment
            else -> waitForPayFragment
        }
        return fragment!!
    }
}
