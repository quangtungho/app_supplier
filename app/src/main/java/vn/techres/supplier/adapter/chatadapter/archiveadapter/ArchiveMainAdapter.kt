package vn.techres.supplier.adapter.chatadapter.archiveadapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.techres.supplier.R
import vn.techres.supplier.fragment.chatmanager.archivemain.FileFragment
import vn.techres.supplier.fragment.chatmanager.archivemain.LinkFragment
import vn.techres.supplier.fragment.chatmanager.archivemain.MediaGroupFragment
import vn.techres.supplier.fragment.chatmanager.archivemain.VideoGroupFragment


@SuppressLint("WrongConstant")
class ArchiveMainAdapter(fr: FragmentManager?, private val context: Context) :
    FragmentPagerAdapter(fr!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragment: Fragment? = null
    private var mediaGroupFragment = MediaGroupFragment()
    private var videoGroupFragment = VideoGroupFragment()
    private var linkFragment = LinkFragment()
    private var fileFragment = FileFragment()

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.media_group)
            1 -> return context.getString(R.string.video_group)
            2 -> return context.getString(R.string.link)
            3 -> return context.getString(R.string.file)
        }
        return ""
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        fragment = when (position) {
            0 -> mediaGroupFragment
            1 -> videoGroupFragment
            2 -> linkFragment
            3 -> fileFragment
            else -> mediaGroupFragment
        }
        return fragment!!
    }
}
