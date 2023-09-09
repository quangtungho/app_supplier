package vn.techres.supplier.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.Html
import android.text.TextUtils.TruncateAt.MARQUEE
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aghajari.emojiview.AXEmojiUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.PictureFileUtils.getPath
import com.paginate.Paginate
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.squareup.okhttp.HttpUrl
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.socket.emitter.Emitter
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication.Companion.clipboard
import vn.techres.supplier.activity.SupplierApplication.Companion.context
import vn.techres.supplier.activity.SupplierApplication.Companion.getSocketInstance
import vn.techres.supplier.adapter.chatadapter.ChatAdapter
import vn.techres.supplier.adapter.chatadapter.GroupOrderAdapter
import vn.techres.supplier.adapter.chatadapter.ImageClipAdapter
import vn.techres.supplier.adapter.chatadapter.UserTagsAdapter
import vn.techres.supplier.base.BaseBindingActivity
import vn.techres.supplier.databinding.*
import vn.techres.supplier.helper.*
import vn.techres.supplier.helper.linkpreview.GetLinkPreviewListener
import vn.techres.supplier.helper.linkpreview.LinkPreview
import vn.techres.supplier.helper.linkpreview.LinkUtil
import vn.techres.supplier.helper.screenshot.ScreenshotDetectionDelegate
import vn.techres.supplier.helper.screenshot.ScreenshotDetectionDelegate.getFilePathClipFromContentResolver
import vn.techres.supplier.interfaces.*
import vn.techres.supplier.model.chat.data.*
import vn.techres.supplier.model.datamodel.DataListOrder
import vn.techres.supplier.model.entity.CurrentConfigJava.getConfigJava
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.entity.MessageOrderEmit
import vn.techres.supplier.model.eventbussms.EventBusStickerClick
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.*
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.math.ln


