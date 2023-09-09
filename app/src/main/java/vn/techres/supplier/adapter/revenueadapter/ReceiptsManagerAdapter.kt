package vn.techres.supplier.adapter.revenueadapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.revenuemanagement.genusitems.receipts.InReceiptsFragment
import vn.techres.supplier.fragment.revenuemanagement.genusitems.receipts.ReceiptsFragment


class ReceiptsManagerAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var receiptsFragment = ReceiptsFragment()
    private var inReceiptsFragment = InReceiptsFragment()

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.txt_processing)
            1 -> return context.getString(R.string.txt_completed)
        }
        return ""
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        fragment = when (position) {
            0 -> receiptsFragment
            1 -> inReceiptsFragment
            else -> receiptsFragment
        }
        return fragment!!
    }
}
