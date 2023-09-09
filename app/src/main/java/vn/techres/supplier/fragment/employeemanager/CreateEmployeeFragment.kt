package vn.techres.supplier.fragment.employeemanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
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
import vn.techres.supplier.databinding.FragmentCreateEmployeeBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.listener.GlideEngine
import vn.techres.supplier.model.entity.CurrentConfigNode
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.datamodel.DataListEmployee
import vn.techres.supplier.model.datamodel.DataRoles
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.CreateStaffParams
import vn.techres.supplier.model.response.EmployeeProfileResponse
import vn.techres.supplier.model.response.GetLinkImageResponse
import vn.techres.supplier.model.response.RolesExchangeResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class CreateEmployeeFragment :
        BaseBindingFragment<FragmentCreateEmployeeBinding>(FragmentCreateEmployeeBinding::inflate) {
    val tagName: String = CreateEmployeeFragment::class.java.name
    var listSpinner: ArrayList<String> = ArrayList()
    var listRoles: ArrayList<DataRoles> = ArrayList()
    private var nameImage: String? = ""
    var fileImage = ""
    var avatarUrl = ""
    private var listData = ArrayList<DataListEmployee>()
    var idRoles = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        mainActivity!!.setHeader(getString(R.string.header_create_employee))
        mainActivity!!.setBackClick(true)

        binding!!.btnChoose.setOnClickListener {
            chooseAvatar()
        }
        binding!!.btnAdd.setOnClickListener {
            binding!!.btnAdd.isEnabled = false
            Handler().postDelayed({ binding!!.btnAdd.isEnabled = true }, 2000)
            binding!!.btnAdd.showLoading()
            btnCreateEmployee()
        }

    }

    private fun dataSpinner() {
        for (item in listRoles.indices) {
            binding!!.spinnerData.adapter = ArrayAdapter(
                    mainActivity!!,
                    android.R.layout.simple_list_item_1,
                    listSpinner
            )
        }
        binding!!.spinnerData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listRoles.indices) {
                    if (listSpinner[p2] == listRoles[i].name) {
                        idRoles = listRoles[i].id
                        binding!!.txtRoles.text = listRoles[i].name
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun btnCreateEmployee() {

        avatarUrl = nameImage!!
        val names = binding!!.nameStaff.text.toString()
        val address = binding!!.addressStaff.text.toString()
        val email = binding!!.emailStaff.text.toString()
        val phone = binding!!.phoneStaff.text.toString()
        idRoles
        when {
            avatarUrl.isEmpty() -> {
                val snack = Snackbar.make(
                        binding!!.constrainLayoutMain,
                        getString(R.string.toast_img_staff_empty),
                        Snackbar.LENGTH_LONG
                )
                snack.show()
                binding!!.btnAdd.hideLoading()
            }
            names.isEmpty() -> {
                val snack = Snackbar.make(
                        binding!!.constrainLayoutMain,
                        getString(R.string.toast_name_staff_empty),
                        Snackbar.LENGTH_LONG
                )
                snack.show()
                binding!!.btnAdd.hideLoading()
            }
            address.isEmpty() -> {
                val snack = Snackbar.make(
                        binding!!.constrainLayoutMain,
                        getString(R.string.toast_address_staff_empty),
                        Snackbar.LENGTH_LONG
                )
                snack.show()
                binding!!.btnAdd.hideLoading()
            }
            email.isEmpty() -> {
                val snack = Snackbar.make(
                        binding!!.constrainLayoutMain,
                        getString(R.string.toast_email_staff_empty),
                        Snackbar.LENGTH_LONG
                )
                snack.show()
                binding!!.btnAdd.hideLoading()
            }
            phone.isEmpty() -> {
                val snack = Snackbar.make(
                        binding!!.constrainLayoutMain,
                        getString(R.string.toast_phones_staff_empty),
                        Snackbar.LENGTH_LONG
                )
                snack.show()
                binding!!.btnAdd.hideLoading()
            }
            binding!!.txtRoles.text == getString(R.string.txt_please_choose) -> {
                val snack = Snackbar.make(
                        binding!!.constrainLayoutMain,
                        getString(R.string.toast_roles_exchange_empty),
                        Snackbar.LENGTH_LONG
                )
                snack.show()
                binding!!.btnAdd.hideLoading()
            }

            else -> {
                //createEmployee(names, address, phone, email, idRolesExchange, avatarUrl)
                getLinkPhoto(names, address, phone, email, idRoles, nameImage!!)

            }
        }
    }

    private fun chooseAvatar() {
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

    override fun onResume() {
        super.onResume()
        listRoles.clear()
        listRolesExchange()
    }

    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
    //__________API____________//
    /**
     *  goi API get danh sach chuc vu supplier]
     */
    private fun listRolesExchange() {
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
                            listRoles.clear()
                            listRoles = response.data!!
                            for (item in response.data!!.indices) {
                                listSpinner.add(response.data!![item].name)
                            }
                            dataSpinner()
                        } else {
                            val snack = Snackbar.make(
                                    binding!!.constrainLayoutMain,
                                    response.status,
                                    Snackbar.LENGTH_LONG
                            )
                            snack.show()
                        }


                    }
                })
    }

    private fun getLinkPhoto(
            names: String,
            address: String,
            phone: String,
            email: String, roles: Int, name_file: String,
    ) {
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
                        val snack = Snackbar.make(
                                binding!!.constrainLayoutMain,
                                getString(R.string.toast_error_get_link),
                                Snackbar.LENGTH_LONG
                        )
                        snack.show()
                        binding!!.btnAdd.hideLoading()
                    }

                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: GetLinkImageResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            avatarUrl = response.data!!.link_original
                            createEmployee(
                                    names,
                                    address,
                                    phone,
                                    email,
                                    roles,
                                    avatarUrl,
                            )
                            uploadPhoto(fileImage, TechresEnum.TYPE_UPLOAD.toString().toInt())
                            binding!!.btnAdd.hideLoading()

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
                        mainActivity!!.setLoading(false)
                        val snack = Snackbar.make(
                                binding!!.constrainLayoutMain,
                                getString(R.string.toast_error_upload_image),
                                Snackbar.LENGTH_LONG
                        )
                        snack.show()
                        binding!!.btnAdd.hideLoading()
                    }


                    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                    }

                    override fun onSuccess(
                            context: Context,
                            uploadInfo: UploadInfo,
                            serverResponse: ServerResponse,
                    ) {
                    }

                })
    }

    /**
     *  goi API post tao nhan vien supplier
     */
    private fun createEmployee(
            names: String,
            address: String,
            phone: String,
            email: String,
            roles: Int,
            avatar: String,
    ) {
        val params = CreateStaffParams()
        params.http_method = 1 /*post 1/get 0*/
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees/create"
        params.params.name = names
        params.params.address = address
        params.params.email = email
        params.params.phone = phone
        params.params.supplier_role_id = roles
        params.params.avatar = avatar
        params.let {
            ServiceFactory.createRetrofitService(
                    TechResService::class.java, requireActivity()
            )
                    .getCreateEmployee(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<EmployeeProfileResponse> {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {
                        }

                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(response: EmployeeProfileResponse) {
                            if (response.status == AppConfig.SUCCESS_CODE) {
                                listData.clear()
                                mainActivity!!.supportFragmentManager.popBackStack()

                            } else {
                                val snack = Snackbar.make(
                                        binding!!.constrainLayoutMain,
                                        response.status,
                                        Snackbar.LENGTH_LONG
                                )
                                snack.show()
                            }
                        }
                    })
        }
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
                            .into(binding!!.avatar)

                    nameImage = selectList[0].fileName
                    fileImage = selectList[0].path
                } else {
                    Glide.with(this)
                            .load(
                                    selectList[0].realPath
                            )
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .into(binding!!.avatar)

                    nameImage = selectList[0].fileName
                    fileImage = selectList[0].realPath
                }
            }
        }
    }
}