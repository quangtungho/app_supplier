package vn.techres.supplier.fragment.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import vn.techres.supplier.R
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentEditProfileBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.listener.GlideEngine
import vn.techres.supplier.model.entity.CurrentConfigNode
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.entity.User
import vn.techres.supplier.model.datamodel.DataRoles
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.EditProfileAccountParams
import vn.techres.supplier.model.response.GetLinkImageResponse
import vn.techres.supplier.model.response.RolesExchangeResponse
import vn.techres.supplier.model.response.UserResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.util.stream.Collectors

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class EditProfileFragment :
        BaseBindingFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {
    val tagName: String = EditProfileFragment::class.java.name
    private var nameImage: String? = ""
    private var avatarUrl: String = ""
    private var fileImage = ""
    private var idRolesExchange = 0
    private var user = User()
    private var listSpinnerString = ArrayList<String>()
    private var listRolesExchange = ArrayList<DataRoles>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {

        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        //onCreate
        fragmentCreate()
        //Animation alpha infinite
        val animAlpha = AnimationUtils.loadAnimation(context, R.anim.alpha_infinite)
        //set animation alpha icon camera on the avatar
        binding!!.iconCameraEditprofile.startAnimation(animAlpha)

        /**
         * When edittext focusted then:
         * 1. Icon pencil animation alpha infinite
         * 2. View underlined background color = orange
         * When edittext focusenabled then everything return initial state
         */

        //Edittext name user
        binding!!.editNameEditprofile.setOnFocusChangeListener { _, b ->
            if (b) {
                binding!!.iconeditName.startAnimation(animAlpha)
                binding!!.viewNameEditprofile.setBackgroundResource(R.color.color_main)
            } else {
                binding!!.iconeditName.clearAnimation()
                binding!!.viewNameEditprofile.setBackgroundResource(R.color.grey_main)
            }
        }
        binding!!.editPhoneEditprofile.setOnClickListener {
            Toast.makeText(
                    requireActivity(),
                    "Số điện thoại không được chỉnh sửa",
                    Toast.LENGTH_SHORT
            ).show()
        }
        //Edittext phone
        binding!!.editPhoneEditprofile.setOnFocusChangeListener { _, b ->

            if (b) {
                binding!!.iconeditPhone.startAnimation(animAlpha)
                binding!!.viewPhoneEditprofile.setBackgroundResource(R.color.color_main)
            } else {
                binding!!.iconeditPhone.clearAnimation()
                binding!!.viewPhoneEditprofile.setBackgroundResource(R.color.grey_main)
            }
        }

        //Edittext email
        binding!!.editEmailEditprofile.setOnFocusChangeListener { _, b ->
            if (b) {
                binding!!.iconeditEmail.startAnimation(animAlpha)
                binding!!.viewEmailEditprofile.setBackgroundResource(R.color.color_main)
            } else {
                binding!!.iconeditEmail.clearAnimation()
                binding!!.viewEmailEditprofile.setBackgroundResource(R.color.grey_main)
            }
        }

        //Edittext address
        binding!!.editAddressEditprofile.setOnFocusChangeListener { _, b ->
            if (b) {
                binding!!.iconeditAddress.startAnimation(animAlpha)
                binding!!.viewAddressEditprofile.setBackgroundResource(R.color.color_main)
            } else {
                binding!!.iconeditAddress.clearAnimation()
                binding!!.viewAddressEditprofile.setBackgroundResource(R.color.grey_main)
            }
        }//--------------------------------------------------------------------------------------------------
        //onClick avatar
        binding!!.imgAccountDetail.setOnClickListener {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isCamera(true)
                    .maxSelectNum(1)
                    .minSelectNum(0)
                    .isMaxSelectEnabledMask(true)
                    .isOpenClickSound(true)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
        }


        /**
         * onClick button update
         * update name, address, email, avatar
         * if success then intent fragment profile
         * if error then toast error
         */
        binding!!.btnUpdateEditprofile.setOnClickListener {
            if (fileImage.isEmpty()) {
                avatarUrl = CurrentUser.getCurrentUser(mainActivity!!)!!.avatar
                updateProfile(idRolesExchange)
            } else {
                getLinkPhoto(nameImage!!)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        listRolesAPI()
    }

    private fun fragmentCreate() {
        mainActivity!!.setHeader(getString(R.string.title_editprofile))

        val animFragment = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragment)

        user = CurrentUser.getCurrentUser(mainActivity!!.baseContext)!!
        AppUtils.callPhotoAvatar(user.avatar, binding!!.imgAccountDetail)
        binding!!.editNameEditprofile.setText(user.name)
        binding!!.editPhoneEditprofile.setText(user.phone)
        binding!!.editEmailEditprofile.setText(user.email)
        binding!!.editAddressEditprofile.setText(user.address)
        binding!!.txtRolesExchange.text = user.supplier_employee_position
    }

    private fun dataSpinner() {
        listSpinnerString = listSpinnerString.stream()
                .filter { x -> !x.equals(user.supplier_employee_position) }
                .collect(Collectors.toList()) as ArrayList<String>
        val swap = listRolesExchange.stream()
                .filter { x -> x.name == user.supplier_employee_position }
                .collect(Collectors.toList()) as ArrayList<DataRoles>
        listRolesExchange = listRolesExchange.stream()
                .filter { x -> x.name != user.supplier_employee_position }
                .collect(Collectors.toList()) as ArrayList<DataRoles>
        listSpinnerString.add(0, user.supplier_employee_position)
        listRolesExchange.add(0, swap[0])
        binding!!.spinnerData.adapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                listSpinnerString
        )
        binding!!.spinnerData.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        for (i in listRolesExchange.indices) {
                            if (listSpinnerString[p2] == listRolesExchange[i].name) {
                                idRolesExchange = listRolesExchange[i].id
                                binding!!.txtRolesExchange.text = listRolesExchange[p2].name
                                break
                            }
                        }
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }


    }

    private fun listRolesAPI() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-roles"

        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .getListRolesExchange(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RolesExchangeResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: RolesExchangeResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listRolesExchange = response.data!!
                            listSpinnerString.clear()
                            for (item in listRolesExchange.indices) {
                                listSpinnerString.add(listRolesExchange[item].name)
                            }
                            dataSpinner()
                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                    .show()
                        }
                    }
                })
    }

    private fun updateProfile(idRolesExchange: Int) {
        val params = EditProfileAccountParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees/${user.id}"
        params.params.name = binding!!.editNameEditprofile.text.toString()
        params.params.email = binding!!.editEmailEditprofile.text.toString()
        params.params.phone = binding!!.editPhoneEditprofile.text.toString()
        params.params.address = binding!!.editAddressEditprofile.text.toString()
        params.params.supplier_role_id = idRolesExchange
        params.params.avatar = avatarUrl
        ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
        )
                .editProfileAccount(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: UserResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            val user = response.data
                            saveUser(user)

                            val animFragmentHide =
                                    AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
                            binding!!.constrainLayoutMain.startAnimation(animFragmentHide)

                            cacheManager.put(
                                    TechresEnum.KEY_BACK.toString(),
                                    TechresEnum.KEY_BACK.toString()
                            )

                            Toast.makeText(
                                    requireActivity(),
                                    getString(R.string.toast_edit_success),
                                    Toast.LENGTH_SHORT
                            )
                                    .show()

                            Handler(Looper.getMainLooper()).postDelayed(
                                    {
                                        activity?.supportFragmentManager
                                                ?.beginTransaction()
                                                ?.replace(R.id.nav_host, ProfileAccountFragment())
                                                ?.commit()
                                    }, TechresEnum.TIME_100.toString().toLong()
                            )
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {}

                })
    }

    fun saveUser(userResponse: User?) {
        val user = CurrentUser.getCurrentUser(mainActivity!!.baseContext)
        user?.let {
            it.name = userResponse?.name + ""
            it.phone = userResponse?.phone + ""
            it.address = userResponse?.address + ""
            it.email = userResponse?.email + ""
            it.supplier_employee_position = userResponse?.supplier_employee_position + ""
            it.avatar = userResponse?.avatar + ""
        }
        CurrentUser.saveUserInfo(mainActivity!!.baseContext, user)

    }

    private fun getLinkPhoto(name_file: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_UPLOAD
        params.request_url =
                "/api-upload/get-link-file-by-user-supplier?type=${TechresEnum.TYPE_UPLOAD}&name_file=$name_file"
        ServiceFactory.createRetrofitServiceNode(
                TechResService::class.java, requireActivity()
        )
                .getLinkImage(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<GetLinkImageResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        Toast.makeText(
                                requireActivity(),
                                getString(R.string.toast_error_get_link),
                                Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: GetLinkImageResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            avatarUrl = response.data!!.link_original
                            uploadPhoto(fileImage, TechresEnum.TYPE_UPLOAD.toString().toInt())
                        } else {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
                })
    }

    private fun uploadPhoto(photo: String, type: Int) {
        //============ Upload image to server via service ===============
        val serverUrlString: String =
                String.format(
                        "%s/api-upload/upload-file-by-user-supplier/%s/%s",
                        CurrentConfigNode.getConfigNode(mainActivity!!).api_ads,
                        type,
                        nameImage
                )
        MultipartUploadRequest(mainActivity!!, serverUrlString)
                .setMethod("POST")
                .addHeader(
                        resources.getString(R.string.Authorization),
                        CurrentUser.getAccessTokenNodeJs(mainActivity!!)
                )
                .addFileToUpload(
                        filePath = photo,
                        parameterName = resources.getString(R.string.send_file)
                ).subscribe(mainActivity!!, this, object : RequestObserverDelegate {
                    override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                    }

                    override fun onCompletedWhileNotObserving() {
                    }

                    override fun onError(
                            context: Context,
                            uploadInfo: UploadInfo,
                            exception: Throwable,
                    ) {
                        Toast.makeText(
                                requireActivity(),
                                getString(R.string.toast_error_upload_image),
                                Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                    }

                    override fun onSuccess(
                            context: Context,
                            uploadInfo: UploadInfo,
                            serverResponse: ServerResponse,
                    ) {
                        updateProfile(idRolesExchange)
                    }

                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val selectList = PictureSelector.obtainMultipleResult(data)

                if (selectList[0].realPath == null) {
                    Glide.with(this)
                            .load(
                                    selectList[0].path
                            )
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .into(binding!!.imgAccountDetail)

                    nameImage = selectList[0].fileName
                    fileImage = selectList[0].path
                } else {
                    Glide.with(this)
                            .load(
                                    selectList[0].realPath
                            )
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .into(binding!!.imgAccountDetail)

                    nameImage = selectList[0].fileName
                    fileImage = selectList[0].realPath
                }
            }
        }
    }

    override fun onBackPress() {
        val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
        binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    cacheManager.put(
                            TechresEnum.KEY_BACK.toString(),
                            TechresEnum.KEY_BACK.toString()
                    )
                    mainActivity!!.supportFragmentManager.popBackStack()
                }, TechresEnum.TIME_100.toString().toLong()
        )
    }
}