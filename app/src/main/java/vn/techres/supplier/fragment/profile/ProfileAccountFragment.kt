package vn.techres.supplier.fragment.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentProfileAccountBinding
import vn.techres.supplier.fragment.navigate.MainFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.entity.User
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.UserResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class ProfileAccountFragment :
    BaseBindingFragment<FragmentProfileAccountBinding>(FragmentProfileAccountBinding::inflate) {
    val tagName: String = ProfileAccountFragment::class.java.name
    private var user = User()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        //onCreate
        fragmentCreate()

        //Intent MoreProfile fragment
        onClickFragmentMoreProfile()

        //Intent EditProfile fragment
        onClickFragmentEditProfile()

        //onClick back
        binding!!.btnBack.setOnClickListener {
            onBackPress()
        }
        //onClick back
        binding!!.btnBack2.setOnClickListener {
            onBackPress()
        }

        //Show dialog avatar
        binding!!.avatarProfile.setOnClickListener {
            openDialogAvatar()
        }
        //Show dialog avatar
        binding!!.avatarProfile2.setOnClickListener {
            openDialogAvatar()
        }
        //Show dialog image card
        binding!!.rlCardImageProfile.setOnClickListener {
            openDialogCardImage()
        }
        binding!!.txtAddressProfile.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding!!.txtAddressProfile.marqueeRepeatLimit = -1
        binding!!.txtAddressProfile.isSelected = true
    }
    override fun onResume() {
        super.onResume()
        user = CurrentUser.getCurrentUser(mainActivity!!.baseContext)!!
        getUser()
    }
    private fun fragmentCreate() {
        mainActivity!!.setHideHeader(false)

        if (cacheManager.get(TechresEnum.KEY_BACK.toString()) == TechresEnum.KEY_BACK.toString()) {
            cacheManager.put(TechresEnum.KEY_BACK.toString(), TechresEnum.NULL.toString())
            val animFragment = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_show)
            binding!!.constrainLayoutMain.startAnimation(animFragment)
        } else {
            val animFragment = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
            binding!!.constrainLayoutMain.startAnimation(animFragment)
        }
    }
    private fun getUser() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees/" + CurrentUser.getCurrentUser(mainActivity!!)!!.id

        ServiceFactory.createRetrofitService(
            TechResService::class.java, mainActivity!!
        )
            .getUser(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UserResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mainActivity!!.setLoading(false)
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: UserResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        val user = response.data
                        saveUser(user)
                        mainActivity!!.setLoading(false)
                    } else {
                        mainActivity!!.setLoading(false)
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    fun saveUser(userResponse: User?) {
        //Lưu lại các dữ liệu của user sau khi getAPI user thành công
        val user = CurrentUser.getCurrentUser(requireActivity().baseContext)
        user?.let {
            it.name = userResponse?.name + ""
            it.phone = userResponse?.phone + ""
            it.address = userResponse?.address + ""
            it.email = userResponse?.email + ""
            it.supplier_employee_position = userResponse?.supplier_employee_position + ""
            it.avatar = userResponse?.avatar + ""
            it.supplier_role_id = userResponse?.supplier_role_id!!.toInt()
        }
        CurrentUser.saveUserInfo(mainActivity!!.baseContext, user)

        //Gán thông tin user lên giao diện
        AppUtils.callPhotoAvatar(user!!.avatar, binding!!.imgAccountDetail)

        AppUtils.callPhotoAvatar(user.avatar, binding!!.imgAccountDetail2)
        binding!!.txtNameUserProfile.text = user.name
        binding!!.txtNameUserProfile2.text = user.name
        binding!!.txtAddressProfile.text = user.address
        binding!!.txtEmailProfile.text = user.email
        binding!!.txtPhoneProfile.text = user.phone
        binding!!.txtRoles.text = user.supplier_employee_position
    }
    private fun onClickFragmentMoreProfile() {
        val moreProfileFragment = MoreProfileAccountFragment()

        binding!!.btnMoreProfile.setOnClickListener {
            val animFragmentHide = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_hide)
            binding!!.constrainLayoutMain.startAnimation(animFragmentHide)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, MoreProfileAccountFragment())
                        ?.addToBackStack(moreProfileFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }

        binding!!.btnMoreProfile2.setOnClickListener {
            val animFragmentHide = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_hide)
            binding!!.constrainLayoutMain.startAnimation(animFragmentHide)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, MoreProfileAccountFragment())
                        ?.addToBackStack(moreProfileFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }
    }
    private fun onClickFragmentEditProfile() {
        val editProfileFragment = EditProfileFragment()

        binding!!.btnEditProfile.setOnClickListener {
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

        val linearDialogViewAvatar = dialog.findViewById<LinearLayout>(R.id.linearDialogViewavatar)
        val btnAddPictureFromDevice =
            dialog.findViewById<LinearLayout>(R.id.btnAddPictureFromDevice)

        linearDialogViewAvatar!!.setOnClickListener {
            openDialogViewAvatar()
            dialog.dismiss()
        }

        btnAddPictureFromDevice!!.setOnClickListener {
            val addNewPhotoFragment = AddNewPhotoFragment()
            cacheManager.put(
                TechresEnum.KEY_BACK.toString(),
                TechresEnum.ACCOUNTMENU_ID_FRAGMENT.toString()
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


        AppUtils.callPhotoAvatar(user.avatar, imgDialogAvatar)

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

        val linearDialogViewCardImg =
            dialog.findViewById<LinearLayout>(R.id.linearDialogViewcardimg)

        linearDialogViewCardImg!!.setOnClickListener {
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
        val animFragmentHide = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
        binding!!.constrainLayoutMain.startAnimation(animFragmentHide)
        cacheManager.put(
            TechresEnum.KEY_BACK.toString(),
            TechresEnum.ACCOUNTMENU_ID_FRAGMENT.toString()
        )
        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.nav_host, MainFragment())
                    ?.commit()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}