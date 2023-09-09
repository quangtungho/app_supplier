package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.chatmanager.DiaryFragment
import vn.techres.supplier.fragment.chatmanager.PhonebookFragment


@SuppressLint("WrongConstant")
class ContractAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var phonebookFragment = PhonebookFragment()
    private var diaryFragment = DiaryFragment()


    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.phonebook)
            1 -> return context.getString(R.string.call_logs)

        }
        return ""
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        fragment = when (position) {
            0 -> phonebookFragment
            1 -> diaryFragment
            else -> phonebookFragment
        }
        return fragment!!
    }
}
