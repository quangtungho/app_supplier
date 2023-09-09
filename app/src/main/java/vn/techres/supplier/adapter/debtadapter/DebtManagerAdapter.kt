package vn.techres.supplier.adapter.debtadapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.debtmanager.DebtReceivableFragment
import vn.techres.supplier.fragment.debtmanager.DebtToPayFragment


class DebtManagerAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var debtReceivableFragment = DebtReceivableFragment()
    private var debtToPayFragment = DebtToPayFragment()

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.debtReceivableFragment)
            1 -> return context.getString(R.string.debtToPayFragment)
        }
        return ""
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        fragment = when (position) {
            0 -> debtReceivableFragment
            1 -> debtToPayFragment
            else -> debtReceivableFragment
        }
        return fragment!!
    }
}
