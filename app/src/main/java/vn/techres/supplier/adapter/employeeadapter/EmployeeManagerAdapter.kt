package vn.techres.supplier.adapter.employeeadapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.employeemanager.TabEmployeeOffFragment
import vn.techres.supplier.fragment.employeemanager.TabEmployeeOnFragment


class EmployeeManagerAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var tabStaffOnFragment = TabEmployeeOnFragment()
    private var tabStaffOffFragment = TabEmployeeOffFragment()

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
            0 -> tabStaffOnFragment
            1 -> tabStaffOffFragment
            else -> tabStaffOnFragment
        }
        return fragment!!
    }
}
