package vn.techres.supplier.adapter.customeradapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.restaurantcontactors.BranchFragment
import vn.techres.supplier.fragment.restaurantcontactors.ContactorsFragment
import vn.techres.supplier.fragment.restaurantcontactors.DebtRestaurantFragment


class ContactCustomerAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var tabBranchFragment = BranchFragment()
    private var tabContactFragment = ContactorsFragment()
    private var tabDebtFragment = DebtRestaurantFragment()

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.text_branch)
            1 -> return context.getString(R.string.title_debt)
            2 -> return context.getString(R.string.text_contact)
        }
        return ""
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        fragment = when (position) {
            0 -> tabBranchFragment
            1 -> tabDebtFragment
            2 -> tabContactFragment
            else -> tabBranchFragment
        }
        return fragment!!
    }
}
