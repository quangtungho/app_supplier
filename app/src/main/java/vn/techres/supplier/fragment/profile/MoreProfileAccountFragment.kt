package vn.techres.supplier.fragment.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentMoreProfileBinding
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.entity.User
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class MoreProfileAccountFragment :
    BaseBindingFragment<FragmentMoreProfileBinding>(FragmentMoreProfileBinding::inflate) {
    val tagName: String = MoreProfileAccountFragment::class.java.name
    private var user = User()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        //onCreate
        fragmentCreate()

        //onClick back
        binding!!.btnBack.setOnClickListener {
            onBackPress()
        }

        //onClick edit avatar
        binding!!.btnNewPicMore.setOnClickListener {
            openDialogAvatar()
        }

        //Intent InfoProfile fragment
        onClickFragmentInfoProfile()

        //Intent EditProfile fragment
        onClickFragmentEditProfile()
    }
    private fun fragmentCreate() {
        mainActivity!!.setHideHeader(false)

        user = CurrentUser.getCurrentUser(mainActivity!!.baseContext)!!
        binding!!.txtNameUserMore.text = user.name

        if (cacheManager.get(TechresEnum.KEY_BACK.toString()) == TechresEnum.KEY_BACK.toString()) {
            cacheManager.put(TechresEnum.KEY_BACK.toString(), TechresEnum.NULL.toString())
            val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
            binding!!.constrainLayoutMain.startAnimation(animFragment)
        } else {
            val animFragment = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
            binding!!.constrainLayoutMain.startAnimation(animFragment)
        }
    }
    private fun onClickFragmentInfoProfile() {
        binding!!.btnInfoMore.setOnClickListener {
            val infoProfileFragment = InfoProfileFragment()

            val animFragmentHide = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_hide)
            binding!!.constrainLayoutMain.startAnimation(animFragmentHide)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, InfoProfileFragment())
                        ?.addToBackStack(infoProfileFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }
    }
    private fun onClickFragmentEditProfile() {
        binding!!.btnUpdateInfoMore.setOnClickListener {
            val editProfileFragment = EditProfileFragment()

            val animFragmentHide = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_hide)
            binding!!.constrainLayoutMain.startAnimation(animFragmentHide)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, EditProfileFragment())
                        ?.addToBackStack(editProfileFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }
    }
    /**
     * Dialog avatar:
     * 1. Take a new photo
     * 2. Select a photo from the device
     * 3. Choose an available photo
     */
    private fun openDialogAvatar() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_newavatar_profile)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val linearDialogViewable = dialog.findViewById<LinearLayout>(R.id.linearDialogViewavatar)
        linearDialogViewable!!.visibility = View.GONE
        dialog.show()
    }
    override fun onBackPress() {
        val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
        binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                cacheManager.put(TechresEnum.KEY_BACK.toString(), TechresEnum.KEY_BACK.toString())
                mainActivity!!.supportFragmentManager.popBackStack()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}