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
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentInfoProfileBinding
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.entity.CurrentConfigJava
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.entity.User
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class InfoProfileFragment :
    BaseBindingFragment<FragmentInfoProfileBinding>(FragmentInfoProfileBinding::inflate) {
    val tagName: String = InfoProfileFragment::class.java.name
    private var user = User()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        //onCreate
        fragmentCreate()

        val editProfileFragment = EditProfileFragment()

        //onClick avatar
        binding!!.avatarProfile.setOnClickListener {
            openDialogAvatar()
        }

        //onClick image card
        binding!!.linearCardImageProfile.setOnClickListener {
            openDialogCardImage()
        }

        binding!!.btnBack.setOnClickListener {
            onBackPress()
        }

        binding!!.btnEditInfoProfile.setOnClickListener {
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
    private fun fragmentCreate() {
        mainActivity!!.setHideHeader(false)
        user = CurrentUser.getCurrentUser(mainActivity!!.baseContext)!!
        AppUtils.callPhotoAvatar(user.avatar, binding!!.imgAccountDetail)
        binding!!.txtNameUserInfoprofile.text = user.name
        binding!!.txtPhoneInfoprofile.text = user.phone
        binding!!.txtEmailInfoprofile.text = user.email
        binding!!.txtAddressInfoprofile.text = user.address
        binding!!.txtRolesInfoprofile.text = user.supplier_employee_position

        if (cacheManager.get(TechresEnum.KEY_BACK.toString()) == TechresEnum.KEY_BACK.toString()) {
            cacheManager.put(TechresEnum.KEY_BACK.toString(), TechresEnum.NULL.toString())
            val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
            binding!!.constrainLayoutMain.startAnimation(animFragment)
        } else {
            val animFragment = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
            binding!!.constrainLayoutMain.startAnimation(animFragment)
        }
    }
    /**
     * Dialog avatar:
     * 1. View full avatar
     * 2. Take a new photo
     * 3. Select a photo from the device
     * 4. Choose an available photo
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
        val btnAddPictureFromDevice =
            dialog.findViewById<LinearLayout>(R.id.btnAddPictureFromDevice)

        linearDialogViewable!!.setOnClickListener {
            openDialogViewAvatar()
            dialog.dismiss()
        }

        btnAddPictureFromDevice!!.setOnClickListener {
            val addNewPhotoFragment = AddNewPhotoFragment()
            cacheManager.put(
                TechresEnum.KEY_BACK.toString(),
                TechresEnum.INFOPROFILEACCOUNT_ID_FRAGMENT.toString()
            )

            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.nav_host, AddNewPhotoFragment())
                ?.addToBackStack(addNewPhotoFragment.tagName)
                ?.commit()

            dialog.dismiss()
        }

        dialog.show()
    }
    //Dialog view full avatar
    private fun openDialogViewAvatar() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_showavatar_profile)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val imgDialogAvatar = dialog.findViewById<ImageView>(R.id.imgDialogAvatar)

        Glide
            .with(mainActivity!!)
            .load(
                String.format(
                    "%s%s",
                    CurrentConfigJava.getConfigJava(mainActivity!!).ads_domain,
                    user.avatar
                )
            )
            .centerCrop()
            .into(imgDialogAvatar!!)

        dialog.show()
    }
    /**
     * Dialog image card:
     * 1. View full image card
     * 2. Take a new photo
     * 3. Select a photo from the device
     * 4. Choose an available photo
     */
    private fun openDialogCardImage() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_cardimage_profile)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val linearDialogViewpager =
            dialog.findViewById<LinearLayout>(R.id.linearDialogViewcardimg)

        linearDialogViewpager!!.setOnClickListener {
            openDialogViewCardImage()
            dialog.dismiss()
        }

        dialog.show()
    }
    //Dialog view full image card
    private fun openDialogViewCardImage() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_showcardimage_profile)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
    override fun onBackPress() {
        val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
        binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
        cacheManager.put(TechresEnum.KEY_BACK.toString(), TechresEnum.KEY_BACK.toString())
        Handler(Looper.getMainLooper()).postDelayed(
            {
                mainActivity!!.supportFragmentManager.popBackStack()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}