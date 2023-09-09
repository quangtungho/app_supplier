package vn.techres.supplier.activity

import android.annotation.SuppressLint
import android.app.*
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.StrictMode
import android.view.Display
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.multidex.MultiDex
import com.aghajari.emojiview.AXEmojiManager
import com.aghajari.emojiview.emoji.iosprovider.AXIOSEmojiProvider
import com.google.firebase.FirebaseApp
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.Polling
import io.socket.engineio.client.transports.PollingXHR
import io.socket.engineio.client.transports.WebSocket
import net.gotev.uploadservice.BuildConfig
import net.gotev.uploadservice.UploadServiceConfig.initialize
import net.gotev.uploadservice.UploadServiceConfig.retryPolicy
import net.gotev.uploadservice.data.RetryPolicyConfig
import net.gotev.uploadservice.data.UploadNotificationAction
import net.gotev.uploadservice.data.UploadNotificationConfig
import net.gotev.uploadservice.data.UploadNotificationStatusConfig
import net.gotev.uploadservice.extensions.getCancelUploadIntent
import okhttp3.OkHttpClient
import vn.techres.supplier.R
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.WriteLog
import vn.techres.supplier.helper.sticker.UI
import vn.techres.supplier.model.entity.CurrentConfigNode
import vn.techres.supplier.model.entity.CurrentUser.isLogin
import java.net.URISyntaxException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.properties.Delegates


@SuppressLint("StaticFieldLeak")
class SupplierApplication : Application() {
    private var heightView = 0

    var active = false

