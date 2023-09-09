package vn.techres.supplier.fragment.slidewelcome

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import vn.techres.supplier.R
import vn.techres.supplier.adapter.developersadapter.AdvAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentAdvSlideWelcomeBinding
import vn.techres.supplier.fragment.forgotpassword.LoginFragment
import vn.techres.supplier.fragment.navigate.MainFragment

class AdvSlideWelcomeFragment :
    BaseBindingFragment<FragmentAdvSlideWelcomeBinding>(FragmentAdvSlideWelcomeBinding::inflate) {
    private lateinit var dotsTv: Array<TextView?>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {

        mainActivity!!.setHideHeader(false)
        mainActivity!!.setLinearLayoutVersion(false)

        binding!!.viewpager.adapter = AdvAdapter(mainActivity!!.supportFragmentManager)

        binding!!.viewpager.setCurrentItem(titleArray.count() * 0, false)
        binding!!.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                setDots(position)
            }
        })
        setDots(0)

        binding!!.btnNext.setOnClickListener {
            val currentPage: Int = binding!!.viewpager.currentItem + 1
            if (currentPage == 3) {
                activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.nav_host, MainFragment())?.commitNow()
            } else {
                binding!!.viewpager.currentItem = currentPage
            }
        }

        binding!!.btnSkip.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host, LoginFragment())?.commitNow()
        }

        binding!!.btnBack.setOnClickListener {
            val currentPage: Int = binding!!.viewpager.currentItem - 1
            binding!!.viewpager.currentItem = currentPage
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AdvSlideWelcomeFragment().apply {
            arguments = Bundle().apply { }
        }
    }

    private fun setDots(page: Int) {
        binding!!.dotsLayout.removeAllViews()
        dotsTv = arrayOfNulls(titleArray.count())

        for (i in dotsTv.indices) {
            dotsTv[i] = TextView(context)
            dotsTv[i]!!.text = Html.fromHtml("&#8226;")
            dotsTv[i]!!.textSize = 30f
            dotsTv[i]!!.setTextColor(Color.parseColor("#a9b4bb"))
            binding!!.dotsLayout.addView(dotsTv[i])
        }
        if (dotsTv.isNotEmpty()) {
            dotsTv[page]!!.setTextColor(Color.parseColor("#FFA233"))
        }
    }

    override fun onBackPress() {
    }
}