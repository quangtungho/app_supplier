package vn.techres.supplier.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication.Companion.context
import vn.techres.supplier.adapter.chatadapter.DetailOrderAdapter
import vn.techres.supplier.base.BaseBindingActivity
import vn.techres.supplier.databinding.ActivityDetailOrderBinding
import vn.techres.supplier.helper.*
import vn.techres.supplier.helper.Currency
import vn.techres.supplier.model.chat.data.FileChat
import vn.techres.supplier.model.chat.data.MessageChat
import vn.techres.supplier.model.datamodel.DataDetailOrderBillAll
import vn.techres.supplier.model.datamodel.DataListMatialOrder
import vn.techres.supplier.model.entity.CurrentConfigJava
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.DetailListMatialOrderResponse
import vn.techres.supplier.model.response.DetailOrderBillAllResponse
import vn.techres.supplier.model.response.FileChatResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class DetailOrderActivity : BaseBindingActivity<ActivityDetailOrderBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDetailOrderBinding
        get() = ActivityDetailOrderBinding::inflate
    private var idOrderBill: Int = 0
    private var adapter: DetailOrderAdapter? = null
    private var dataDetailBillOrder = DataDetailOrderBillAll()
    private var type_check_adapter = 0
    var bitmapImage: Bitmap? = null
    private val fileChat = ArrayList<FileChat>()
    private var messageKey = ""
    private var photoPath = ""
    private var groupID = ""

    //Lấy danh sách nguyên liệu theo phiếu đặt hàng từ kế toán
    private var listBillOrderMaterial = ArrayList<DataListMatialOrder>()
    override fun onSetBodyView() {
        binding.header.toolbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.header.toolbarTitle.text = getString(R.string.txt_detail_order)
        setAdapter()
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.constraintLayoutMain.startAnimation(animFragment)
        idOrderBill = cacheManager.get(TechresEnum.GET_BY_ID.toString())!!.toInt()
        groupID = intent.getStringExtra(TechresEnum.GET_BY_ID_GROUP.toString()).toString()
        binding.viewReturnsOrder.visibility = View.VISIBLE
        binding.txtReturnsOrder.visibility = View.VISIBLE
        binding.receive.visibility = View.VISIBLE
        binding.viewReceiveOrder.visibility = View.VISIBLE
        binding.totalMoney1.visibility = View.GONE
        binding.totalMoney.visibility = View.GONE
        binding.totalMoney2.visibility = View.VISIBLE
        binding.txtTotalMoney2.visibility = View.VISIBLE
        binding.imgCopy.text = getString(R.string.take_a_picture)
        getAPIDetailBillOrder()
        getAPIBillOrderMaterial()
        AppUtils.callPhoto(cacheManager.get(TechresEnum.GET_BY_AVATAR.toString()), binding.avatar)

        binding.icnImageCLipDetele.setOnClickListener {
            binding.rlnImageClip.visibility = View.GONE
            binding.btnCopy.setBackgroundResource(R.drawable.bg_radius_copy)
            binding.imgCopy.text = getString(R.string.take_a_picture)
        }


        binding.btnCopy.setOnClickListener {
            bitmapImage = getScreenshotToView(binding.rcvInventoryReport)
            try {
                createImageFile(bitmapImage!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            binding.btnCopy.setBackgroundResource(R.drawable.bg_radius_uncopy)
            binding.imgCopy.text = getString(R.string.un_take_a_picture)

            Handler().postDelayed({
                binding.rlnImageClip.visibility = View.VISIBLE
                binding.imageCLip.setImageBitmap(bitmapImage)
            }, 1000)
        }


        binding.sendPhotoClip.setOnClickListener { j ->
            if (type_check_adapter == 2) {
                Toast.makeText(this, "Đã lưu vào thư viện.", Toast.LENGTH_SHORT).show()
                binding.rlnImageClip.visibility = View.GONE
            } else {
                Handler().postDelayed({
                    uploadFileChatToServer(
                        this,
                        photoPath,
                        groupID,
                        0,
                        0,
                        0
                    )
                }, 4000)

            }
        }
    }

    fun setAdapter() {
        adapter = DetailOrderAdapter(this.baseContext)
        binding.rcvInventoryReport.adapter = adapter
        binding.rcvInventoryReport.layoutManager = LinearLayoutManager(context)
        binding.rcvInventoryReport.setHasFixedSize(true)
    }

    private fun getScreenshotToView(view: View): Bitmap {
        var view = view
        if (view.visibility != View.VISIBLE) {
            view = getNextView(view)
        }
        //Define a bitmap with the same size as the view
        val returnedBitmap: Bitmap = when (view) {
            is ScrollView -> {
                Bitmap.createBitmap(
                    (view as ViewGroup).getChildAt(0).width,
                    (view as ViewGroup).getChildAt(0).height,
                    Bitmap.Config.ARGB_8888
                )
            }
            is RecyclerView -> {
                view.measure(
                    View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                Bitmap.createBitmap(
                    view.getWidth(),
                    view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888
                )
            }
            else -> {
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            }
        }

        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

    private fun getNextView(view: View): View {
        var view = view
        if (view.parent != null && view.parent is ViewGroup) {
            val group = view.parent as ViewGroup
            var child: View
            var getNext = false
            //Iterate through all views from parent
            for (i in 0 until group.childCount) {
                child = group.getChildAt(i)
                if (getNext) {
                    //Make sure the view is visible, else iterate again until we find a visible view
                    if (child.visibility == View.VISIBLE) {
                        view = child
                    }
                }
                //Iterate until we find out current view,
                // then we want to get the NEXT view
                if (child.id == view.id) {
                    getNext = true
                }
            }
        }
        return view
    }

    @Throws(IOException::class)
    fun createImageFile(bitmap: Bitmap) {
        val filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val dir = File(filepath.absolutePath + "/Supplier/")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val outputStream: FileOutputStream
        val file = File(dir, String.format("%s.png", System.currentTimeMillis().toString()))
        outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        try {
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        MediaScannerConnection.scanFile(this, arrayOf(file.path), arrayOf("image/jpeg"), null)
        photoPath = file.path
    }

    private fun saveBitmap(bitmap: Bitmap): String? {
        val imagePath = File(
            Environment.getExternalStorageDirectory().toString() + String.format(
                "/screenshot_%s.png",
                Calendar.getInstance().timeInMillis
            )
        )
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(imagePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            return imagePath.path
        } catch (ignored: IOException) {
        }
        return ""
    }

    private fun formatFloatToString(f: Float): String {
        val i = f.toInt()
        return if (f == i.toFloat()) i.toString() else f.toString()
    }

    private fun uploadFileChatToServer(
        activity: Activity?,
        path: String?,
        groupID: String?,
        size: Long,
        width: Int,
        height: Int,
    ) {
        uploadNameFileChat(path, size, width, height)
        uploadFileChat(activity, path, groupID)
    }

    @SuppressLint("LogNotTimber")
    private fun uploadFileChat(
        activity: Activity?,
        photo: String?,
        groupID: String?
    ) {
        val serverUrlString: String =
            CurrentConfigJava.getConfigJava(context).ads_domain + String.format(
                "/api-upload/upload-file-server-chat/%s/%s/%s",
                1,
                groupID,
                AppUtils.getNameFileToPath(photo!!)
            )
        Log.d("upload_domain", serverUrlString)
        val paramNameString = "send_file"
        try {
            MultipartUploadRequest(this, serverUrlString)
                .setMethod("POST")
                .addFileToUpload(photo, paramNameString)
                .addHeader(
                    "Authorization",
                    CurrentUser.getCurrentUser(this)!!.nodeAccessToken
                )
                .setMaxRetries(3)
                .setNotificationConfig { _, uploadId ->
                    SupplierApplication.getNotificationConfig(
                        activity,
                        uploadId,
                        R.string.multipart_upload
                    )
                }
                .startUpload()

        } catch (exc: Exception) {

        }
    }

    private fun uploadNameFileChat(
        path: String?,
        size: Long,
        width: Int,
        height: Int
    ) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_UPLOAD
        params.request_url =
            "api-upload/get-link-server-chat?type=1" + "&name_file=" + AppUtils.getNameFileToPath(
                path!!
            ) + "&group_id=" + groupID + "&size=" + size + "&width=" + width + "&height=" + height
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, this
        )
            .uploadNameFile(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<FileChatResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: FileChatResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        fileChat.add(response.data)
                        AppUtils.emitSocket(
                            SocketChatEmitDataEnum.CHAT_IMAGE.toString(), MessageChat(
                                CurrentUser.getCurrentUser(this@DetailOrderActivity)!!.id,
                                groupID,
                                SocketChatMessageTypeEnum.IMAGE.toString().toInt(),
                                fileChat,
                                messageKey
                            )
                        )
                        finish()
                    } else
                        Toast.makeText(
                            this@DetailOrderActivity,
                            response.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                }
            })
    }

    /**
     * goi API get chi tiet don hang
     */
    private fun getAPIDetailBillOrder() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders/$idOrderBill"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, this
        )
            .getDetailBillOrderAll(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DetailOrderBillAllResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("SetTextI18n")
                override fun onNext(response: DetailOrderBillAllResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataDetailBillOrder = response.data
                        //thong tin đặt hàng
                        binding.nameRestaurant.text = dataDetailBillOrder.restaurant_name
                        binding.nameBrand.text = dataDetailBillOrder.restaurant_brand_name
                        binding.nameBranch.text = dataDetailBillOrder.branch_name
                        binding.deliveryDate.text = dataDetailBillOrder.delivery_at
                        binding.code.text = dataDetailBillOrder.code
                        binding.totalmaterial.text =
                            formatFloatToString(dataDetailBillOrder.total_material!!.toFloat())

                        binding.totalMoney2.text =
                            Currency.formatCurrencyDecimal(
                                dataDetailBillOrder.total_amount_reality
                                    .toFloat()
                            )
                    }else {
                        Toast.makeText(this@DetailOrderActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    /**
     * goi API get chi tiet nguyen lieu don hang
     */
    private fun getAPIBillOrderMaterial() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders/$idOrderBill/supplier-order-detail?is_return_material&status&time&page&limit"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, this
        )
            .getDetailOrderMaterial(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DetailListMatialOrderResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: DetailListMatialOrderResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listBillOrderMaterial = response.data
                        adapter!!.setDataListOrder(listBillOrderMaterial)
                    }else {
                        Toast.makeText(this@DetailOrderActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
}