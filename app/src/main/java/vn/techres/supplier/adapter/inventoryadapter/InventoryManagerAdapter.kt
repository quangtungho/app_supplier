package vn.techres.supplier.adapter.inventoryadapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.inventorymanagement.ExportFragment
import vn.techres.supplier.fragment.inventorymanagement.WareHouseFragment


class InventoryManagerAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var wareHouseFragment = WareHouseFragment()
    private var exportFragment = ExportFragment()

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.title_warehouse)
            1 -> return context.getString(R.string.title_wexport)
        }
        return ""
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        fragment = when (position) {
            0 -> wareHouseFragment
            1 -> exportFragment
            else -> wareHouseFragment
        }
        return fragment!!
    }
}
