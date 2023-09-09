package vn.techres.supplier.adapter.unitsadapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.units.UnitsActiveFragment
import vn.techres.supplier.fragment.units.UnitsPauseFragment


class UnitsManagerAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var unitsActiveFragment = UnitsActiveFragment()
    private var unitsPauseFragment = UnitsPauseFragment()

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.tab_status_active)
            1 -> return context.getString(R.string.tab_status_pause)
        }
        return ""
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        fragment = when (position) {
            0 -> unitsActiveFragment
            1 -> unitsPauseFragment
            else -> unitsActiveFragment
        }
        return fragment!!
    }
}