    companion object {
        private var instance: SupplierApplication? = null

        private var socketInstance: Socket? = null

        lateinit var context: Context

        lateinit var clipboard: ClipboardManager
        var notificationManager: NotificationManager? = null
        var widthView by Delegates.notNull<Int>()

        var UPLOAD_CHANNEL = "UPLOAD CHANNEL"

        fun applicationContext(): SupplierApplication {
            return instance as SupplierApplication
        }

        fun getSocketInstance(): Socket? {
            return if (socketInstance != null) {
                socketInstance
            }else{
                initializationSocket()
                socketInstance
            }
        }

        fun initializationSocket() {
            if (isLogin(context)) {
                val trustAllCerts: Array<TrustManager> = arrayOf(
                        @SuppressLint("CustomX509TrustManager")
                        object : X509TrustManager {
                            @SuppressLint("TrustAllX509TrustManager")
                            override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                            }

                            @SuppressLint("TrustAllX509TrustManager")
                            override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                            }

                            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                                return arrayOf()
                            }

                        }
                )
                try {
                    val sslContext: SSLContext = SSLContext.getInstance("SSL")
                    sslContext.init(null, trustAllCerts, SecureRandom())
                    val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
                    val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                    builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    builder.hostnameVerifier { _, _ -> true }
                    builder.connectTimeout(20, TimeUnit.SECONDS)
                    builder.writeTimeout(1, TimeUnit.MINUTES)
                    builder.readTimeout(1, TimeUnit.MINUTES)
                    IO.setDefaultOkHttpCallFactory(builder.build())
                    IO.setDefaultOkHttpWebSocketFactory(builder.build())
                } catch (ignored: Exception) {
                }
                val opts = IO.Options()
                opts.reconnection = true
                opts.transports = arrayOf(Polling.NAME, PollingXHR.NAME, WebSocket.NAME)
                try {
                    socketInstance = IO.socket(CurrentConfigNode.getConfigNode(context).api_chat_supplier.toString(), opts)
                            .connect()

                    Thread {
                        try {
                            Thread.sleep(5000)
                            WriteLog.d("Socket", String.format("Link: %s, Trang thai: %s", CurrentConfigNode.getConfigNode(
                                    context).api_chat_supplier.toString(), getSocketInstance()!!.connected()))

                        } catch (ignored: Exception) {
                        }
                    }.start()

                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }
            }
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        fun getNotificationConfig(
                activity: Activity?,
                uploadId: String?,
                @StringRes title: Int
        ): UploadNotificationConfig {
            val clickIntent: PendingIntent = PendingIntent.getActivity(
                    activity, 1, Intent(activity, MainActivity::class.java), PendingIntent.FLAG_ONE_SHOT
            )
            val autoClear = true
            val clearOnAction = true
            val ringToneEnabled = true
            val noActions: ArrayList<UploadNotificationAction> = ArrayList(1)
            val cancelAction = UploadNotificationAction(
                    R.drawable.ic_cancelled, context.getString(R.string.cancel_upload),
                    context.getCancelUploadIntent(uploadId!!)
            )
            val progressActions: ArrayList<UploadNotificationAction> = ArrayList(1)
            progressActions.add(cancelAction)
            @SuppressLint("ResourceAsColor") val progress = UploadNotificationStatusConfig(
                    context.getString(title),
                    context.getString(R.string.uploading),
                    R.drawable.ic_upload,
                    Color.BLUE,
                    null,
                    clickIntent,
                    progressActions,
                    clearOnAction,
                    autoClear
            )
            val success = UploadNotificationStatusConfig(
                    context.getString(title),
                    context.getString(R.string.upload_success),
                    R.drawable.ic_upload_success,
                    Color.GREEN,
                    null,
                    clickIntent,
                    noActions,
                    clearOnAction,
                    autoClear
            )
            val error = UploadNotificationStatusConfig(
                    context.getString(title),
                    context.getString(R.string.upload_error),
                    R.drawable.ic_upload_error,
                    Color.RED,
                    null,
                    clickIntent,
                    noActions,
                    clearOnAction,
                    autoClear
            )
            val cancelled = UploadNotificationStatusConfig(
                    context.getString(title),
                    context.getString(R.string.upload_cancelled),
                    R.drawable.ic_cancelled,
                    Color.YELLOW,
                    null,
                    clickIntent,
                    noActions,
                    clearOnAction
            )
            return UploadNotificationConfig(
                    UPLOAD_CHANNEL,
                    ringToneEnabled,
                    progress,
                    success,
                    error,
                    cancelled
            )
        }

        const val notificationChannelID = "TestChannel"
    }

    // Customize the notification channel as you wish. This is only for a bare minimum example
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                    notificationChannelID,
                    "TestApp Channel",
                    NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelUpload = NotificationChannel(
                    UPLOAD_CHANNEL,
                    UPLOAD_CHANNEL,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            channelUpload.description =
                    "Kênh thông báo tiến trình tải hình ảnh, video lên hệ thống."
            channelUpload.setSound(null, null)

            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channelUpload)
        }
    }


    init {
        instance = this
        context = this
    }

    override fun onCreate() {
        super.onCreate()
        AppConfig.loadConfig(this)

        applicationContext()

        clipboard = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)!!

        val builder =
                StrictMode.VmPolicy.Builder()
        builder.detectFileUriExposure()
        StrictMode.setVmPolicy(builder.build())

        createNotificationChannel()

        createNotificationChannels()

        initialize(this, UPLOAD_CHANNEL, BuildConfig.DEBUG)
        retryPolicy = RetryPolicyConfig(1, 10, 2, 3)

        //Create socket
        initializationSocket()

        val wm: WindowManager =
                context.getSystemService(WINDOW_SERVICE) as WindowManager
        val display: Display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        widthView = size.x
        heightView = size.y

        //Setup Emoji
        //Setup Emoji
        AXEmojiManager.install(this, AXIOSEmojiProvider(this))
        UI.mEmojiView = true
        UI.mSingleEmojiView = false
        UI.mStickerView = true
        UI.mCustomView = false
        UI.mFooterView = true
        UI.mCustomFooter = false
        UI.mWhiteCategory = true
        UI.loadTheme()
        AXEmojiManager.getEmojiViewTheme().footerSelectedItemColor =
                getColor(R.color.main_bg)
        AXEmojiManager.getEmojiViewTheme().footerBackgroundColor = Color.WHITE
        AXEmojiManager.getEmojiViewTheme().selectionColor = Color.TRANSPARENT
        AXEmojiManager.getEmojiViewTheme().selectedColor = getColor(R.color.main_bg)
        AXEmojiManager.getEmojiViewTheme().categoryColor = Color.WHITE
        AXEmojiManager.getEmojiViewTheme().setAlwaysShowDivider(true)
        AXEmojiManager.getEmojiViewTheme().backgroundColor = getColor(R.color.white)

        AXEmojiManager.getStickerViewTheme().selectionColor = getColor(R.color.main_bg)
        AXEmojiManager.getStickerViewTheme().selectedColor = getColor(R.color.main_bg)
        AXEmojiManager.getStickerViewTheme().backgroundColor = Color.WHITE
        AXEmojiManager.getStickerViewTheme().categoryColor = getColor(R.color.white)
        AXEmojiManager.getStickerViewTheme().setAlwaysShowDivider(true)
        UI.setCategoryStickers(AppUtils.readFileSticker())

        FirebaseApp.initializeApp(this)
    }


    @Synchronized
    fun getInstance(): SupplierApplication? {
        return instance
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}