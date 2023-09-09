package vn.techres.supplier.adapter.developersadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import vn.techres.supplier.fragment.slidewelcome.backgroundColorArray
import vn.techres.supplier.fragment.slidewelcome.AdvSlideFragment
import vn.techres.supplier.fragment.slidewelcome.resourceArray
import vn.techres.supplier.fragment.slidewelcome.titleArray

class AdvAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return AdvSlideFragment.newInstance(
            backgroundColorArray[(position % titleArray.count())],
            resourceArray[(position % titleArray.count())],
            titleArray[(position % titleArray.count())]
        )
    }

    override fun getCount(): Int {
        return titleArray.count()
    }
}