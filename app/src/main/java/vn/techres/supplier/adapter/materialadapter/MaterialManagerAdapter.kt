package vn.techres.supplier.adapter.materialadapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.materialmanager.MaterialActiveFragment
import vn.techres.supplier.fragment.materialmanager.MaterialPauseFragment


class MaterialManagerAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var materialActiveFragment = MaterialActiveFragment()
    private var materialPauseFragment = MaterialPauseFragment()

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.tab_status_material_active)
            1 -> return context.getString(R.string.tab_status_material_pause)
        }
        return ""
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        fragment = when (position) {
            0 -> materialActiveFragment
            1 -> materialPauseFragment
            else -> materialActiveFragment
        }
        return fragment!!
    }
}
