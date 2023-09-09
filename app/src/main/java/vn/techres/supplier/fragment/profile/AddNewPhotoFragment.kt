package vn.techres.supplier.fragment.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
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
import vn.techres.supplier.databinding.FragmentAddNewPhotoBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.listener.GlideEngine
import vn.techres.supplier.model.entity.CurrentConfigNode
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.entity.User
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.EditProfileAccountParams
import vn.techres.supplier.model.response.GetLinkImageResponse
import vn.techres.supplier.model.response.UserResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class AddNewPhotoFragment :
    BaseBindingFragment<FragmentAddNewPhotoBinding>(FragmentAddNewPhotoBinding::inflate) {
    val tagName: String = AddNewPhotoFragment::class.java.name

    private var nameImage: String? = ""
    private var avatarUrl: String? = ""
    private var fileImage = ""
    private var dataStatus: Boolean = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        //onCreate
        fragmentCreate()

        onClickChangeNewPhoto()

        //When click button, update avatar and intent Fragment ProfileAccount
        onClickButtonAccept()
    }
    override fun onResume() {
        super.onResume()
        //Khi người dùng không chọn hình nào thì back về màn hình profile
        if (!dataStatus) {
            onBackPress()
        }
    }
    private fun fragmentCreate() {
        mainActivity!!.setHeader(getString(R.string.title_update_avatar))

        val animFragment = AnimationUtils.loadAnimation(context, R.anim.slide_bottom_in)
        binding!!.constrainLayoutMain.startAnimation(animFragment)

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
    private fun onClickChangeNewPhoto() {
        binding!!.btnChangeNewPhoto.setOnClickListener {
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
    }
    private fun onClickButtonAccept() {
        binding!!.btnConfirmAndSave.setOnClickListener {
            getLinkPhoto(nameImage!!)
        }
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
                        Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
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
        try {
            MultipartUploadRequest(mainActivity!!, serverUrlString)
                .setMethod("POST")
                .addHeader(
                    resources.getString(R.string.Authorization),
                    CurrentUser.getCurrentUser(mainActivity!!)!!.nodeAccessToken
                )
                .addFileToUpload(
                    filePath = photo,
                    parameterName = resources.getString(R.string.send_file)
                )
                .subscribe(mainActivity!!, this, object : RequestObserverDelegate {
                    override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                    }

                    override fun onCompletedWhileNotObserving() {
                    }

                    override fun onError(
                        context: Context,
                        uploadInfo: UploadInfo,
                        exception: Throwable,
                    ) {
                        mainActivity!!.setLoading(false)
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
                        updateAvatar()
                    }
                })
        } catch (e: Exception) {
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            dataStatus = true
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val selectList = PictureSelector.obtainMultipleResult(data)
                selectList[0].realPath

                if (selectList[0].realPath == null) {
                    Glide.with(this)
                        .load(
                            selectList[0].path
                        )
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding!!.imageNewPhoto)

                    nameImage = selectList[0].fileName
                    fileImage = selectList[0].path
                } else {
                    Glide.with(this)
                        .load(
                            selectList[0].realPath
                        )
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding!!.imageNewPhoto)

                    nameImage = selectList[0].fileName
                    fileImage = selectList[0].realPath
                }
            }
        } else {
            dataStatus = false
        }
    }
    private fun updateAvatar() {
        val params = EditProfileAccountParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees/" + CurrentUser.getCurrentUser(mainActivity!!)!!.id

        params.params.name = CurrentUser.getCurrentUser(mainActivity!!)!!.name
        params.params.phone = CurrentUser.getCurrentUser(mainActivity!!)!!.phone
        params.params.address = CurrentUser.getCurrentUser(mainActivity!!)!!.address
        params.params.email = CurrentUser.getCurrentUser(mainActivity!!)!!.email
        params.params.supplier_role_id =
            CurrentUser.getCurrentUser(mainActivity!!)!!.supplier_role_id
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
                            AnimationUtils.loadAnimation(
                                requireActivity(),
                                R.anim.slide_bottom_out
                            )
                        binding!!.constrainLayoutMain.startAnimation(animFragmentHide)

                        Toast.makeText(
                            requireActivity(),
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        /**
                         * Nếu chỉnh sửa avatar ở màn hình ProfileAccountFragment thì chuyển về màn hình ProfileAccountFragment
                         * Ngược lại sẽ chuyển về màn hình InfoProfileFragment
                         */
                        if (cacheManager.get(TechresEnum.KEY_BACK.toString()) == TechresEnum.ACCOUNTMENU_ID_FRAGMENT.toString()) {
                            activity?.supportFragmentManager
                                ?.beginTransaction()
                                ?.replace(
                                    R.id.nav_host,
                                    ProfileAccountFragment()
                                )
                                ?.commit()
                        } else {
                            cacheManager.put(
                                TechresEnum.KEY_BACK.toString(),
                                TechresEnum.KEY_BACK.toString()
                            )
                            activity?.supportFragmentManager
                                ?.beginTransaction()
                                ?.replace(
                                    R.id.nav_host,
                                    InfoProfileFragment()
                                )
                                ?.commit()
                        }
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
            it.avatar = userResponse?.avatar + ""
        }
        CurrentUser.saveUserInfo(mainActivity!!.baseContext, user)
    }
    override fun onBackPress() {
        val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_bottom_out)
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