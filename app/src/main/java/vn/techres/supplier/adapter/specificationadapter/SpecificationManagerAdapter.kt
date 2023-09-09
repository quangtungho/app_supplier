package vn.techres.supplier.adapter.specificationadapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.specification.SpecificationActiveFragment
import vn.techres.supplier.fragment.specification.SpecificationPauseFragment


class SpecificationManagerAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var specificationActiveFragment = SpecificationActiveFragment()
    private var specificationPauseFragment = SpecificationPauseFragment()

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
            0 -> specificationActiveFragment
            1 -> specificationPauseFragment
            else -> specificationActiveFragment
        }
        return fragment!!
    }
}