class ChatActivity : BaseBindingActivity<ActivityChatBinding>(), OnClickGroupOrder,
    RevokeMessageHandler,
    ChangeUserTag, ScreenshotDetectionDelegate.ScreenshotDetectionListener, ClickFullScreen,
    ClickImageClip {
    override val bindingInflater: (LayoutInflater) -> ActivityChatBinding
        get() = ActivityChatBinding::inflate
    var contentBinding: ItemChatBinding? = null
    var actionBinding: ItemChatViewBinding? = null
    var groupID = ""
    var code = ""
    var aBoolean = false
    var nCurrent = -1
    var groupType = 0
    private var currentSearch = 0
    private var isStrokeSearch = 0
    private var dataOrder = ArrayList<DataListOrder>()
    private var totalPage = 0
    private var currentPage = 1
    private var resultsPage = 0
    private var mPaginate: Paginate? = null
    private var isLoadingMore = false
    private var messageKey = ""
    private var isReplyMessage = 0
    private var isOrderMessage = 0
    private val fileChats: ArrayList<FileChat> = ArrayList()
    private var imagesChoose: ArrayList<LocalMedia> = ArrayList()
    private val reqCODESPEECHINPUT = 100
    private var messageReply: MessageReply? = null
    private var messageOrder: MessageOrder? = MessageOrder()
    private var detailGroup: DetailGroup? = null
    private var isLink = 0
    private var isFile = 0
    private var isDetectImage = 0
    var checkPosition = false
    var chatAdapter: ChatAdapter? = null
    var listMessages: ArrayList<MessagesByGroup>? = ArrayList()
    var userTagsAdapter: UserTagsAdapter? = null
    var dialogAdapter: GroupOrderAdapter? = null
    var supplierId = 0
    var restaurantId = 0
    private var messageLink: MessageLink? = null
    private var linkPreviewCallback: LinkPreviewCallback? = null
    private var linkPreviewClip: LinkPreviewCallback? = null
    private var video = 0
    var membersList = ArrayList<Members>()
    private var contentPaste: String? = null
    private var clipCopy: String? = null
    private var messageLinkClip: MessageLink? = null
    private var textCrawlerClip = TextCrawler()
    private var imageClip: ArrayList<String> = ArrayList()
    private val screenshotDetectionDelegate = ScreenshotDetectionDelegate(this, this)
    private var images: ImageClip = ImageClip()
    private val chatTagList: ArrayList<ChatTag> = ArrayList()
    private var imageClipAdapter: ImageClipAdapter? = null
    private var dialogOrder: BottomSheetDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        joinGroup()
        registrySocket()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        screenshotDetectionDelegate.startScreenshotDetection()
    }

    override fun onDestroy() {
        super.onDestroy()
        leaveRoom()

        screenshotDetectionDelegate.stopScreenshotDetection()

    }

    override fun onPause() {
        super.onPause()
        JCVideoPlayer.releaseAllVideos()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun searchEmployee(s: String) {
        if (s == "") {
            actionBinding!!.rcvUserTag.visibility = View.GONE
        } else
            actionBinding!!.rcvUserTag.visibility = View.VISIBLE
        userTagsAdapter!!.filter(s)
    }

    @SuppressLint("LogNotTimber")
    override fun onResume() {
        super.onResume()
        getAPIListOrder()
        getDetailGroup()

        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        if (clipboard.hasPrimaryClip()) {
            val primaryClipDataClipCopy: ClipData? = clipboard.primaryClip
            if (primaryClipDataClipCopy != null) {
                val itemClip: ClipData.Item = primaryClipDataClipCopy.getItemAt(0)
                if (!itemClip.text.equals("")) {
                    val clip: String = itemClip.text.toString()
                    Log.d("clip_Copy", String.format("clip_Copy %s:", clip))
                    var seconds: Long = 0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Log.d(
                            "clip_Copy", String.format(
                                "clipboard : %s",
                                clipboard.primaryClipDescription!!.timestamp
                            )
                        )
                        Log.d("clip_Copy", String.format("curren time : %s", Date().time))
                        seconds =
                            Date().time - clipboard.primaryClipDescription!!.timestamp
                        seconds = seconds / 1000 / 60
                    }
                    if (seconds <= 1.toLong()) {
                        if (clip.isNotEmpty()) {
                            textCrawlerClip = TextCrawler()
                            textCrawlerClip.makePreview(linkPreviewClip, clip)
                        }
                    }
                }
            }
        }

    }

    @SuppressLint("LogNotTimber")
    override fun onSetBodyView() {
        contentBinding = ItemChatBinding.inflate(layoutInflater)
        actionBinding = ItemChatViewBinding.inflate(layoutInflater)

        checkReadExternalStoragePermission()
        //Khoi tao layout
        binding.layoutMain.addView(actionBinding!!.root)
        actionBinding!!.layoutContainer.addView(contentBinding!!.root)
        groupID = intent.getStringExtra(TechresEnum.ID_GROUP.toString())!!
        restaurantId = intent.getIntExtra(TechresEnum.RESTAURANT_ID.toString(), 0)
        supplierId = intent.getIntExtra(TechresEnum.SUPPLIER_ID.toString(), 0)
        groupType = intent.getIntExtra(TechresEnum.GROUP_TYPE.toString(), 0)
        chatAdapter = ChatAdapter(this)
        userTagsAdapter = UserTagsAdapter(this)
        imageClipAdapter = ImageClipAdapter(this)
        chatAdapter!!.setClickFullScreen(this)

        //Get APi get message
        getPinnedMessage()
        getMessagePaginationByGroup()
        contentBinding!!.rclListImageClip.adapter = imageClipAdapter
        contentBinding!!.rclListImageClip.layoutManager =
            PreCachingLayoutManager(this, RecyclerView.HORIZONTAL, false)
        contentBinding!!.rclListImageClip.setHasFixedSize(true)
        contentBinding!!.rclListImageClip.itemAnimator = DefaultItemAnimator()

//        //Khoi tai usertag
//        actionBinding!!.rcvUserTag.adapter = userTagsAdapter
//        actionBinding!!.rcvUserTag.layoutManager =
//            PreCachingLayoutManager(this, RecyclerView.VERTICAL, false)
//        actionBinding!!.rcvUserTag.setHasFixedSize(true)
//
//        actionBinding!!.rcvUserTag.itemAnimator = DefaultItemAnimator()

//        //Setup emoji
        AppUtils.setupEmojiSticker(
            this,
            actionBinding!!.editTextMessage,
            actionBinding!!.imageViewEmoji,
            actionBinding!!.emojiPopupLayout,
            actionBinding!!.extensions
        )

        getImageClipPhoto()
        contentBinding!!.rcvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NotNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position: Int = getCurrentItem()
                    checkPosition = position != 0
                    if (position == 0) {
                        contentBinding!!.txtMoreMessage.visibility = View.GONE
                        contentBinding!!.scrollMessage.visibility = View.GONE
                    } else {
                        if (contentBinding!!.txtMoreMessage.visibility == View.VISIBLE) {
                            contentBinding!!.scrollMessage.visibility = View.GONE
                        } else contentBinding!!.scrollMessage.visibility = View.VISIBLE
                    }
                }
            }
        })
        //clip coppy
        linkPreviewClip = object : LinkPreviewCallback {
            override fun onPre() {}
            override fun onPos(sourceContent: SourceContent, isNull: Boolean) {
                if (isNull || sourceContent.finalUrl == "") {
                    actionBinding!!.ctlLinkMessage.visibility = View.GONE
                } else {
                    actionBinding!!.ctlLinkMessage.visibility = View.VISIBLE
                    actionBinding!!.link.rlnLink.visibility = View.GONE
                    actionBinding!!.link.rlnLinkClip.visibility = View.VISIBLE
                    contentBinding!!.rlnImageClip.visibility = View.GONE
                    actionBinding!!.link.youtube.visibility = View.GONE
                    actionBinding!!.link.chatAttachmentLinkTitleClip.text =
                        if (sourceContent.title != null) sourceContent.title else ""
                    actionBinding!!.link.chatAttachmentLinkDescriptionClip.text =
                        if (sourceContent.description != null) sourceContent.description else ""
                    AppUtils.callPhotoLocal(
                        sourceContent.images[0],
                        actionBinding!!.link.chatAttachmentLinkPhotoClip
                    )
                    messageLinkClip = MessageLink()
                    messageLinkClip!!.cannonical_url = sourceContent.url
                    messageLinkClip!!.description = sourceContent.description
                    messageLinkClip!!.media_thumb = sourceContent.images[0]
                    messageLinkClip!!.title = sourceContent.title
                    imageClip = ArrayList()
                }
            }
        }
        clipCopy = intent.getStringExtra(TechresEnum.CLIP.toString())
        if (clipCopy != null) {
            if (clipCopy!!.contains(getString(R.string.link_youtube_1)) || clipCopy!!.contains(
                    getString(R.string.link_youtube_2)
                )
            ) {
                val videoId: String = getVideo(clipCopy!!)
                try {
                    if (video == 0) {
                        actionBinding!!.link.youtube.addYouTubePlayerListener(object :
                            AbstractYouTubePlayerListener() {
                            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                                super.onReady(youTubePlayer)
                                actionBinding!!.ctlLinkMessage.visibility = View.VISIBLE
                                actionBinding!!.link.rlnLink.visibility = View.GONE
                                actionBinding!!.link.rlnLinkClip.visibility = View.VISIBLE
                                contentBinding!!.rlnImageClip.visibility = View.GONE
                                actionBinding!!.link.youtube.visibility = View.VISIBLE
                                actionBinding!!.link.linkThumbClip.visibility = View.GONE
                                messageLinkClip = MessageLink()
                                messageLinkClip!!.cannonical_url = clipCopy as String
                                youTubePlayer.cueVideo(videoId, 0f)
                                imageClip = ArrayList()
                                LinkUtil.getInstance().getLinkPreview(
                                    applicationContext,
                                    clipCopy!!,
                                    object : GetLinkPreviewListener {
                                        override fun onSuccess(link: LinkPreview) {
                                            messageLinkClip!!.description = link.description
                                            messageLinkClip!!.title = link.title
                                            messageLinkClip!!.media_thumb =
                                                link.imageFile.path
                                        }

                                        override fun onFailed(e: Exception?) {}
                                    })
                            }

                            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerError) {
                                super.onError(youTubePlayer, error)
                                if (error == PlayerError.VIDEO_NOT_FOUND) {
                                    messageLinkClip = MessageLink()
                                }
                            }
                        })
                    }
                } catch (ignored: Exception) {
                    ignored.printStackTrace()
                }
            } else if (Patterns.WEB_URL.matcher(clipCopy!!.chars().toString()).matches()) {
                if (HttpUrl.parse(clipCopy) != null) {
                    textCrawlerClip = TextCrawler()
                    textCrawlerClip.makePreview(linkPreviewClip, clipCopy)
                }
            }
        }
        actionBinding!!.link.sendClip.setOnClickListener {
            val messageChat = MessageChat(
                CurrentUser.getCurrentUser(baseContext)!!.id,
                groupID,
                "",
                SocketChatMessageTypeEnum.LINK.toString().toInt(),
                ArrayList(),
                messageLinkClip!!
            )
            AppUtils.emitSocket(SocketChatEmitDataEnum.CHAT_LINK.toString(), messageChat)
            actionBinding!!.link.rlnLinkClip.visibility = View.GONE
            actionBinding!!.link.youtube.visibility = View.GONE
            actionBinding!!.ctlLinkMessage.visibility = View.GONE
            actionBinding!!.link.youtube.release()
            imageClip = ArrayList()
            val data = ClipData.newPlainText("", "")
            clipboard.setPrimaryClip(data)
        }

        actionBinding!!.link.chatAttachmentLinkDeleteClip.setOnClickListener {
            actionBinding!!.link.rlnLinkClip.visibility = View.GONE
            actionBinding!!.link.youtube.visibility = View.GONE
            actionBinding!!.ctlLinkMessage.visibility = View.GONE
            imageClip = ArrayList()
        }

        //end clip coppy

        //Get detailGroup
        getDetailGroup()

        //Setup rcl message
        contentBinding!!.rcvChat.adapter = chatAdapter
        contentBinding!!.rcvChat.layoutManager =
            PreCachingLayoutManager(this, RecyclerView.VERTICAL, true)
        contentBinding!!.rcvChat.setHasFixedSize(true)
        contentBinding!!.rcvChat.itemAnimator = DefaultItemAnimator()
        chatAdapter!!.setRevokeMessageHandler(this)
        chatAdapter!!.setLifecycle(lifecycle)
        Log.d("height key :", actionBinding!!.emojiPopupLayout.popupHeight.toString())
        actionBinding!!.imageCamera.setOnClickListener {
            AppUtils.showImagePickerChat(
                this,
                PictureMimeType.ofAll(),
                PictureConfig.MULTIPLE,
                true,
                null
            )
            actionBinding!!.extensions.visibility = View.GONE
        }
        actionBinding!!.imgOrder.setOnClickListener {
            openDialogBox()
        }
        actionBinding!!.imageViewAttachment.setOnClickListener {
            if (actionBinding!!.emojiPopupLayout.visibility == View.VISIBLE) {
                actionBinding!!.emojiPopupLayout.visibility = View.GONE
            }
            hideKeyboard()
            if (actionBinding!!.extensions.visibility == View.VISIBLE) {
                actionBinding!!.extensions.visibility = View.GONE
            } else {
                actionBinding!!.extensions.visibility = View.VISIBLE
            }
        }
        setListenerExtension()
        binding.header.toolbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.header.toolbarAction2.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, AddMemberGroupChatActivity::class.java)
            intent.putExtra(TechresEnum.ID_GROUP.toString(), groupID)
            startActivity(intent)
        }
        binding.header.toolbarAction2.setOnClickListener {
            showSearch()
        }
        binding.backSearch.setOnClickListener {
            hideSearch()
            hideKeyboard()
        }
        binding.header.toolbarAction3.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, DetailGroupChatActivity::class.java)
            intent.putExtra(TechresEnum.ID_GROUP.toString(), groupID)
            startActivity(intent)
            actionBinding!!.extensions.visibility = View.GONE
        }

        binding.header.lnAvatar.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, DetailGroupChatActivity::class.java)
            intent.putExtra(TechresEnum.ID_GROUP.toString(), groupID)
            startActivity(intent)
            actionBinding!!.extensions.visibility = View.GONE
        }
        actionBinding!!.imageSend.setOnClickListener { send() }
        actionBinding!!.editTextMessage.addTextChangedListener(textMessageWatcher)
        initPaginate()
        actionBinding!!.chatReply.replyDeleteButton.setOnClickListener {
            isReplyMessage = 0
            actionBinding!!.ctlReplyMessage.visibility = View.GONE
            actionBinding!!.imageViewAttachment.visibility = View.VISIBLE
            actionBinding!!.imageCamera.visibility = View.VISIBLE
            actionBinding!!.imgOrder.visibility = View.VISIBLE
            actionBinding!!.imageSend.visibility = View.GONE
        }
        actionBinding!!.chatOrder.replyDeleteButton.setOnClickListener {
            isOrderMessage = 0
            actionBinding!!.ctlOrderMessage.visibility = View.GONE
            actionBinding!!.imageViewAttachment.visibility = View.VISIBLE
            actionBinding!!.imageCamera.visibility = View.VISIBLE
            actionBinding!!.imgOrder.visibility = View.VISIBLE
            actionBinding!!.imageSend.visibility = View.GONE


        }

        //vuốt trả lời tin nhắn cụ thể

        //vuốt trả lời tin nhắn cụ thể
        swipeReply()
        linkPreviewCallback = object : LinkPreviewCallback {
            override fun onPre() {}
            override fun onPos(sourceContent: SourceContent, isNull: Boolean) {
                if (isNull || sourceContent.finalUrl == "") {
                    actionBinding!!.ctlLinkMessage.visibility = View.GONE
                    isLink = 0
                } else {
                    isLink = 1
                    actionBinding!!.ctlLinkMessage.visibility = View.VISIBLE
                    actionBinding!!.link.rlnLink.visibility = View.VISIBLE
                    actionBinding!!.link.rlnLinkClip.visibility = View.GONE
                    contentBinding!!.rlnImageClip.visibility = View.GONE
                    actionBinding!!.link.youtube.visibility = View.GONE
                    imageClip = ArrayList()
                    actionBinding!!.link.youtube.visibility = View.GONE
                    actionBinding!!.link.linkThumb.visibility = View.VISIBLE
                    actionBinding!!.link.chatAttachmentLinkTitle.text =
                        if (sourceContent.title != null) sourceContent.title else ""
                    actionBinding!!.link.chatAttachmentLinkDescription.text =
                        if (sourceContent.description != null) sourceContent.description else ""
                    AppUtils.callPhotoLocal(
                        sourceContent.images[0],
                        actionBinding!!.link.chatAttachmentLinkPhoto
                    )
                    messageLink = MessageLink(
                        sourceContent.images[0],
                        sourceContent.url,
                        sourceContent.title,
                        sourceContent.description
                    )
                }
            }
        }
        actionBinding!!.link.chatAttachmentLinkDelete.setOnClickListener {
            isLink = 0
            messageLink = MessageLink()
            actionBinding!!.ctlLinkMessage.visibility = View.GONE
            video = 0
        }

        contentBinding!!.sendPhotoClip.setOnClickListener {
            isDetectImage = 1
            images.stringList.addAll(imageClip)
            images.time = Date().time.toInt() / 1000 / 60
            Log.d("clip_image2", images.stringList.size.toString())
            val gson = Gson()
            val clipImage: String =
                gson.toJson(images)
            Log.d("clip_image %s", clipImage)
            PrefUtils.getInstance(this)!!.putString(TechresEnum.IMAGE_CLIP.toString(), clipImage)
            messageKey = Math.random().toString()
            actionBinding!!.ctlLinkMessage.visibility = View.GONE
            contentBinding!!.rlnImageClip.visibility = View.GONE
            actionBinding!!.link.rlnLinkClip.visibility = View.GONE
            actionBinding!!.link.rlnLink.visibility = View.GONE
        }
        contentBinding!!.icnImageCLipDetele.setOnClickListener {
            images.stringList.addAll(imageClip)
            images.time = Date().time.toInt() / 1000 / 60
            val gson = Gson()
            val clipImage: String = gson.toJson(images)
            PrefUtils.getInstance(this)!!.putString(TechresEnum.IMAGE_CLIP.toString(), clipImage)
            contentBinding!!.rlnImageClip.visibility = View.GONE
            imageClip = ArrayList()
        }
        contentBinding!!.btnListPinned.setOnClickListener {
//            val intent = Intent(this, PinnedDetailActivity::class.java)
//            intent.putExtra(TechresEnum.ID_GROUP.toString(), groupID)
//            startActivity(intent)
        }

        userTagsAdapter!!.setChangeUserTag(this)
        contentBinding!!.openImage.setOnClickListener {
            contentBinding!!.lnView.visibility = View.GONE
            contentBinding!!.rltListImageClip.visibility = View.VISIBLE
        }
        contentBinding!!.closeImage.setOnClickListener {
            contentBinding!!.lnView.visibility = View.VISIBLE
            contentBinding!!.rltListImageClip.visibility = View.GONE
        }
        imageClipAdapter!!.setClickImageClip(this)
        contentBinding!!.sendListImageClip.setOnClickListener {
            isDetectImage = 1
            images.stringList.addAll(imageClip)
            images.time = Date().time.toInt() / 1000 / 60
            Log.d("clip_image2", images.stringList.size.toString())
            val gson = Gson()
            val clipImage: String = gson.toJson(images)
            Log.d("clip_image", clipImage)
            PrefUtils.getInstance(this)!!.putString(TechresEnum.IMAGE_CLIP.toString(), clipImage)
            contentBinding!!.rltListImageClip.visibility = View.GONE
            contentBinding!!.lnView.visibility = View.VISIBLE
            messageKey = Math.random().toString()

            contentBinding!!.rlnImageClip.visibility = View.GONE
        }
        contentBinding!!.cancelListImageClip.setOnClickListener {
            images.stringList.addAll(imageClip)
            images.time = Date().time.toInt() / 1000 / 60
            val gson = Gson()
            val clipImage: String = gson.toJson(images)
            PrefUtils.getInstance(this)!!.putString(TechresEnum.IMAGE_CLIP.toString(), clipImage)
            contentBinding!!.rlnImageClip.visibility = View.GONE
            imageClip = ArrayList()
        }
        binding.header.toolbarAction1.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, AddMemberGroupChatActivity::class.java)
            intent.putExtra(TechresEnum.ID_GROUP.toString(), groupID)
            startActivity(intent)
        }
        contentBinding!!.txtMoreMessage.setOnClickListener {
            contentBinding!!.rcvChat.scrollToPosition(0)
            contentBinding!!.txtMoreMessage.visibility = View.GONE
        }
        contentBinding!!.scrollMessage.setOnClickListener {
            contentBinding!!.rcvChat.scrollToPosition(0)
            contentBinding!!.scrollMessage.visibility = View.GONE
        }
    }

    private fun showSearch() {
//        if (listMessagePinned.size > 0) {
//            contentBinding.pinned.lnPinned.setVisibility(View.GONE)
//        }
        // messageSearch = ArrayList<MessagesByGroup>()
        binding.countMessSearch.visibility = View.VISIBLE
        binding.header.toolbar.visibility = View.GONE
        actionBinding!!.countMessSearch.visibility = View.VISIBLE
        actionBinding!!.inputChat.visibility = View.GONE
        binding.inputSearch.isFocusable = true
        binding.inputSearch.requestFocus()
        val imm = binding.inputSearch.context
            .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.inputSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideSearch() {
        binding.inputSearch.setText("")
        // messageSearch = ArrayList<MessagesByGroup>()
        binding.header.toolbar.visibility = View.VISIBLE
        actionBinding!!.inputChat.visibility = View.VISIBLE
        binding.countMessSearch.visibility = View.GONE
        actionBinding!!.countMessSearch.visibility = View.GONE
        actionBinding!!.messCountSearch.text = ""
        currentSearch = 0
        actionBinding!!.countUp.isEnabled = false
        actionBinding!!.countDown.isEnabled = false
        isStrokeSearch = 0
    }

    override fun onBackPressed() {
        if (!actionBinding!!.emojiPopupLayout.onBackPressed() && !actionBinding!!.emojiPopupLayout.isKeyboardOpen && actionBinding!!.extensions.visibility != View.VISIBLE) {
            super.onBackPressed()
        } else if (actionBinding!!.extensions.visibility == View.VISIBLE) {
            actionBinding!!.extensions.visibility = View.GONE
        } else {
            hideKeyboard()
        }
    }

    private fun joinGroup() {
        PrefUtils.getInstance(this)!!.putString(TechresEnum.ID_GROUP.toString(), groupID)
        PrefUtils.getInstance(this)!!.putBoolean(TechresEnum.IS_JOIN.toString(), true)
        AppUtils.emitSocket(
            SocketChatEmitDataEnum.JOIN_ROOM.toString(),
            GroupEmit(CurrentUser.getCurrentUser(this)!!.id, groupID)
        )
    }

    private fun leaveRoom() {
        unRegistrySocket()
        PrefUtils.getInstance(this)!!.putString(TechresEnum.ID_GROUP.toString(), groupID)
        PrefUtils.getInstance(this)!!.putBoolean(TechresEnum.IS_JOIN.toString(), false)
        AppUtils.emitSocket(
            SocketChatEmitDataEnum.LEAVE_ROOM.toString(),
            GroupEmit(
                CurrentUser.getCurrentUser(this)!!.id,
                groupID
            )
        )
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                this.getSystemService(
                    INPUT_METHOD_SERVICE
                ) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun unRegistrySocket() {
        getSocketInstance()?.off(SocketChatOnDataEnum.CHAT_TEXT.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.CHAT_IMAGE.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.CHAT_VIDEO.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.CHAT_STICKER.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.CHAT_LINK.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.CHAT_FILE.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.USER_IS_TYPING.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.REVOKE_MESSAGE.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.PINNED_MESSAGE_TEXT.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.PINNED_MESSAGE.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.REVOKE_PINNED_MESSAGE.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.RES_CHAT_ERROR.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.RES_REMOVE_GROUP.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.RES_ADD_NEW_USER.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.RES_REACTION_MESSAGE.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.RES_MESSAGE_VIEWED.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.RES_LIST_MESSAGE_VIEWED.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.RES_CHAT_VIDEO_CALL.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.RES_CHAT_AUDIO_CALL.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.RES_CHAT_BUSINESS_CARD.toString())
        getSocketInstance()?.off(SocketChatOnDataEnum.RES_CHAT_ORDER.toString())

    }

    private fun registrySocket() {
        getSocketInstance()?.on(SocketChatOnDataEnum.CHAT_TEXT.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.CHAT_IMAGE.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.CHAT_VIDEO.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.CHAT_STICKER.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.CHAT_LINK.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.CHAT_FILE.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.USER_IS_TYPING.toString(), onTyping)
        getSocketInstance()?.on(SocketChatOnDataEnum.REVOKE_MESSAGE.toString(), onRevoke)
        getSocketInstance()?.on(SocketChatOnDataEnum.CHAT_REPLY.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.PINNED_MESSAGE_TEXT.toString(), onPinned)
        getSocketInstance()?.on(SocketChatOnDataEnum.PINNED_MESSAGE.toString(), onPinnedHeader)
        getSocketInstance()?.on(
            SocketChatOnDataEnum.REVOKE_PINNED_MESSAGE.toString(),
            onRevokePinned
        )
        getSocketInstance()?.on(SocketChatOnDataEnum.RES_CHAT_ERROR.toString(), onError)
        getSocketInstance()?.on(SocketChatOnDataEnum.RES_REMOVE_GROUP.toString(), onRemoveGroup)
        getSocketInstance()?.on(SocketChatOnDataEnum.RES_ADD_NEW_USER.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.RES_REACTION_MESSAGE.toString(), onReaction)
        getSocketInstance()?.on(SocketChatOnDataEnum.RES_MESSAGE_VIEWED.toString(), onMessageView)
        getSocketInstance()?.on(
            SocketChatOnDataEnum.RES_LIST_MESSAGE_VIEWED.toString(),
            onListMessageView
        )
        //getSocketInstance().on(SocketChatOnDataEnum.RES_CALL_CONNECT.toString(), onCall)
        getSocketInstance()?.on(SocketChatOnDataEnum.RES_CHAT_VIDEO_CALL.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.RES_CHAT_AUDIO_CALL.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.RES_CHAT_BUSINESS_CARD.toString(), onMessage)
        getSocketInstance()?.on(SocketChatOnDataEnum.RES_CHAT_ORDER.toString(), onMessage)

    }

    private fun checkReadExternalStoragePermission() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestReadExternalStoragePermission()
//        }
    }

    private fun getCurrentItem(): Int {
        return (Objects.requireNonNull(contentBinding!!.rcvChat.layoutManager) as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }

    @SuppressLint("NotifyDataSetChanged", "LogNotTimber", "SetTextI18n")
    private fun getImageClipPhoto() {
        Log.d(
            "image clip :",
            PrefUtils.getInstance(this)!!.getString(TechresEnum.IMAGE_CLIP.toString())!!
        )
        if (!PrefUtils.getInstance(this)!!.getString(TechresEnum.IMAGE_CLIP.toString())
                .equals("")
        ) {
            val gson = Gson()
            images = gson.fromJson(
                PrefUtils.getInstance(this)!!.getString(TechresEnum.IMAGE_CLIP.toString()),
                ImageClip::class.java
            )
            Log.d("clip_image2", images.stringList.size.toString())
            if (images.stringList.isNotEmpty()) {
                imageClip = getFilePathClipFromContentResolver(this)
                imageClip = imageClip.stream()
                    .filter { x: String? ->
                        !images.stringList.contains(
                            x
                        )
                    }
                    .collect(Collectors.toList()) as ArrayList<String>
                if (imageClip.isNotEmpty()) {
                    if (isLink != 1) {
                        contentBinding!!.rlnImageClip.visibility = View.GONE
                        setImageClip()
                    }
                }
            }
        } else {
            imageClip = getFilePathClipFromContentResolver(this)
            if (imageClip.isNotEmpty()) {
                if (isLink != 1) {
                    contentBinding!!.rlnImageClip.visibility = View.GONE
                    setImageClip()
                }
            }
        }
        if (imageClip.isNotEmpty()) {
            if (imageClip.size > 1) {
                contentBinding!!.sendPhotoClip.text = "Gửi " + imageClip.size + " Ảnh"
                contentBinding!!.sendListImageClip.text = "Gửi " + imageClip.size + " Ảnh"
                contentBinding!!.openImage.visibility = View.VISIBLE
            } else {
                contentBinding!!.sendListImageClip.text = getString(R.string.send_now)
                contentBinding!!.sendPhotoClip.text = getString(R.string.send_now)
                contentBinding!!.openImage.visibility = View.GONE
            }
            imageClipAdapter = ImageClipAdapter(this)
            imageClipAdapter!!.setDataSource(imageClip)
            contentBinding!!.rclListImageClip.adapter = imageClipAdapter
            imageClipAdapter!!.notifyDataSetChanged()
        }
    }

    private fun setImageClip() {
        contentBinding!!.imageCLip.clipToOutline = true
        contentBinding!!.imageCLip2.clipToOutline = true
        contentBinding!!.imageCLip3.clipToOutline = true
        when (imageClip.size) {
            1 -> {
                AppUtils.callPhotoLocal(imageClip[0], contentBinding!!.imageCLip)
                contentBinding!!.cardView1.visibility = View.GONE
                contentBinding!!.cardView2.visibility = View.GONE
            }
            2 -> {
                AppUtils.callPhotoLocal(imageClip[imageClip.size - 1], contentBinding!!.imageCLip)
                AppUtils.callPhotoLocal(imageClip[imageClip.size - 2], contentBinding!!.imageCLip2)
                contentBinding!!.cardView1.visibility = View.VISIBLE
                contentBinding!!.cardView2.visibility = View.GONE
            }
            else -> {
                AppUtils.callPhotoLocal(imageClip[imageClip.size - 1], contentBinding!!.imageCLip)
                AppUtils.callPhotoLocal(imageClip[imageClip.size - 2], contentBinding!!.imageCLip2)
                AppUtils.callPhotoLocal(imageClip[imageClip.size - 3], contentBinding!!.imageCLip3)
                contentBinding!!.cardView1.visibility = View.VISIBLE
                contentBinding!!.cardView2.visibility = View.VISIBLE
            }
        }
    }

    private fun getVideo(url: String): String {
        var link: String? = ""
        val a: Int
        val b: Int
        if (url.contains(getString(R.string.link_youtube_1))) {
            a = url.indexOf(".be/")
            link = url.substring(a + 4)
        } else if (url.contains(getString(R.string.link_youtube_4))) {
            a = url.indexOf("?v=")
            if (url.contains("&")) {
                b = url.indexOf("&")
                link = url.substring(a + 3, b)
            } else link = url.substring(a + 3)
        } else if (url.contains(getString(R.string.link_youtube_3))) {
            a = url.indexOf("?")
            b = url.indexOf("shorts/")
            link = url.substring(b + 7, a)
        }
        return link!!
    }

    private fun setListenerExtension() {
        actionBinding!!.contact.setOnClickListener {
            val intent = Intent(applicationContext, ContractActivity::class.java)
            startActivity(intent)
        }


        actionBinding!!.file.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            try {
                startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    PictureConfig.CHOOSE_REQUEST_FILE
                )
            } catch (ex: java.lang.Exception) {
                println("browseClick :$ex")
            }
        }
    }

    private fun send() {
        var message: String
        if (chatTagList.isNotEmpty()) {
            message = Objects.requireNonNull(actionBinding!!.editTextMessage.text).toString()
            for (i in chatTagList.indices) {
                val tagName =
                    getString(R.string.font_tag_name_start) + chatTagList[i].full_name + "</font>" + " "
                message = message.replace(tagName, chatTagList[i].full_name)
            }
        } else {
            message = Objects.requireNonNull(actionBinding!!.editTextMessage.text).toString()
        }
        when {
            isReplyMessage == 1 -> {
                messageReply!!.message = message
                messageReply!!.list_tag_name = chatTagList
                AppUtils.emitSocket(SocketChatEmitDataEnum.CHAT_REPLY.toString(), messageReply)

            }
            isLink == 1 -> {
                val messageChat = MessageChat(
                    CurrentUser.getCurrentUser(this)!!.id,
                    groupID,
                    message,
                    SocketChatMessageTypeEnum.LINK.toString().toInt(),
                    ArrayList(),
                    messageLink!!
                )
                messageChat.list_tag_name = chatTagList
                AppUtils.emitSocket(SocketChatEmitDataEnum.CHAT_LINK.toString(), messageChat)
                val data = ClipData.newPlainText("", "")
                clipboard.setPrimaryClip(data)
            }
            isOrderMessage == 1 -> {
                messageOrder!!.supplier_id = CurrentUser.getCurrentUser(this)!!.supplier_id
                messageOrder!!.list_tag_name = chatTagList
                messageOrder!!.message = message
                messageOrder!!.message_type =
                    Integer.parseInt(SocketChatMessageTypeEnum.TEXT.toString())
                val messageOrderEmit =
                    MessageOrderEmit(
                        groupID,
                        messageOrder
                    )
                AppUtils.emitSocket(SocketChatEmitDataEnum.CHAT_ORDER.toString(), messageOrderEmit)
            }
            else -> {
                val messageChat =
                    MessageChat(
                        CurrentUser.getCurrentUser(this)!!.id,
                        groupID,
                        message,
                        SocketChatMessageTypeEnum.TEXT.toString().toInt()
                    )
                messageChat.list_tag_name = chatTagList
                AppUtils.emitSocket(SocketChatEmitDataEnum.CHAT_TEXT.toString(), messageChat)
            }
        }

        //Clear data
        actionBinding!!.editTextMessage.text!!.clear()
        actionBinding!!.ctlReplyMessage.visibility = View.GONE
        actionBinding!!.ctlLinkMessage.visibility = View.GONE
        actionBinding!!.rcvUserTag.visibility = View.GONE
        actionBinding!!.ctlOrderMessage.visibility = View.GONE
        isReplyMessage = 0
        isOrderMessage = 0
        video = 0
        isLink = 0
        actionBinding!!.link.youtube.release()
        messageLink = MessageLink()
        imageClip.clear()
        chatTagList.clear()
    }

    private val textMessageWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}


        @RequiresApi(api = Build.VERSION_CODES.N)
        override fun afterTextChanged(s: Editable) {
            if (s.isEmpty()) {
                chatTagList.clear()
                AppUtils.emitSocket(
                    SocketChatEmitDataEnum.JOIN_ROOM.toString(),
                    groupID
                )
                actionBinding!!.imageViewAttachment.visibility = View.VISIBLE
                actionBinding!!.imgOrder.visibility = View.VISIBLE
                actionBinding!!.imageCamera.visibility = View.VISIBLE
                actionBinding!!.imageSend.visibility = View.GONE
                actionBinding!!.link.youtube.visibility = View.GONE
                actionBinding!!.rcvUserTag.visibility = View.GONE
            } else {
                actionBinding!!.imageViewAttachment.visibility = View.GONE
                actionBinding!!.imgOrder.visibility = View.GONE
                actionBinding!!.imageCamera.visibility = View.GONE
                actionBinding!!.imageSend.visibility = View.VISIBLE
                actionBinding!!.link.youtube.visibility = View.VISIBLE
                imageClip = ArrayList()
                if (isLink == 0) {
                    actionBinding!!.ctlLinkMessage.visibility = LinearLayout.GONE
                }
                AppUtils.emitSocket(
                    SocketChatEmitDataEnum.JOIN_ROOM.toString(),
                    groupID
                )
                setTimeout { offTying() }
                contentPaste = ""
                Timber.d("contentPaste :%s ", contentPaste)
                if (s.toString().contains(contentPaste!!)) {
                    isLink = 1
                    if (contentPaste!!.contains(getString(R.string.link_youtube_1)) || contentPaste!!.contains(
                            getString(R.string.link_youtube_2)
                        )
                    ) {
                        try {
                            val videoId = AppUtils.getVideo(contentPaste)
                            if (video == 0) {
                                actionBinding!!.link.youtube.initialize(object :
                                    AbstractYouTubePlayerListener() {
                                    override fun onReady(youTubePlayer: YouTubePlayer) {
                                        super.onReady(youTubePlayer)
                                        youTubePlayer.cueVideo(videoId!!, 0f)
                                        actionBinding!!.ctlLinkMessage.visibility =
                                            LinearLayout.VISIBLE
                                        actionBinding!!.link.youtube.visibility =
                                            LinearLayout.VISIBLE
                                        actionBinding!!.link.rlnLink.visibility =
                                            LinearLayout.VISIBLE
                                        actionBinding!!.link.linkThumb.visibility =
                                            LinearLayout.GONE
                                        actionBinding!!.link.rlnLinkClip.visibility =
                                            LinearLayout.GONE
                                        video = 1
                                        messageLink = MessageLink()
                                        messageLink!!.cannonical_url = contentPaste.toString()
                                        com.mega4tech.linkpreview.LinkUtil.getInstance()
                                            .getLinkPreview(context,
                                                contentPaste.toString(), object :
                                                    com.mega4tech.linkpreview.GetLinkPreviewListener {
                                                    override fun onSuccess(link: com.mega4tech.linkpreview.LinkPreview) {
                                                        messageLink!!.description =
                                                            link.description
                                                        messageLink!!.title = link.title
                                                        messageLink!!.media_thumb =
                                                            link.imageFile.path
                                                    }

                                                    override fun onFailed(e: java.lang.Exception) {}
                                                })
                                    }

                                    override fun onError(
                                        youTubePlayer: YouTubePlayer,
                                        error: PlayerError
                                    ) {
                                        super.onError(youTubePlayer, error)
                                        if (error === PlayerError.VIDEO_NOT_FOUND) {
                                            isLink = 0
                                            video = 0
                                            actionBinding!!.link.rlnLinkClip.visibility =
                                                LinearLayout.GONE
                                            actionBinding!!.link.youtube.visibility =
                                                LinearLayout.GONE
                                            actionBinding!!.link.linkThumb.visibility =
                                                LinearLayout.GONE
                                            messageLink = MessageLink()
                                        }
                                    }
                                })
                            } else {
                                actionBinding!!.link.youtube.getYouTubePlayerWhenReady(object :
                                    YouTubePlayerCallback {
                                    override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                                        if (videoId != null) {
                                            youTubePlayer.cueVideo(videoId, 0f)
                                        }
                                        actionBinding!!.link.rlnLinkClip.visibility =
                                            LinearLayout.VISIBLE
                                        actionBinding!!.link.youtube.visibility =
                                            LinearLayout.VISIBLE
                                        actionBinding!!.link.rlnLink.visibility =
                                            LinearLayout.VISIBLE
                                        actionBinding!!.link.linkThumb.visibility =
                                            LinearLayout.GONE
                                        actionBinding!!.link.rlnLinkClip.visibility =
                                            LinearLayout.GONE
                                        messageLink = MessageLink()
                                        messageLink!!.cannonical_url = contentPaste.toString()
                                        com.mega4tech.linkpreview.LinkUtil.getInstance()
                                            .getLinkPreview(context,
                                                contentPaste.toString(), object :
                                                    com.mega4tech.linkpreview.GetLinkPreviewListener {
                                                    override fun onSuccess(link: com.mega4tech.linkpreview.LinkPreview) {
                                                        messageLink!!.description = link.description
                                                        messageLink!!.title = link.title
                                                        messageLink!!.media_thumb =
                                                            link.imageFile.path
                                                    }

                                                    override fun onFailed(e: java.lang.Exception) {}
                                                })
                                    }
                                })
                            }
                        } catch (e: java.lang.Exception) {
                            AppUtils.makeText(Objects.requireNonNull(e.message))
                        }
                    } else {
                        val linkPreviewCallback: LinkPreviewCallback =
                            object : LinkPreviewCallback {
                                override fun onPre() {}
                                override fun onPos(sourceContent: SourceContent, isNull: Boolean) {
                                    if (isNull || sourceContent.finalUrl == "") {
                                        actionBinding!!.link.rlnLinkClip.visibility =
                                            LinearLayout.GONE
                                        isLink = 0
                                    } else {
                                        isLink = 1
                                        actionBinding!!.link.rlnLinkClip.visibility =
                                            LinearLayout.VISIBLE
                                        actionBinding!!.link.rlnLink.visibility =
                                            LinearLayout.VISIBLE
                                        actionBinding!!.link.rlnLinkClip.visibility =
                                            LinearLayout.GONE
                                        contentBinding!!.rlnImageClip.visibility = LinearLayout.GONE
                                        actionBinding!!.link.youtube.visibility = LinearLayout.GONE
                                        imageClip = ArrayList()
                                        actionBinding!!.link.youtube.visibility = LinearLayout.GONE
                                        actionBinding!!.link.linkThumb.visibility =
                                            LinearLayout.VISIBLE
                                        actionBinding!!.link.chatAttachmentLinkTitle.text =
                                            if (sourceContent.title != null) sourceContent.title else ""
                                        actionBinding!!.link.chatAttachmentLinkDescription.text =
                                            if (sourceContent.description != null) sourceContent.description else ""
                                        AppUtils.callPhotoLocal(
                                            sourceContent.images[0],
                                            actionBinding!!.link.chatAttachmentLinkPhoto
                                        )
                                        messageLink = MessageLink(
                                            sourceContent.images[0],
                                            sourceContent.url,
                                            sourceContent.title,
                                            sourceContent.description
                                        )
                                    }
                                }
                            }
                        val textCrawler = TextCrawler()
                        textCrawler.makePreview(linkPreviewCallback, contentPaste)
                    }
                }
            }
        }


        override fun onTextChanged(
            charSequence: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            val s = charSequence.toString()
            val arrS = s.split("\\s".toRegex()).toTypedArray()
            if (arrS.isNotEmpty()) {
                val sizeArr = arrS.size - 1
                if (arrS[sizeArr] != "" && arrS[sizeArr][0] == '@' && arrS[sizeArr].isNotEmpty()) {
                    actionBinding!!.rcvUserTag.visibility = View.VISIBLE
                    val nameMain = arrS[sizeArr].substring(1)
                    searchEmployee(nameMain)

                } else if (arrS[sizeArr] != "" && arrS[sizeArr].length >= 3) {
                    actionBinding!!.rcvUserTag.visibility = View.VISIBLE
                    val nameMain = arrS[sizeArr].substring(1)
                    searchEmployee(nameMain)
                } else actionBinding!!.rcvUserTag.visibility = View.GONE
            }
        }
    }

    private fun setTimeout(runnable: Runnable) {
        Thread {
            try {
                Thread.sleep(5000)
                runnable.run()
            } catch (ignored: java.lang.Exception) {
            }
        }.start()
    }

    private fun offTying() {
        AppUtils.emitSocket(
            SocketChatEmitDataEnum.USER_IS_NOT_TYPING.toString(),
            GroupEmit(CurrentUser.getCurrentUser(this)!!.id, groupID)
        )
    }

    private fun initPaginate() {
        if (mPaginate == null) {
            val callbacks: Paginate.Callbacks = object : Paginate.Callbacks {
                override fun onLoadMore() {
                    if (currentPage < resultsPage) {
                        currentPage += 1
                        getMessagePaginationByGroupLast(currentPage)
                    } else isLoadingMore = false
                }

                override fun isLoading(): Boolean {
                    return isLoadingMore
                }

                override fun hasLoadedAllItems(): Boolean {
                    return if (totalPage < MessageChatGroup.LIMIT_PAGE_MESSAGE) true else currentPage == resultsPage
                }
            }
            mPaginate = Paginate.with(contentBinding!!.rcvChat, callbacks)
                .setLoadingTriggerThreshold(0)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(CustomLoadingListItemCreator(contentBinding!!.rcvChat))
                .build()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onScreenCaptured(s: String?) {
        if (isLink != 1) {
            imageClip = imageClip.stream().filter { x: String -> !x.contains(s!!) }
                .collect(Collectors.toList<Any>()) as ArrayList<String>
            if (s != null) {
                imageClip.add(s)
            }
            if (imageClip.size > 1) {
                contentBinding!!.sendPhotoClip.text = "Gửi " + imageClip.size + " Ảnh"
                contentBinding!!.sendListImageClip.text = "Gửi " + imageClip.size + " Ảnh"
                contentBinding!!.openImage.visibility = View.VISIBLE
            } else {
                contentBinding!!.openImage.visibility = View.GONE
                contentBinding!!.sendListImageClip.text = getString(R.string.send_now)
            }
            if (imageClip.size > 0) {
                imageClipAdapter = ImageClipAdapter(this)
                imageClipAdapter!!.setDataSource(imageClip)
                contentBinding!!.rclListImageClip.adapter = imageClipAdapter
                imageClipAdapter!!.notifyDataSetChanged()
            }
            actionBinding!!.ctlLinkMessage.visibility = View.VISIBLE
            contentBinding!!.rlnImageClip.visibility = View.GONE
            actionBinding!!.link.rlnLinkClip.visibility = View.GONE
            actionBinding!!.link.rlnLink.visibility = View.GONE
            actionBinding!!.link.youtube.visibility = View.GONE
            setImageClip()
        }
    }

    override fun onScreenCapturedWithDeniedPermission() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onClick(name: String?, id: Int?, avatar: String?) {
        val randomTag = Math.random().toString()
        chatTagList.add(ChatTag(id!!, avatar!!, name!!, randomTag))
        val s: Array<String> =
            Objects.requireNonNull(actionBinding!!.editTextMessage.text).toString()
                .split("\\s").toTypedArray()
        val text = StringBuilder()
        for (i in 0 until s.size - 1) {
            text.append(" ").append(s[i])
        }
        var textMessage = text.toString()
        if (chatTagList.size > 0) {
            for (i in chatTagList.indices) {
                val tagName =
                    getString(R.string.font_tag_name_start) + "@" + chatTagList[i].full_name + "</font>" + " "
                textMessage = textMessage.replace("@" + chatTagList[i].full_name, tagName)
            }
        }
        val message =
            textMessage + getString(R.string.font_tag_name_start) + "@" + name + "</font>" + " "
        actionBinding!!.editTextMessage.setText(
            Html.fromHtml(
                message,
                Html.FROM_HTML_MODE_COMPACT
            )
        )
        actionBinding!!.editTextMessage.setSelection(
            actionBinding!!.editTextMessage.text!!.length
        )
        actionBinding!!.rcvUserTag.visibility = View.GONE
    }


    override fun clickFullScreen(url: String?, seconds: Float, position: Int) {

    }

    override fun clickPictureInPicture(url: String?, seconds: Float, position: Int) {

    }

    override fun clickDelete(position: Int) {

    }

    override fun clickImage(position: Int) {
        AppUtils.showImageMediaLocalSlider(imageClip, position)
    }

    override fun onRevoke(messagesByGroup: MessagesByGroup?, view: View?) {
        setLongClickItemMessage(messagesByGroup!!)
    }

    override fun onRevokeClick(messagesByGroup: MessagesByGroup?, view: View?, view1: View?) {

    }

    override fun onRevokeEmoji(
        messagesByGroup: MessagesByGroup?,
        view: View?,
        reactionItems: List<ReactionItem?>?,
        y: Int
    ) {

    }

    @SuppressLint("LogNotTimber")
    private val onMessage: Emitter.Listener =
        Emitter.Listener { args ->
            Thread {
                runOnUiThread {
                    Log.e("MessageChatResponse :", java.lang.String.valueOf(args[0]))
                    val gson = Gson()
                    val message: MessagesByGroup =
                        gson.fromJson(
                            args[0].toString(),
                            MessagesByGroup::class.java
                        )
                    contentBinding!!.noMessage.visibility = View.GONE
                    if ((message.message_type == SocketChatMessageTypeEnum.IMAGE.toString()
                            .toInt() || message.message_type == SocketChatMessageTypeEnum.VIDEO.toString()
                            .toInt() || message.message_type == SocketChatMessageTypeEnum.FILE.toString()
                            .toInt()) && CurrentUser.getCurrentUser(this)!!.id == message.sender.member_id && message.sender.app_name == "supplier" && !message.random_key.equals("")
                    ) {
                        for (i in listMessages!!.indices) {
                            if (listMessages!![i].key_message == message.key_message) {
                                listMessages!![i].files = message.files
                                listMessages!![i].random_key = message.random_key
                                break
                            }
                        }
                    } else {
                        listMessages!!.add(0, message)
                        chatAdapter!!.notifyItemInserted(0)
                        chatAdapter!!.notifyItemChanged(1)
                        if (message.sender.member_id == CurrentUser.getCurrentUser(this)!!.id && message.sender.app_name == "supplier") {
                            contentBinding!!.rcvChat.scrollToPosition(0)
                            contentBinding!!.txtMoreMessage.visibility = View.GONE
                        } else {
                            if (checkPosition) {
                                if (contentBinding!!.scrollMessage.visibility == View.VISIBLE) {
                                    contentBinding!!.scrollMessage.visibility =
                                        View.GONE
                                }
                                contentBinding!!.txtMoreMessage.visibility =
                                    View.VISIBLE
                            } else {
                                contentBinding!!.txtMoreMessage.visibility = View.GONE
                                contentBinding!!.rcvChat.scrollToPosition(0)
                            }
                        }
                    }
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onError: Emitter.Listener =
        Emitter.Listener { args ->
            Thread {
                runOnUiThread {
                    Log.e(
                        "ErrorChatResponse : %s",
                        args[0].toString()
                    )
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onTyping: Emitter.Listener =
        Emitter.Listener { args ->
            Thread {
                runOnUiThread {
                    Log.e("TypingChatResponse:", args[0].toString())
                    val gson = Gson()
                    val tyingChat: ObjectTyingChat =
                        gson.fromJson(
                            args[0].toString(),
                            ObjectTyingChat::class.java
                        )
                    if (tyingChat.member_id == CurrentUser.getCurrentUser(this)!!.id) {
                        contentBinding!!.lnTyping.visibility = View.GONE
                    } else {
                        contentBinding!!.lnTyping.visibility = View.VISIBLE
                        AppUtils.callPhotoAvatar(tyingChat.avatar, contentBinding!!.cirImage)
                        setTimeout { contentBinding!!.lnTyping.visibility = View.GONE }
                    }
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onRemoveGroup: Emitter.Listener =
        Emitter.Listener { args ->
            Thread {
                Log.e("Socket RES_REMOVE_GROUP %s", java.lang.String.valueOf(args[0]))
                val gson = Gson()
                val socketGroup: SocketRemoveGroup = gson.fromJson(
                    args[0].toString(),
                    SocketRemoveGroup::class.java
                )
                if (groupID == socketGroup.group_id) {
                    //    showMessage(getString(android.R.string.remove_group))
                    onBackPressed()
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onRevoke: Emitter.Listener =
        Emitter.Listener { args ->
            Thread {
                runOnUiThread {
                    Log.e("RevokeChatResponse:", java.lang.String.valueOf(args[0]))
                    val gson = Gson()
                    val message: MessagesByGroup =
                        gson.fromJson(
                            args[0].toString(),
                            MessagesByGroup::class.java
                        )
                    for (i in listMessages!!.indices) {
                        if (listMessages!![i].random_key == message.random_key) {
                            listMessages!![i].status = 0
                            chatAdapter!!.notifyItemChanged(i)
                            break
                        }
                    }
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onReaction: Emitter.Listener =
        Emitter.Listener { args ->
            Thread {
                runOnUiThread {
                    Log.d("ReactionChatResponse", java.lang.String.valueOf(args[0]))
                    val gson = Gson()
                    val message: MessagesByGroup =
                        gson.fromJson(
                            args[0].toString(),
                            MessagesByGroup::class.java
                        )
                    for (i in listMessages!!.indices) {
                        if (listMessages!![i].random_key == message.random_key) {
                            listMessages!![i].reactions = message.reactions
                            chatAdapter!!.notifyItemChanged(i)
                            break
                        }
                    }
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onMessageView: Emitter.Listener =
        Emitter.Listener { args ->
            Thread {
                runOnUiThread {
                    Log.d("MessageViewResponse", args[0].toString())
                    val gson = Gson()
                    val messageView: MessageView = gson.fromJson(
                        args[0].toString(),
                        MessageView::class.java
                    )
                    if (listMessages!!.size > 0) {
                        val messageViewList =
                            listMessages!![0].message_viewed
                        messageViewList.add(messageView)
                        listMessages!![0].message_viewed = messageViewList
                        chatAdapter!!.notifyItemChanged(0)
                    }
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onListMessageView: Emitter.Listener =
        Emitter.Listener { args ->
            Thread {
                runOnUiThread {
                    Log.d("MessageListViewResponse %s", args[0].toString())
                    val gson = Gson()
                    val token: TypeToken<ArrayList<MessageView?>?> =
                        object : TypeToken<ArrayList<MessageView?>?>() {}
                    val messageViewList: ArrayList<MessageView> =
                        gson.fromJson(java.lang.String.valueOf(args[0]), token.type)
                    listMessages!![0].message_viewed = messageViewList
                    chatAdapter!!.notifyItemChanged(0)
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onPinned =
        Emitter.Listener { args: Array<Any> ->
            Thread {
                runOnUiThread {
                    Log.d("PinnedChatResponse: %s", args[0].toString())
                    val gson = Gson()
                    val message: MessagesByGroup =
                        gson
                            .fromJson(
                                args[0].toString(),
                                MessagesByGroup::class.java
                            )
                    listMessages!!.add(0, message)
                    chatAdapter!!.notifyItemInserted(0)
                    chatAdapter!!.notifyItemChanged(1)
                    if ((message.sender.member_id == CurrentUser.getCurrentUser(this)!!.id) && message.sender.app_name == "supplier"
                    ) {
                        contentBinding!!.rcvChat.scrollToPosition(0)
                        contentBinding!!.txtMoreMessage.visibility = View.GONE
                    } else {
                        if (checkPosition) {
                            if (contentBinding!!.scrollMessage.visibility == View.VISIBLE) {
                                contentBinding!!.scrollMessage.visibility =
                                    View.GONE
                                contentBinding!!.txtMoreMessage.visibility =
                                    View.VISIBLE
                            } else contentBinding!!.txtMoreMessage.visibility =
                                View.VISIBLE
                        } else {
                            contentBinding!!.txtMoreMessage.visibility = View.GONE
                            contentBinding!!.rcvChat.scrollToPosition(0)
                        }
                    }

                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onPinnedHeader =
        Emitter.Listener { args: Array<Any> ->
            Thread {
                runOnUiThread {
                    Log.d("PinnedChatResponse: %s", args[0].toString())
                    val gson = Gson()
                    val message: MessagesByGroup =
                        gson
                            .fromJson(
                                args[0].toString(),
                                MessagesByGroup::class.java
                            )
                    setUpPinnedMessage(message)
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onRevokePinned =
        Emitter.Listener { args: Array<Any> ->
            Thread {
                runOnUiThread {
                    Log.d("PinnedChatResponse:", args[0].toString())
                    val gson = Gson()
                    val message: MessagesByGroup =
                        gson
                            .fromJson(
                                args[0].toString(),
                                MessagesByGroup::class.java
                            )
                    listMessages!!.add(0, message)
                    chatAdapter!!.notifyItemInserted(0)
                    chatAdapter!!.notifyItemChanged(1)
                    if ((message.sender.member_id == CurrentUser.getCurrentUser(this)!!.id) && message.sender.app_name == "supplier") {
                        contentBinding!!.rcvChat.scrollToPosition(0)
                        contentBinding!!.txtMoreMessage.visibility = View.GONE
                    } else {
                        if (checkPosition) {
                            if (contentBinding!!.scrollMessage.visibility == View.VISIBLE) {
                                contentBinding!!.scrollMessage.visibility =
                                    View.GONE
                            }
                            contentBinding!!.txtMoreMessage.visibility =
                                View.VISIBLE
                        } else {
                            contentBinding!!.txtMoreMessage.visibility = View.GONE
                            contentBinding!!.rcvChat.scrollToPosition(0)
                        }
                    }
                    contentBinding!!.lnPinned.visibility = View.GONE
                }
            }.start()
        }

    private fun getMessagePaginationByGroup() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url = "/api/message-tms-supplier?limit=20&page=1&group_id=$groupID"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, this
        )
            .getMessagePaginationByGroup(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MessageByGroupResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Toast.makeText(
                        this@ChatActivity,
                        getString(R.string.onError),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged", "LogNotTimber")
                override fun onNext(response: MessageByGroupResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listMessages!!.clear()
                        listMessages!!.addAll(response.data!!.list!!)
                        chatAdapter!!.setDataSource(listMessages!!)
                        chatAdapter!!.notifyDataSetChanged()
                        if (listMessages!!.size == 0) {
                            contentBinding!!.noMessage.visibility = View.VISIBLE
                        } else contentBinding!!.noMessage.visibility = View.GONE
                        totalPage = response.data!!.total_record
                        resultsPage = if (totalPage % MessageChatGroup.LIMIT_PAGE_MESSAGE == 0) {
                            totalPage / MessageChatGroup.LIMIT_PAGE_MESSAGE
                        } else totalPage / MessageChatGroup.LIMIT_PAGE_MESSAGE + 1
                        Log.e("ResultsPageChat :", resultsPage.toString())
                        contentBinding!!.rcvChat.scrollToPosition(0)
                    }
                }
            })

    }

    private fun getMessagePaginationByGroupLast(page: Int?) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url = "/api/message-tms-supplier?limit=20&page=$page&group_id=$groupID"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, this
        )
            .getMessagePaginationByGroup(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MessageByGroupResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: MessageByGroupResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listMessages!!.addAll(response.data!!.list!!)
                        chatAdapter!!.notifyItemRangeInserted(
                            listMessages!!.size,
                            response.data!!.list!!.size

                        )
                    }else {
                        Toast.makeText(this@ChatActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun getAPIListOrder() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders?time&payment_status=-1&status=${TechresEnum.PAYMENT_STATUS}&status=${TechresEnum.STATUS_1},${TechresEnum.STATUS_3},${TechresEnum.STATUS_4},${TechresEnum.STATUS_5},${TechresEnum.STATUS_6},${TechresEnum.STATUS_7}&page=${TechresEnum.STATUS_1}&limit=${TechresEnum.LIMIT_1000}&get_time_store=${TechresEnum.GET_TIME_STORE_2}&restaurant_id=$restaurantId"
        ServiceFactory.createRetrofitService(
            TechResService::class.java, this
        )
            .getListOrder(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<OrderResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: OrderResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataOrder = response.data.list
                        dialogAdapter?.setDataSource(dataOrder)
                    }else {
                        Toast.makeText(this@ChatActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun getDetailGroup() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url =
            "/api/group-tms-supplier/${intent.getStringExtra(TechresEnum.ID_GROUP.toString())}/detail"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, this
        )
            .getDetailGroup(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DetailGroupResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {

                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: DetailGroupResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        if (response.data != null) {
                            if (contentBinding != null && actionBinding != null) {
                                detailGroup = response.data
                                AppUtils.callGroupAvatar(
                                    detailGroup!!.avatar,
                                    binding.header.imvAvatar
                                )
                                binding.header.tvTitle.text = detailGroup!!.restaurant_name
                                binding.header.tvTitle.ellipsize = MARQUEE
                                binding.header.tvTitle.marqueeRepeatLimit = -1
                                binding.header.tvTitle.isSelected = true
                                binding.header.tvCountMember.text = String.format(
                                    "%s %s",
                                    detailGroup!!.number_employee,
                                    getString(R.string.member)
                                )

                            }
                        }
                        membersList.clear()
                        val members = Members("All", -1)
                        var list = ArrayList<Members>()
                        list.add(0, members)
                        list.addAll(response.data!!.members)
                        list = list.stream()
                            .filter { x -> x.member_id != CurrentUser.getCurrentUser(context)!!.id }
                            .collect(Collectors.toList()) as ArrayList<Members>
                        membersList.addAll(list)
                        AppUtils.initRecyclerView(actionBinding!!.rcvUserTag, userTagsAdapter)
                        userTagsAdapter!!.setDataSource(membersList)
                    } else {
                        Toast.makeText(this@ChatActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                actionBinding!!.link.rlnLinkClip.visibility = View.GONE
                actionBinding!!.ctlLinkMessage.visibility = View.GONE
                contentBinding!!.rlnImageClip.visibility = View.GONE
                actionBinding!!.link.youtube.visibility = View.GONE
                imageClip = ArrayList()
                imagesChoose =
                    PictureSelector.obtainMultipleResult(data) as ArrayList<LocalMedia>
                messageKey = Math.random().toString()
                uploadFileLocal(imagesChoose)
                for (media in imagesChoose) {
                    uploadFileChatToServer(
                        this,
                        media.realPath.trim(),
                        groupID,
                        media.size,
                        media.width,
                        media.height
                    )
                }
            } else if (requestCode == reqCODESPEECHINPUT) {
                if (null != data) {
                    val result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    actionBinding!!.editTextMessage.append(result!![0])
                }
            }
            if (requestCode == PictureConfig.CHOOSE_REQUEST_FILE) {
                actionBinding!!.link.rlnLinkClip.visibility = View.GONE
                actionBinding!!.ctlLinkMessage.visibility = View.GONE
                contentBinding!!.rlnImageClip.visibility = View.GONE
                actionBinding!!.link.youtube.visibility = View.GONE
                isFile = 1
                imageClip = ArrayList()
                try {
                    val uri: Uri? = data!!.data
                    var path: String = getPath(this, uri)!!
                    val file = File(path)
                    path = file.path
                    uploadFileMessageLocal(file)
                    uploadFileChatToServer(
                        this,
                        path.trim(),
                        groupID,
                        file.length(),
                        0,
                        0
                    )
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun uploadFileLocal(file: List<LocalMedia>) {
        val fileChats: MutableList<FileChat> = ArrayList()
        for (localFile in file) {
            fileChats.add(
                FileChat(
                    localFile.fileName,
                    localFile.realPath,
                    localFile.realPath,
                    localFile.size
                )
            )
        }
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val currentTime: String = sdf.format(Date())
        val message = MessagesByGroup()
        message.sender = Sender(
            CurrentUser.getCurrentUser(this)!!.name,
            CurrentUser.getCurrentUser(this)!!.avatar,
            CurrentUser.getCurrentUser(this)!!.id
        )
        message.today = 0
        message.reactions = Reactions()
        message.files = fileChats
        if (isFile == 0) {
            if (file[0].mimeType == "video/mp4") {
                message.message_type =
                    SocketChatMessageTypeEnum.VIDEO.toString().toInt()
            } else message.message_type =
                SocketChatMessageTypeEnum.IMAGE.toString().toInt()
        } else {
            message.message_type = SocketChatMessageTypeEnum.FILE.toString().toInt()
        }
        message.interval_of_time = currentTime
        message.created_at = currentTime
        message.message = ""
        message.message_viewed = ArrayList()
        message.status = 1
        message.key_message = messageKey
        contentBinding!!.noMessage.visibility = View.GONE
        listMessages!!.add(0, message)
        chatAdapter!!.notifyItemInserted(0)
        chatAdapter!!.notifyItemChanged(1)
        contentBinding!!.rcvChat.scrollToPosition(0)
    }

    private fun uploadFileMessageLocal(file: File) {
        val fileChats: MutableList<FileChat> = ArrayList()
        fileChats.add(
            FileChat(
                file.name,
                file.absolutePath,
                file.absolutePath,
                file.length()
            )
        )
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val currentTime = sdf.format(Date())
        val message = MessagesByGroup()
        message.sender = Sender(
            CurrentUser.getCurrentUser(this)!!.name,
            CurrentUser.getCurrentUser(this)!!.avatar,
            CurrentUser.getCurrentUser(this)!!.id
        )
        message.today = 0
        message.reactions = Reactions()
        message.files = fileChats
        message.message_type = SocketChatMessageTypeEnum.FILE.toString().toInt()
        message.created_at = currentTime
        message.message = ""
        message.message_viewed = ArrayList()
        message.status = 1
        message.key_message = messageKey
        contentBinding!!.noMessage.visibility = View.GONE
        listMessages!!.add(0, message)
        chatAdapter!!.notifyItemInserted(0)
        chatAdapter!!.notifyItemChanged(1)
        contentBinding!!.rcvChat.scrollToPosition(0)
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
                        uploadNameFile(response.data)
                    } else
                        Toast.makeText(
                            this@ChatActivity,
                            response.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                }
            })
    }

    fun uploadNameFile(data: FileChat) {
        if (isFile == 0) {
            if (AppUtils.checkMimeTypeVideo(data.link_original!!)) {
                fileChats.clear()
                fileChats.add(data)
                AppUtils.emitSocket(
                    SocketChatEmitDataEnum.CHAT_VIDEO.toString(),
                    MessageChat(
                        CurrentUser.getCurrentUser(this)!!.id,
                        groupID,
                        SocketChatMessageTypeEnum.VIDEO.toString().toInt(),
                        fileChats,
                        messageKey
                    )
                )
                fileChats.clear()
            } else {
                if (isDetectImage == 1) {
                    fileChats.add(data)
                    if (fileChats.size == imageClip.size) {
                        AppUtils.emitSocket(
                            SocketChatEmitDataEnum.CHAT_IMAGE.toString(),
                            MessageChat(
                                CurrentUser.getCurrentUser(this)!!.id,
                                groupID,
                                SocketChatMessageTypeEnum.IMAGE.toString().toInt(),
                                fileChats,
                                messageKey
                            )
                        )
                        fileChats.clear()
                        imageClip.clear()
                        isDetectImage = 0
                    }
                } else {
                    fileChats.add(data)
                    if (fileChats.size == imagesChoose.size) {
                        AppUtils.emitSocket(
                            SocketChatEmitDataEnum.CHAT_IMAGE.toString(),
                            MessageChat(
                                CurrentUser.getCurrentUser(this)!!.id,
                                groupID,
                                SocketChatMessageTypeEnum.IMAGE.toString().toInt(),
                                fileChats,
                                messageKey
                            )
                        )
                        fileChats.clear()
                    }
                }
            }
        } else {
            fileChats.clear()
            fileChats.add(data)
            AppUtils.emitSocket(
                SocketChatEmitDataEnum.CHAT_FILE.toString(),
                MessageChat(
                    CurrentUser.getCurrentUser(this)!!.id,
                    groupID,
                    SocketChatMessageTypeEnum.FILE.toString().toInt(),
                    fileChats,
                    messageKey
                )
            )
            fileChats.clear()
            isFile = 0
        }
    }

    @SuppressLint("LogNotTimber")
    private fun uploadFileChat(
        activity: Activity?,
        photo: String?,
        groupID: String?
    ) {
        val serverUrlString: String = getConfigJava(context).ads_domain + String.format(
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun setLongClickItemMessage(messages: MessagesByGroup) {
        if (messages.status != 0) {
            val binding =
                ChatSelectionMessageBottomViewBinding.inflate(
                    layoutInflater
                )
            val dialog = BottomSheetDialog(this, R.style.SheetDialog)
            dialog.setContentView(binding.root)
            dialog.show()
            if (messages.sender.member_id != CurrentUser.getCurrentUser(this)!!.id) {
                binding.btnRevoke.visibility = View.GONE
            } else {
                binding.btnRevoke.visibility = View.VISIBLE
            }
            if (messages.message_type == SocketChatMessageTypeEnum.IMAGE.toString()
                    .toInt() || messages.message_type == SocketChatMessageTypeEnum.STICKER.toString()
                    .toInt() || messages.message_type == SocketChatMessageTypeEnum.VIDEO.toString()
                    .toInt() || messages.message_type == SocketChatMessageTypeEnum.FILE.toString()
                    .toInt()
            ) {
                binding.imvCopy.setImageResource(R.drawable.icn_csc_menu_download_n)
                binding.tvCopy.text = getString(R.string.download)
            } else {
                binding.imvCopy.setImageResource(R.drawable.icn_csc_menu_copy_n)
                binding.tvCopy.text = getString(R.string.copy_my_chat)
            }
            binding.likeItem.setOnClickListener {
                AppUtils.emitSocket(
                    SocketChatEmitDataEnum.REACTION_MESSAGE.toString(),
                    SocketReactionMessage(3, messages.random_key, groupID)
                )
                dialog.dismiss()
            }
            binding.loveItem.setOnClickListener {
                AppUtils.emitSocket(
                    SocketChatEmitDataEnum.REACTION_MESSAGE.toString(),
                    SocketReactionMessage(1, messages.random_key, groupID)
                )
                dialog.dismiss()
            }
            binding.smileItem.setOnClickListener {
                AppUtils.emitSocket(
                    SocketChatEmitDataEnum.REACTION_MESSAGE.toString(),
                    SocketReactionMessage(2, messages.random_key, groupID)
                )
                dialog.dismiss()
            }
            binding.wowItem.setOnClickListener {
                AppUtils.emitSocket(
                    SocketChatEmitDataEnum.REACTION_MESSAGE.toString(),
                    SocketReactionMessage(6, messages.random_key, groupID)
                )
                dialog.dismiss()
            }
            binding.angryItem.setOnClickListener {
                AppUtils.emitSocket(
                    SocketChatEmitDataEnum.REACTION_MESSAGE.toString(),
                    SocketReactionMessage(5, messages.random_key, groupID)
                )
                dialog.dismiss()
            }
            binding.sadItem.setOnClickListener {
                AppUtils.emitSocket(
                    SocketChatEmitDataEnum.REACTION_MESSAGE.toString(),
                    SocketReactionMessage(4, messages.random_key, groupID)
                )
                dialog.dismiss()
            }
            binding.btnPinned.setOnClickListener {
                sendPinned(messages)
                dialog.dismiss()
            }
            binding.btnCopy.setOnClickListener {
                when (messages.message_type) {
                    1, 7, 8 -> {
                        if (messages.message != "") {
                            AppUtils.copyText(this, messages.message)
                        } else AppUtils.copyText(
                            this,
                            messages.message_link!!.cannonical_url
                        )
                        Toast.makeText(
                            this,
                            getString(R.string.copy_chat),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    2, 5, 3 -> AppUtils.downloadFile(
                        this,
                        messages.files[0].link_original
                    )
                    else -> {
                    }
                }
                dialog.dismiss()
            }
            binding.btnRevoke.setOnClickListener {
                AppUtils.emitSocket(
                    SocketChatEmitDataEnum.REVOKE_MESSAGE.toString(),
                    RevokeMessage(
                        messages.random_key,
                        SocketChatMessageTypeEnum.REVOKE.toString().toInt(),
                        groupID
                    )
                )
                dialog.dismiss()
            }
            binding.btnShare.setOnClickListener {
                val intent = Intent(this, ShareMessageGroupActivity::class.java)
                val gson = Gson()
                intent.putExtra(
                    TechresEnum.GROUP_DATA.toString(),
                    gson.toJson(messages)
                )
                intent.putExtra(TechresEnum.ID_GROUP.toString(), groupID)
                startActivity(intent)
                dialog.dismiss()
            }
            binding.btnInformation.setOnClickListener {
                val intent = Intent(this, InformationMessageActivity::class.java)
                val gson = Gson()
                intent.putExtra(
                    TechresEnum.DETAIL_GROUP_CHAT.toString(),
                    gson.toJson(detailGroup)
                )
                intent.putExtra(
                    TechresEnum.MESSAGE_CHAT.toString(),
                    gson.toJson(messages)
                )
                startActivity(intent)
                dialog.dismiss()
            }
            binding.btnReply.setOnClickListener {
                isOrderMessage = 0
                actionBinding!!.ctlOrderMessage.visibility = View.GONE
                isReplyMessage = 1
                actionBinding!!.ctlReplyMessage.visibility = View.VISIBLE
                messageReply = MessageReply(
                    CurrentUser.getCurrentUser(this)!!.id,
                    groupID,
                    Objects.requireNonNull(actionBinding!!.editTextMessage.text)
                        .toString(),
                    messages.random_key,
                    SocketChatMessageTypeEnum.REPLY.toString().toInt()
                )
                when (messages.message_type) {
                    1, 7 -> {
                        actionBinding!!.chatReply.replyThumbContainer.visibility =
                            View.GONE
                        actionBinding!!.chatReply.replyName.text =
                            messages.sender.full_name
                        actionBinding!!.chatReply.replyText.text = Html.fromHtml(
                            messages.message,
                            Html.FROM_HTML_MODE_COMPACT
                        )
                    }
                    8 -> {
                        actionBinding!!.chatReply.replyThumbContainer.visibility =
                            View.VISIBLE
                        actionBinding!!.chatReply.replyName.text =
                            messages.sender.full_name
                        actionBinding!!.chatReply.linkPlayBtn.visibility = View.GONE
                        if (messages.message_link!!.media_thumb != "") {
                            AppUtils.callPhotoLocal(
                                messages.message_link!!.media_thumb,
                                actionBinding!!.chatReply.replyImage
                            )
                            actionBinding!!.chatReply.replyThumbContainer.visibility =
                                View.VISIBLE
                        } else {
                            actionBinding!!.chatReply.replyThumbContainer.visibility =
                                View.GONE
                        }
                        actionBinding!!.chatReply.replyText.text = Html.fromHtml(
                            messages.message_link!!.cannonical_url,
                            Html.FROM_HTML_MODE_COMPACT
                        )
                    }
                    2 -> {
                        actionBinding!!.chatReply.replyThumbContainer.visibility =
                            View.VISIBLE
                        actionBinding!!.chatReply.replyName.text =
                            messages.sender.full_name
                        actionBinding!!.chatReply.replyText.text =
                            getString(R.string.pinned_image)
                        actionBinding!!.chatReply.linkPlayBtn.visibility = View.GONE
                        AppUtils.callPhoto(
                            messages.files[0].link_original,
                            actionBinding!!.chatReply.replyImage
                        )
                    }
                    4 -> {
                        actionBinding!!.chatReply.replyThumbContainer.visibility =
                            View.VISIBLE
                        actionBinding!!.chatReply.replyName.text =
                            messages.sender.full_name
                        actionBinding!!.chatReply.replyText.text =
                            getString(R.string.pinned_sticker)
                        actionBinding!!.chatReply.linkPlayBtn.visibility = View.GONE
                        AppUtils.callPhoto(
                            messages.message,
                            actionBinding!!.chatReply.replyImage
                        )
                    }
                    3 -> {
                        actionBinding!!.chatReply.replyThumbContainer.visibility =
                            View.VISIBLE
                        actionBinding!!.chatReply.replyName.text =
                            messages.sender.full_name
                        actionBinding!!.chatReply.replyText.text = getNameNoType(
                            messages.files[0].link_original!!
                        )
                        actionBinding!!.chatReply.linkPlayBtn.visibility = View.GONE
                        val typeMedia: String =
                            AppUtils.getMimeType(messages.files[0].link_original!!)!!
                        actionBinding!!.chatReply.replyImage.layoutParams.width =
                            AppUtils.dpToPx(baseContext, 24f).toInt()
                        actionBinding!!.chatReply.replyImage.layoutParams.height =
                            AppUtils.dpToPx(baseContext, 32f).toInt()
                        actionBinding!!.chatReply.replyImage.requestLayout()
                        actionBinding!!.chatReply.lnCardView.layoutParams.width =
                            AppUtils.dpToPx(baseContext, 24f).toInt()
                        actionBinding!!.chatReply.lnCardView.layoutParams.height =
                            AppUtils.dpToPx(baseContext, 32f).toInt()
                        actionBinding!!.chatReply.lnCardView.requestLayout()
                        if (typeMedia == MediaTypeEnum.TYPE_JPG.toString() || typeMedia == MediaTypeEnum.TYPE_JPEG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_SVG.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_photo
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_DOCX.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_word
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_AI.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_ai
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_DMG.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_dmg
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_XLSX.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_excel
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_HTML.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_html
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_MP3.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_music
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_PDF.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_pdf
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_PPTX.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_powerpoint
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_PSD.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_psd
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_REC.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_record
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_EXE.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_setup
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_SKETCH.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_sketch
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_TXT.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_txt
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_MP4.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_video
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_XML.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_xml
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_ZIP.toString()) {
                            actionBinding!!.chatReply.replyImage.setImageDrawable(
                                context.getDrawable(
                                    R.drawable.icon_file_zip
                                )
                            )
                        } else actionBinding!!.chatReply.replyImage.setImageDrawable(
                            context.getDrawable(
                                R.drawable.icon_file_attach
                            )
                        )
                    }
                    5 -> {
                        actionBinding!!.chatReply.replyThumbContainer.visibility =
                            View.VISIBLE
                        actionBinding!!.chatReply.replyName.text =
                            messages.sender.full_name
                        actionBinding!!.chatReply.replyText.text =
                            getString(R.string.pinned_video)
                        actionBinding!!.chatReply.linkPlayBtn.visibility = View.VISIBLE
                        if (messages.files[0].link_original!!.contains("public/")) {
                            if (messages.key_message == "") {
                                AppUtils.callPhoto(
                                    messages.files[0].link_original,
                                    actionBinding!!.chatReply.replyImage
                                )
                            }
                        } else {
                            AppUtils.callPhotoLocal(
                                messages.files[0].link_original,
                                actionBinding!!.chatReply.replyImage
                            )
                        }
                    }
                    else -> {
                        isReplyMessage = 0
                        actionBinding!!.ctlReplyMessage.visibility = View.GONE
                    }
                }
                dialog.dismiss()
            }
        }
    }

    private fun getNameNoType(path: String): String {
        val filename = path.substring(path.lastIndexOf("/") + 1)
        val parts = filename.split("\\.").toTypedArray()
        return parts[0]
    }

    @Subscribe
    fun onSticker(event: EventBusStickerClick) {
        isOrderMessage = 0
        actionBinding!!.ctlOrderMessage.visibility = View.GONE

        fileChats.clear()
        fileChats.add(FileChat(event.sticker))
        AppUtils.emitSocket(
            SocketChatEmitDataEnum.CHAT_STICKER.toString(),
            MessageChat(
                CurrentUser.getCurrentUser(this)!!.id,
                groupID,
                SocketChatMessageTypeEnum.STICKER.toString().toInt(),
                event.sticker
            )
        )
        fileChats.clear()
        actionBinding!!.link.rlnLinkClip.visibility = View.GONE
        actionBinding!!.ctlLinkMessage.visibility = View.GONE
        actionBinding!!.link.youtube.visibility = View.GONE
        imageClip = ArrayList()
    }

    private fun openDialogBox() {
        val bindingDialog = DialogChatOrderBinding.inflate(layoutInflater)

        dialogOrder = BottomSheetDialog(this)
        dialogOrder!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogOrder!!.setContentView(bindingDialog.root)
        isOrderMessage = 0
        getAPIListOrder()
        if (dataOrder.size == 0) {
            bindingDialog.linearDataNull.visibility = View.VISIBLE
            bindingDialog.rclOrder.visibility = View.GONE
        } else {
            bindingDialog.linearDataNull.visibility = View.GONE
            bindingDialog.rclOrder.visibility = View.VISIBLE
        }
        dialogAdapter = GroupOrderAdapter(this)
        bindingDialog.rclOrder.adapter = dialogAdapter
        bindingDialog.rclOrder.layoutManager =
            PreCachingLayoutManager(this, RecyclerView.VERTICAL, false)
        bindingDialog.rclOrder.setHasFixedSize(true)
        dialogAdapter!!.setGroupOrderClick(this)
        dialogAdapter!!.setDetailOrderClick(this)
        bindingDialog.rclOrder.itemAnimator = DefaultItemAnimator()
        bindingDialog.imgDelete.setOnClickListener {
            dialogOrder!!.cancel()
        }
        bindingDialog.swipeRefreshLayout.setOnRefreshListener {
            getAPIListOrder()
            bindingDialog.swipeRefreshLayout.isRefreshing = false
        }
        bindingDialog.seachStaff.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList: ArrayList<DataListOrder> = ArrayList()
                for (key in dataOrder) {
                    if (dataOrder.size > 0) {
                        val name: String = key.code!!
                        if (name.lowercase(Locale.getDefault())
                                .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                dialogAdapter!!.setDataSource(newList)
                bindingDialog.rclOrder.setHasFixedSize(true)
                bindingDialog.rclOrder.adapter = dialogAdapter

                return false
            }
        })
        dialogOrder!!.show()
    }

    private fun getPinnedMessage() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url =
            "/api/pinned-messages-order/get-current?group_id=$groupID"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, this
        )
            .getPinnedMessage(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MessageResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: MessageResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        setUpPinnedMessage(response.data)
                    } else
                        Toast.makeText(
                            this@ChatActivity,
                            response.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                }
            })

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun setUpPinnedMessage(message: MessagesByGroup) {
        val messagePinned = message.message_pinned
        contentBinding!!.btnRemovePinned.setOnClickListener {
            AppUtils.emitSocket(
                SocketChatEmitDataEnum.REVOKE_PINNED_MESSAGE.toString(),
                PinnedEmmitSocket(
                    message.message_pinned.random_key,
                    groupID,
                    SocketChatMessageTypeEnum.REMOVE_PINNED.toString().toInt(), ""
                )
            )
        }
        contentBinding!!.lnShowDetails.setOnClickListener {
            if (contentBinding!!.lnShowPinned.visibility == View.VISIBLE) {
                contentBinding!!.lnShowPinned.visibility = View.GONE
            } else contentBinding!!.lnShowPinned.visibility = View.VISIBLE
        }
        if (messagePinned.status != 0) {
            contentBinding!!.lnPinned.visibility = View.VISIBLE
            contentBinding!!.txtUserPinned.text = message.sender.full_name
            contentBinding!!.tvUserPinned.text = messagePinned.sender.full_name
            contentBinding!!.txtTimePinned.text =
                AppUtils.getToDayMessage(message.created_at)
            if (messagePinned.status == 0) {
                contentBinding!!.linkThumbContainer.visibility = View.GONE
                contentBinding!!.tvContentPinned.text =
                    getString(R.string.revoke_message)
            } else {
                when (messagePinned.message_type) {
                    2 -> {
                        contentBinding!!.linkThumbContainer.visibility = View.VISIBLE
                        contentBinding!!.linkPlayBtn.visibility = View.GONE
                        AppUtils.callPhoto(
                            messagePinned.files[0].link_original,
                            contentBinding!!.linkThumb
                        )
                        contentBinding!!.tvContentPinned.text =
                            getString(R.string.pinned_image)
                    }
                    5 -> {
                        contentBinding!!.linkThumbContainer.visibility = View.VISIBLE
                        AppUtils.callPhoto(
                            messagePinned.files[0].link_original,
                            contentBinding!!.linkThumb
                        )
                        contentBinding!!.linkPlayBtn.visibility = View.VISIBLE
                        contentBinding!!.tvContentPinned.text =
                            getString(R.string.pinned_video)
                    }
                    4 -> {
                        contentBinding!!.linkThumbContainer.visibility = View.VISIBLE
                        contentBinding!!.linkPlayBtn.visibility = View.GONE
                        AppUtils.callPhoto(
                            messagePinned.message,
                            contentBinding!!.linkThumb
                        )
                        contentBinding!!.tvContentPinned.text =
                            getString(R.string.pinned_sticker)
                    }
                    8 -> {
                        if (messagePinned.message == "") {
                            contentBinding!!.tvContentPinned.text = Html.fromHtml(
                                messagePinned.message_link!!.cannonical_url,
                                Html.FROM_HTML_MODE_COMPACT
                            )
                        } else {
                            var messageData = messagePinned.message
                            if (messagePinned.list_tag_name.isNotEmpty()) {
                                var i = 0
                                while (i < messagePinned.list_tag_name.size) {
                                    val tagName =
                                        getString(R.string.font_tag_name_start) + messagePinned.list_tag_name[i].full_name + "</font>" + " "
                                    messageData = messageData.replace(
                                        messagePinned.list_tag_name[i].full_name,
                                        tagName
                                    )
                                    i++
                                }
                            }
                            //Change data
                            messageData = messageData.replace("\n", "<br>")
                            contentBinding!!.tvContentPinned.text =
                                AXEmojiUtils.replaceWithEmojis(
                                    this,
                                    Html.fromHtml(
                                        messageData,
                                        Html.FROM_HTML_MODE_COMPACT
                                    ), 24f
                                )
                        }
                        contentBinding!!.linkPlayBtn.visibility = View.GONE
                        if (messagePinned.message_link!!.media_thumb == "") {
                            contentBinding!!.linkThumbContainer.visibility = View.GONE
                        } else {
                            contentBinding!!.linkThumbContainer.visibility =
                                View.VISIBLE
                            AppUtils.callPhotoLocal(
                                messagePinned.message_link!!.media_thumb,
                                contentBinding!!.linkThumb
                            )
                        }
                    }
                    else -> {
                        contentBinding!!.linkThumbContainer.visibility = View.GONE
                        contentBinding!!.tvContentPinned.text =
                            Html.fromHtml(
                                messagePinned.message,
                                Html.FROM_HTML_MODE_COMPACT
                            )
                    }
                }
            }
        } else contentBinding!!.lnPinned.visibility = View.GONE
    }

    private fun sendPinned(messagesByGroup: MessagesByGroup) {
        when (messagesByGroup.message_type) {
            1, 4, 2, 5, 7, 8 -> AppUtils.emitSocket(
                SocketChatEmitDataEnum.PINNED_MESSAGE.toString(),
                PinnedEmmitSocket(
                    messagesByGroup.random_key,
                    groupID,
                    SocketChatMessageTypeEnum.PINNED.toString().toInt(), "",
                )
            )
            else -> {
            }
        }
    }

    private fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(amount.toDouble())
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(position: Int, data: DataListOrder) {
        isOrderMessage = 1
        messageOrder!!.order_id = data.id!!
        messageOrder!!.order_status = data.status!!
        messageOrder!!.order_time_delivery = data.delivery_at!!
        messageOrder!!.code = data.code!!
        messageOrder!!.created_at = data.created_at!!
        messageOrder!!.total = data.total_amount!!.toFloat()
        actionBinding!!.chatOrder.orderName.text = data.code
        actionBinding!!.chatOrder.orderAmount.text =
            currencyFormat(data.total_amount.toString()) + getString(
                R.string.txt_vnd
            )
        actionBinding!!.chatOrder.createOrder.text = data.created_at.toString()
        actionBinding!!.ctlOrderMessage.visibility = View.VISIBLE
        actionBinding!!.imageSend.visibility = View.VISIBLE
        actionBinding!!.imageCamera.visibility = View.GONE
        actionBinding!!.imgOrder.visibility = View.VISIBLE
        actionBinding!!.imageViewAttachment.visibility = View.GONE
        dialogOrder?.dismiss()

    }



    //Vuot tra loi tin nhan cu the
    private fun swipeReply() {
        val simpleCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                    return Float.MAX_VALUE
                }

                override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
                    return Float.MAX_VALUE
                }

                override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                    return Float.MAX_VALUE
                }

                @SuppressLint("UseCompatLoadingForDrawables")
                @RequiresApi(api = Build.VERSION_CODES.N)
                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    if (aBoolean) {
                        aBoolean = false
                        val position = viewHolder.bindingAdapterPosition
                        val messages = listMessages!![position]
                        isReplyMessage = 1
                        isOrderMessage = 0
                        actionBinding!!.ctlOrderMessage.visibility = View.GONE
                        if (messages.status == 0) actionBinding!!.ctlReplyMessage.visibility =
                            View.GONE else {
                            actionBinding!!.ctlReplyMessage.visibility = View.VISIBLE
                        }
                        messageReply = MessageReply(
                            CurrentUser.getCurrentUser(this@ChatActivity)!!.id,
                            groupID,
                            Objects.requireNonNull(actionBinding!!.editTextMessage.text)
                                .toString(),
                            messages.random_key,
                            SocketChatMessageTypeEnum.REPLY.toString().toInt()
                        )
                        when (messages.message_type) {
                            1, 7 -> {
                                actionBinding!!.chatReply.replyThumbContainer.visibility =
                                    View.GONE
                                actionBinding!!.chatReply.replyName.text =
                                    messages.sender.full_name
                                actionBinding!!.chatReply.replyText.text = Html.fromHtml(
                                    messages.message,
                                    Html.FROM_HTML_MODE_COMPACT
                                )
                            }
                            8 -> {
                                actionBinding!!.chatReply.replyThumbContainer.visibility =
                                    View.VISIBLE
                                actionBinding!!.chatReply.replyName.text =
                                    messages.sender.full_name
                                actionBinding!!.chatReply.linkPlayBtn.visibility = View.GONE
                                if (messages.message_link!!.media_thumb != "") {
                                    AppUtils.callPhotoLocal(
                                        messages.message_link!!.media_thumb,
                                        actionBinding!!.chatReply.replyImage
                                    )
                                    actionBinding!!.chatReply.replyThumbContainer.visibility =
                                        View.VISIBLE
                                } else {
                                    actionBinding!!.chatReply.replyThumbContainer.visibility =
                                        View.GONE
                                }
                                actionBinding!!.chatReply.replyText.text = Html.fromHtml(
                                    messages.message_link!!.cannonical_url,
                                    Html.FROM_HTML_MODE_COMPACT
                                )
                            }
                            2 -> {
                                actionBinding!!.chatReply.replyThumbContainer.visibility =
                                    View.VISIBLE
                                actionBinding!!.chatReply.replyName.text =
                                    messages.sender.full_name
                                actionBinding!!.chatReply.replyText.text =
                                    getString(R.string.pinned_image)
                                actionBinding!!.chatReply.linkPlayBtn.visibility = View.GONE
                                AppUtils.callPhoto(
                                    messages.files[0].link_original,
                                    actionBinding!!.chatReply.replyImage
                                )
                            }
                            4 -> {
                                actionBinding!!.chatReply.replyThumbContainer.visibility =
                                    View.VISIBLE
                                actionBinding!!.chatReply.replyName.text =
                                    messages.sender.full_name
                                actionBinding!!.chatReply.replyText.text =
                                    getString(R.string.pinned_sticker)
                                actionBinding!!.chatReply.linkPlayBtn.visibility = View.GONE
                                AppUtils.callPhoto(
                                    messages.message,
                                    actionBinding!!.chatReply.replyImage
                                )
                            }
                            3 -> {
                                actionBinding!!.chatReply.replyThumbContainer.visibility =
                                    View.VISIBLE
                                actionBinding!!.chatReply.replyName.text =
                                    messages.sender.full_name
                                actionBinding!!.chatReply.replyText.text = getNameNoType(
                                    messages.files[0].link_original!!
                                )
                                actionBinding!!.chatReply.linkPlayBtn.visibility = View.GONE
                                val typeMedia: String =
                                    AppUtils.getMimeType(messages.files[0].link_original!!)!!
                                actionBinding!!.chatReply.replyImage.layoutParams.width =
                                    AppUtils.dpToPx(baseContext, 24f).toInt()
                                actionBinding!!.chatReply.replyImage.layoutParams.height =
                                    AppUtils.dpToPx(baseContext, 32f).toInt()
                                actionBinding!!.chatReply.replyImage.requestLayout()
                                actionBinding!!.chatReply.lnCardView.layoutParams.width =
                                    AppUtils.dpToPx(baseContext, 24f).toInt()
                                actionBinding!!.chatReply.lnCardView.layoutParams.height =
                                    AppUtils.dpToPx(baseContext, 32f).toInt()
                                actionBinding!!.chatReply.lnCardView.requestLayout()
                                if (typeMedia == MediaTypeEnum.TYPE_JPG.toString() || typeMedia == MediaTypeEnum.TYPE_JPEG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_SVG.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_photo
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_DOCX.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_word
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_AI.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_ai
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_DMG.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_dmg
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_XLSX.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_excel
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_HTML.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_html
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_MP3.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_music
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_PDF.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_pdf
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_PPTX.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_powerpoint
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_PSD.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_psd
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_REC.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_record
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_EXE.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_setup
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_SKETCH.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_sketch
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_TXT.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_txt
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_MP4.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_video
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_XML.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_xml
                                        )
                                    )
                                } else if (typeMedia == MediaTypeEnum.TYPE_ZIP.toString()) {
                                    actionBinding!!.chatReply.replyImage.setImageDrawable(
                                        context.getDrawable(
                                            R.drawable.icon_file_zip
                                        )
                                    )
                                } else actionBinding!!.chatReply.replyImage.setImageDrawable(
                                    context.getDrawable(
                                        R.drawable.icon_file_attach
                                    )
                                )
                            }
                            5 -> {
                                actionBinding!!.chatReply.replyThumbContainer.visibility =
                                    View.VISIBLE
                                actionBinding!!.chatReply.replyName.text =
                                    messages.sender.full_name
                                actionBinding!!.chatReply.replyText.text =
                                    getString(R.string.pinned_video)
                                actionBinding!!.chatReply.linkPlayBtn.visibility = View.VISIBLE
                                if (messages.files[0].link_original!!.contains("public/")) {
                                    if (messages.key_message == "") {
                                        AppUtils.callPhoto(
                                            messages.files[0].link_original,
                                            actionBinding!!.chatReply.replyImage
                                        )
                                    }
                                } else {
                                    AppUtils.callPhotoLocal(
                                        messages.files[0].link_original,
                                        actionBinding!!.chatReply.replyImage
                                    )
                                }
                            }
                            else -> {
                                isReplyMessage = 0
                                actionBinding!!.ctlReplyMessage.visibility = View.GONE
                            }
                        }
                        super.clearView(recyclerView, viewHolder)
                    }
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    if (nCurrent != viewHolder.bindingAdapterPosition) {
                        nCurrent = viewHolder.bindingAdapterPosition
                        aBoolean = false
                    }
                    val item = viewHolder.itemView
                    val f = abs(dX) / item.width * 2
                    translateReboundingView(item, viewHolder, dX)
                    if (f >= 0.8f && !aBoolean) {
                        aBoolean = true
                        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                        if (vibrator.hasVibrator()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val effect = VibrationEffect.createOneShot(30, 2)
                                vibrator.vibrate(effect)
                            }
                        }
                    }
                    getDefaultUIUtil().onDraw(
                        c,
                        recyclerView,
                        item,
                        dX / 6,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(contentBinding!!.rcvChat)
    }

    private fun translateReboundingView(
        itemView: View,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float
    ) {
        val swipeDismissDistanceHorizontal = (itemView.width * 0.8f).toDouble()
        val dragFraction = ln((1 + dX / swipeDismissDistanceHorizontal) / ln(3.0))
        val dragTo = dragFraction * swipeDismissDistanceHorizontal * 0.8f
        viewHolder.itemView.translationX = dragTo.toFloat()
    }
    override fun onDetailOrder(position: Int, id: Int, img: String) {

        CacheManager.getInstance().put(TechresEnum.GET_BY_ID.toString(), id.toString())
        CacheManager.getInstance().put(TechresEnum.GET_BY_AVATAR.toString(), img)
        val intent = Intent(applicationContext, DetailOrderActivity::class.java)
      //  intent.putExtra(TechresEnum.GET_BY_ID_GROUP.toString(),groupID)
        startActivity(intent)
    }

}