package vn.techres.supplier.fragment.employeemanager

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.textfield.TextInputEditText
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import de.hdodenhof.circleimageview.CircleImageView
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
import vn.techres.supplier.databinding.FragmentDetailEmployeeBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.listener.GlideEngine
import vn.techres.supplier.model.datamodel.DataListEmployee
import vn.techres.supplier.model.datamodel.DataRoles
import vn.techres.supplier.model.entity.CurrentConfigNode
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.params.EditStaffParams
import vn.techres.supplier.model.response.EditStaffResponse
import vn.techres.supplier.model.response.EmployeeProfileResponse
import vn.techres.supplier.model.response.GetLinkImageResponse
import vn.techres.supplier.model.response.RolesExchangeResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import vn.techres.supplier.view.TechResTextView
import java.util.stream.Collectors

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class DetailEmployeeFragment :
    BaseBindingFragment<FragmentDetailEmployeeBinding>(FragmentDetailEmployeeBinding::inflate) {
    val tagName: String = DetailEmployeeFragment::class.java.name
    private var listData = ArrayList<DataListEmployee>()
    private var dataEmployee = DataListEmployee()
    private var nameImage: String? = ""
    var fileImage = ""
    var avatarUrl = ""
    private var idEmployee = 0
    var avatar: CircleImageView? = null
    var idEditEmployee = 0
    var listRolesExchange: ArrayList<DataRoles> = ArrayList()
    var listSpinnerString: ArrayList<String> = ArrayList()
    var idRoles = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        mainActivity!!.setHeader(getString(R.string.profileStaff))
        idEmployee = cacheManager.get(TechresEnum.GET_BY_ID.toString())!!.toInt()
        idEditEmployee = cacheManager.get(TechresEnum.GET_BY_ID.toString())!!.toInt()
        listData.removeAll(listData.toSet())
        if (listData != null) {
            binding!!.btnEditStaff.setOnClickListener {
                dialogEditInfo()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        listData.clear()
        detailStaff(idEmployee)
        listRolesExchange.clear()
        listRolesAPI()
    }

    /**
     * goi API get danh sach chuc vu supplier
     */
    private fun listRolesAPI() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/supplier-roles"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, mainActivity!!
        )
            .getListRolesExchange(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<RolesExchangeResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Toast.makeText(
                        mainActivity,
                        getString(R.string.onError),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: RolesExchangeResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        if (response.data != null)
                            listRolesExchange = response.data!!
                        for (item in response.data!!.indices) {
                            listSpinnerString.add(response.data!![item].name)
                        }
                    } else
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun dialogEditInfo() {
        val dialog = Dialog(mainActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_update_employee)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        idRoles = 0
        val spinnerData: Spinner = dialog.findViewById(R.id.spinnerData)
        val txtRolesExchange: TechResTextView = dialog.findViewById(R.id.txtRoles)
        val btnEdit = dialog.findViewById<Button>(R.id.btnEditAndBack)
        val nameStaff = dialog.findViewById<TextInputEditText>(R.id.name_staff)
        val addressStaff = dialog.findViewById<TextInputEditText>(R.id.address_staff)
        val emailStaff = dialog.findViewById<TextInputEditText>(R.id.email_staff)
        val phoneStaff = dialog.findViewById<TextView>(R.id.phone_staff)
        val cancelButton = dialog.findViewById<ImageButton>(R.id.cancel_button)
        val imgDialogAvatar = dialog.findViewById<CircleImageView>(R.id.avatar)
        val btnChoose = dialog.findViewById<ImageButton>(R.id.btnChoose)
        //Animation alpha infinite
        val animAlpha = AnimationUtils.loadAnimation(context, R.anim.alpha_infinite)
        btnChoose!!.startAnimation(animAlpha)
        //set animation alpha icon camera on the avatar

        //Show dialog detail
        nameStaff.setText(dataEmployee.name)
        addressStaff.setText(dataEmployee.address)
        emailStaff.setText(dataEmployee.email)
        phoneStaff.text = dataEmployee.phone
        txtRolesExchange.text = dataEmployee.supplier_employee_position.toString()
        AppUtils.callPhotoAvatar(dataEmployee.avatar, imgDialogAvatar)
        listSpinnerString = listSpinnerString.stream()
            .filter { x -> !x.equals(dataEmployee.supplier_employee_position) }
            .collect(Collectors.toList()) as ArrayList<String>
        val swap = listRolesExchange.stream()
            .filter { x -> x.name == dataEmployee.supplier_employee_position }
            .collect(Collectors.toList()) as ArrayList<DataRoles>
        listRolesExchange = listRolesExchange.stream()
            .filter { x -> x.name != dataEmployee.supplier_employee_position }
            .collect(Collectors.toList()) as ArrayList<DataRoles>
        listSpinnerString.add(0, dataEmployee.supplier_employee_position!!)
        listRolesExchange.add(0, swap[0])

        spinnerData.adapter = ArrayAdapter(
            mainActivity!!,
            android.R.layout.simple_list_item_1,
            listSpinnerString
        )
        spinnerData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (i in listRolesExchange.indices) {
                    if (listSpinnerString[p2] == listRolesExchange[i].name) {

                        idRoles = listRolesExchange[i].id
                        txtRolesExchange.text = listRolesExchange[p2].name
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        btnChoose.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isCamera(true)
                .maxSelectNum(1)
                .minSelectNum(0)
                .isMaxSelectEnabledMask(true)
                .isOpenClickSound(true)
                .forResult(PictureConfig.CHOOSE_REQUEST)
            avatar = imgDialogAvatar
        }
        btnEdit!!.setOnClickListener {
            val name = nameStaff.text.toString()
            val email = emailStaff.text.toString()
            val address = addressStaff.text.toString()
            val phone = phoneStaff.text.toString()

            if (fileImage.isEmpty()) {
                avatarUrl = CurrentUser.getCurrentUser(mainActivity!!)!!.avatar
                editEmployee(name, email, address, phone, idRoles, nameImage!!, dialog)
            } else {
                getLinkPhoto(name, address, phone, email, idRoles, dialog, nameImage!!)
            }
            editEmployee(
                name,
                email,
                address,
                phone,
                idRoles,
                dataEmployee.avatar!!,
                dialog
            )
            mainActivity!!.setLoading(true)
            dialog.dismiss()
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
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
                        .into(avatar!!)

                    nameImage = selectList[0].fileName
                    fileImage = selectList[0].path
                } else {
                    Glide.with(this)
                        .load(
                            selectList[0].realPath
                        )
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(avatar!!)

                    nameImage = selectList[0].fileName
                    fileImage = selectList[0].realPath
                }
            }
        }
    }

    private fun getLinkPhoto(
        names: String,
        address: String,
        phone: String,
        email: String, rolesExchange: Int, dialog: Dialog, name_file: String,
    ) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_UPLOAD
        params.request_url =
            "/api-upload/get-link-file-by-user-supplier?type=${TechresEnum.TYPE_UPLOAD}&name_file=$name_file"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, mainActivity!!
        )
            .getLinkImage(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<GetLinkImageResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Toast.makeText(
                        mainActivity,
                        getString(R.string.toast_error_get_link),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSubscribe(d: Disposable) {}

                override fun onNext(response: GetLinkImageResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        avatarUrl = response.data!!.link_original
                        editEmployee(names, email, address, phone, rolesExchange, avatarUrl, dialog)
                        uploadPhoto(fileImage, TechresEnum.TYPE_UPLOAD.toString().toInt())
                    } else {
                        Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(
                        mainActivity!!,
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
                }

            })
    }

    /**
     *  goi API post chinh sua nhan vien supplier
     */
    private fun editEmployee(
        name_staff: String,
        email_staff: String,
        address_staff: String,
        phone_staff: String,
        rolesExchange: Int,
        avatar: String, dialog: Dialog,
    ) {
        val params = EditStaffParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees/${idEditEmployee}"
        params.params.name = name_staff
        params.params.email = email_staff
        params.params.address = address_staff
        params.params.phone = phone_staff
        params.params.supplier_role_id = rolesExchange
        params.params.avatar = avatar
        params.let {
            ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
            )
                .editEmployee(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<EditStaffResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: EditStaffResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listData.clear()
                            detailStaff(idEditEmployee)
                            mainActivity!!.setLoading(false)
                            dialog.dismiss()
                        } else {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {}

                })
        }
    }

    /**
     *  goi API get chi tiêt nhan vien supplier
     */
    private fun detailStaff(idStaff: Int) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees/$idStaff"
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getDetailEmloyee(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<EmployeeProfileResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: EmployeeProfileResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataEmployee = response.data
                        AppUtils.callPhoto(dataEmployee.avatar, binding!!.imgStaff)
                        binding!!.nameStaff.text = dataEmployee.name.toString()
                        binding!!.addresStaff.text = dataEmployee.address.toString()
                        binding!!.emailStaff.text = dataEmployee.email.toString()
                        binding!!.telephoneStaff.text = dataEmployee.phone.toString()
                        binding!!.roleStaff.text =
                            dataEmployee.supplier_employee_position.toString()
                    } else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })

    }

    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}
