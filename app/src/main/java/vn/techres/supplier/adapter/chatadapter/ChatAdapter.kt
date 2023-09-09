package vn.techres.supplier.adapter.chatadapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.aghajari.emojiview.AXEmojiUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
import org.greenrobot.eventbus.EventBus
import org.jetbrains.annotations.NotNull
import vn.techres.supplier.R
import vn.techres.supplier.activity.DetailOrderActivity
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.databinding.*
import vn.techres.supplier.helper.*
import vn.techres.supplier.helper.AppUtils.getLinkPhoto
import vn.techres.supplier.helper.AppUtils.resizeBitmap
import vn.techres.supplier.helper.Currency.formatCurrencyDecimal
import vn.techres.supplier.interfaces.ClickFullScreen
import vn.techres.supplier.model.eventbussms.EvenBusNewCall
import vn.techres.supplier.interfaces.RevokeMessageHandler
import vn.techres.supplier.model.chat.data.*
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.entity.NumberComparator
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ChatAdapter(var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataSource = ArrayList<MessagesByGroup>()

    private var revokeMessageHandler: RevokeMessageHandler? = null

    fun setRevokeMessageHandler(revokeMessageHandler: RevokeMessageHandler) {
        this.revokeMessageHandler = revokeMessageHandler
    }

    var clickFullScreen: ClickFullScreen? = null
    var lifecycle: Lifecycle? = null

    private val TYPE_TEXT = 0
    private val TYPE_TEXT_THEIR = 1

    private val TYPE_IMAGE = 2
    private val TYPE_IMAGE_THEIR = 3

    private val TYPE_VIDEO = 4
    private val TYPE_VIDEO_THEIR = 5

    private val TYPE_REPLY = 6
    private val TYPE_REPLY_THEIR = 7

    private val TYPE_STICKER = 8
    private val TYPE_STICKER_THEIR = 9

    private val TYPE_LINK = 10
    private val TYPE_LINK_THEIR = 11

    private val TYPE_PINNED = 12
    private val TYPE_ADD = 13


    private val TYPE_FILE = 14
    private val TYPE_FILE_THEIR = 15

    private val TYPE_CALL = 16
    private val TYPE_CALL_THEIR = 17

    private val TYPE_REVOKE = -1
    private val TYPE_REVOKE_THEIR = -2

    private val TYPE_CONTACT = 18
    private val TYPE_CONTACT_THEIR = 19

    private val TYPE_CHAT_ORDER = 20
    private val TYPE_CHAT_ORDER_THEIR = 21

    override fun getItemViewType(position: Int): Int {
        val message: MessagesByGroup = dataSource[position]
        if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.TEXT.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_TEXT
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.TEXT.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_TEXT_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.IMAGE.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_IMAGE
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.IMAGE.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_IMAGE_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.VIDEO.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_VIDEO
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.VIDEO.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_VIDEO_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.REPLY.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_REPLY
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.REPLY.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_REPLY_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.ORDER.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_CHAT_ORDER
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.ORDER.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_CHAT_ORDER_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.STICKER.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_STICKER
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.STICKER.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_STICKER_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.LINK.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_LINK
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.LINK.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_LINK_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.PINNED.toString()) || message.message_type == Integer.parseInt(
                SocketChatMessageTypeEnum.REMOVE_PINNED.toString()
            )
        ) {
            return TYPE_PINNED
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.ADD.toString())) {
            return TYPE_ADD
        } else if (message.status == 0 && checkSenderMessage(message)) {
            return TYPE_REVOKE
        } else if (message.status == 0 && !checkSenderMessage(message)) {
            return TYPE_REVOKE_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.FILE.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_FILE
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.FILE.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_FILE_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CALL_AUDIO.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_CALL
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CALL_AUDIO.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_CALL_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CALL_VIDEO.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_CALL
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CALL_VIDEO.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_CALL_THEIR
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CONTACT.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_CONTACT
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CONTACT.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            return TYPE_CONTACT_THEIR
        } else
            return 100
    }

    private fun checkSenderMessage(message: MessagesByGroup): Boolean {
        return message.sender.member_id == CurrentUser.getCurrentUser(context)!!.id && message.sender.app_name == "supplier"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_TEXT_THEIR -> {
                return TextTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_text_left, parent, false)
                )
            }
            TYPE_IMAGE -> {
                return ImageMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_image_right, parent, false)
                )
            }
            TYPE_IMAGE_THEIR -> {
                return ImageTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_image_left, parent, false)
                )
            }
            TYPE_VIDEO -> {
                return VideoMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_video_right, parent, false)
                )
            }
            TYPE_VIDEO_THEIR -> {
                return VideoTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_video_left, parent, false)
                )
            }
            TYPE_REPLY -> {
                return ReplyMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_reply_right, parent, false)
                )
            }
            TYPE_REPLY_THEIR -> {
                return ReplyTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_reply_left, parent, false)
                )
            }
            TYPE_CHAT_ORDER -> {
                return ChatOrderMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_chat_order_right, parent, false)
                )
            }
            TYPE_CHAT_ORDER_THEIR -> {
                return ChatOrderTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_chat_order_left, parent, false)
                )
            }
            TYPE_STICKER -> {
                return StickerMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_sticker_right, parent, false)
                )
            }
            TYPE_LINK -> {
                return LinkMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_link_right, parent, false)
                )
            }
            TYPE_LINK_THEIR -> {
                return LinkTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_link_left, parent, false)
                )
            }
            TYPE_STICKER_THEIR -> {
                return StickerTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_sticker_left, parent, false)
                )
            }
            TYPE_PINNED -> {
                return PinnedMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_pinned, parent, false)
                )
            }
            TYPE_ADD -> {
                return AddMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_notification, parent, false)
                )
            }
            TYPE_REVOKE -> {
                return RevokeMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_revoke_right, parent, false)
                )
            }
            TYPE_REVOKE_THEIR -> {
                return RevokeTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_revoke_left, parent, false)
                )
            }
            TYPE_FILE -> {
                return FileMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_file_right, parent, false)
                )
            }
            TYPE_FILE_THEIR -> {
                return FileTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_file_left, parent, false)
                )
            }
            TYPE_CALL -> {
                return CallMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_call_right, parent, false)
                )
            }
            TYPE_CALL_THEIR -> {
                return CallTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_call_left, parent, false)
                )
            }
            TYPE_CONTACT -> {
                return ContactMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_contact_right, parent, false)
                )
            }
            TYPE_CONTACT_THEIR -> {
                return ContactTheirMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_contact_left, parent, false)
                )
            }


            else -> {
                return TextMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_text_right, parent, false)
                )
            }
        }
    }

    /**
     * gui tin nhan text gui di ( ben phai )
     */
    inner class TextMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageTextRightBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {

            val data = dataSource[position]

            //Default data
            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.message.tvTime,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            checkUserViewMessage(
                binding.rclUserView,
                binding.send.lnSendMessage,
                data,
                position,
                binding.lnView,
                binding.lnUserView,
                binding.tvMoreView
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            if (position != 0) {
                binding.lnView.visibility = View.GONE
            }

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.padding_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            var message = data.message
            if (data.list_tag_name.isNotEmpty()) {
                for (i in data.list_tag_name.indices) {
                    val tagName: String =
                        context.getString(R.string.font_tag_name_start) + data.list_tag_name[i].full_name + "</font>" + " "
                    message = message.replace(data.list_tag_name[i].full_name, tagName)
                }
            }

            //Change data
            message = message.replace("\n", "<br>")
            binding.message.tvMessage.text = AXEmojiUtils.replaceWithEmojis(
                itemView.context,
                Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT), 24f
            )

            if (data.message_type == SocketChatMessageTypeEnum.KICK.toString().toInt()) {
                itemView.visibility == View.GONE
            }
        }

    }

    /**
     * gui tin nhan text nhan lai ( ben trai )
     */
    inner class TextTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageTextLeftBinding.bind(view)

        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessagesTheir(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.message.tvTime,
                binding.nameMedia,
                binding.imvAvatar,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            binding.nameMedia.text = data.sender.full_name

            var message = data.message
            if (data.list_tag_name.size > 0) {
                for (i in data.list_tag_name.indices) {
                    val tagName: String =
                        context.resources.getString(R.string.font_tag_name_start) + data.list_tag_name[i].full_name + "</font>" + " "
                    message = message.replace(data.list_tag_name[i].full_name, tagName)
                }
            }

            message = message.replace("\n", "<br>")
            binding.message.tvMessage.text = AXEmojiUtils.replaceWithEmojis(
                context,
                Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT), 24f
            )
        }
    }

    /**
     * gui hinh gui di ( ben phai )
     */
    inner class ImageMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageImageRightBinding.bind(view)

        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            checkUserViewMessage(
                binding.rclUserView,
                binding.send.lnSendMessage,
                data,
                position,
                binding.lnView,
                binding.lnUserView,
                binding.tvMoreView
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            if (position != 0) {
                binding.lnView.visibility = View.GONE
            }

            val lp = binding.rltVideo.layoutParams
                    as ViewGroup.MarginLayoutParams
            if (binding.tvTime.visibility == View.GONE) {
                if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                    lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_10).toInt())
                } else {
                    lp.setMargins(0, 0, 0, 0)
                }
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_20).toInt())
            }

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler?.onRevoke(data, v)
                true
            }

            if (data.files.size == 1) {
                binding.lnImageOne.visibility = View.VISIBLE
                binding.lnImageFourMore.visibility = View.GONE
                if (data.files[0].link_original!!.contains("public/")) {
                    Glide.with(SupplierApplication.context)
                        .asBitmap()
                        .load(getLinkPhoto(data.files[0].link_original))
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                binding.imgOneOne.setImageBitmap(
                                    resizeBitmap(
                                        resource,
                                        SupplierApplication.widthView * 2 / 3
                                    )
                                )
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })

                } else {
                    Glide.with(SupplierApplication.context)
                        .asBitmap()
                        .load(data.files[0].link_original)
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                binding.imgOneOne.setImageBitmap(
                                    resizeBitmap(
                                        resource,
                                        SupplierApplication.widthView * 2 / 3
                                    )
                                )
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })
                }
                binding.imgOneOne.setOnClickListener {
                    AppUtils.showImageMediaSlider(
                        data.files[0].link_original, 0
                    )
                }
            } else {
                binding.lnImageOne.visibility = View.GONE
                binding.lnImageFourMore.visibility = View.VISIBLE
                setAdapterImageVideo(binding.rcImgMore, data)
            }
        }

    }

    /**
     * gui hinh nhan lai ( ben trai )
     */
    inner class ImageTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageImageLeftBinding.bind(view)

        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessagesTheir(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                binding.lnNameMedia,
                binding.imvAvatar,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            binding.nameMedia.text = data.sender.full_name

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            val lp = binding.rltVideo.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.tvTime.visibility == View.GONE) {
                if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                    lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_10).toInt())
                } else {
                    lp.setMargins(0, 0, 0, 0)
                }
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_20).toInt())
            }

            if (data.files.size == 1) {
                binding.lnImageOne.visibility = View.VISIBLE
                binding.lnImageFourMore.visibility = View.GONE
                if (data.files[0].link_original!!.contains("public/")) {
                    Glide.with(SupplierApplication.context)
                        .asBitmap()
                        .load(getLinkPhoto(data.files[0].link_original))
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                binding.imgOneOne.setImageBitmap(
                                    resizeBitmap(
                                        resource,
                                        SupplierApplication.widthView * 2 / 3
                                    )
                                )
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })

                } else {
                    Glide.with(SupplierApplication.context)
                        .asBitmap()
                        .load(data.files[0].link_original)
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                binding.imgOneOne.setImageBitmap(
                                    resizeBitmap(
                                        resource,
                                        SupplierApplication.widthView * 2 / 3
                                    )
                                )
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })
                }
                binding.imgOneOne.setOnClickListener {
                    AppUtils.showImageMediaSlider(
                        data.files[0].link_original,
                        0
                    )
                }
            } else {
                binding.lnImageOne.visibility = View.GONE
                binding.lnImageFourMore.visibility = View.VISIBLE
                setAdapterImageVideo(binding.rcImgMore, data)
            }
        }

    }

    /**
     * gui video gui di ( ben phai )
     */
    inner class VideoMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageVideoRightBinding.bind(view)

        @SuppressLint("LogNotTimber")
        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            checkUserViewMessage(
                binding.rclUserView,
                binding.send.lnSendMessage,
                data,
                position,
                binding.lnView,
                binding.lnUserView,
                binding.tvMoreView
            )

            if (position != 0) {
                binding.lnView.visibility = View.GONE
            }

            val lp = binding.rltVideo.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.tvTime.visibility == View.GONE) {
                if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                    lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_10).toInt())
                } else {
                    lp.setMargins(0, 0, 0, 0)
                }
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_20).toInt())
            }

            if (data.files[0].link_original!!.contains("public/")) {
                Log.e("videone", getLinkPhoto(data.files[0].link_original))
                binding.videoPlayer.setUp(
                    getLinkPhoto(data.files[0].link_original),
                    JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,
                    ""
                )
                AppUtils.callPhoto(
                    data.files[0].link_original,
                    binding.videoPlayer.thumbImageView
                )
            } else {
                binding.videoPlayer.setUp(
                    data.files[0].link_original, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, ""
                )
                AppUtils.callPhotoLocal(
                    data.files[0].link_original,
                    binding.videoPlayer.thumbImageView
                )
            }

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }
        }

    }

    /**
     * gui video nhan lai ( ben trai )
     */
    inner class VideoTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageVideoLeftBinding.bind(view)

        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessagesTheir(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                binding.lnNameMedia,
                binding.imvAvatar,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            binding.nameMedia.text = data.sender.full_name

            val lp = binding.rltVideo.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.tvTime.visibility == View.GONE) {
                if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                    lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_10).toInt())
                } else {
                    lp.setMargins(0, 0, 0, 0)
                }
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_20).toInt())
            }

            binding.videoPlayer.setUp(
                getLinkPhoto(data.files[0].link_original),
                JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,
                ""
            )
            AppUtils.callPhoto(data.files[0].link_original, binding.videoPlayer.thumbImageView)

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }
        }

    }

    /**
     * chat don hang gui di ( ben phai )
     */
    inner class ChatOrderMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageChatOrderRightBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.message.tvTime,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            checkUserViewMessage(
                binding.rclUserView,
                binding.send.lnSendMessage,
                data,
                position,
                binding.lnView,
                binding.lnUserView,
                binding.tvMoreView
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            if (position != 0) {
                binding.lnView.visibility = View.GONE
            }

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.padding_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            var message: String = data.message_order.message
            if (data.message_order.list_tag_name.size > 0) {
                for (i in 0 until data.message_order.list_tag_name.size) {
                    val tagName: String =
                        context.resources.getString(R.string.font_tag_name_start) + data.message_order.list_tag_name[i].full_name + "</font>" + " "
                    message =
                        message.replace(data.message_order.list_tag_name[i].full_name, tagName)
                }
            }

            //Change data
            message = message.replace("\n", "<br>")
            binding.message.tvMessage.text = AXEmojiUtils.replaceWithEmojis(
                itemView.context,
                Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT), 24f
            )

            binding.order.orderName.text =
                "Mã ĐH:" + " " + data.message_order.code
            binding.order.orderTime.text =
                "Ngày tạo:" + " " + data.message_order.created_at
            binding.order.orderContent.text =
                "Giá:" + " " + formatCurrencyDecimal(
                    data.message_order.total
                ) + " VND"
            binding.chatOrder.setOnClickListener {
                CacheManager.getInstance()
                    .put(TechresEnum.GET_BY_ID.toString(), data.message_order.order_id.toString())
                val intent = Intent(itemView.context, DetailOrderActivity::class.java)
                intent.putExtra(TechresEnum.GET_BY_ID_GROUP.toString(), data.receiver_id)
                ContextCompat.startActivity(context, intent, null)
            }

        }
    }

    /**
     * chat don hang nhan lai ( ben trai )
     */
    inner class ChatOrderTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageChatOrderLeftBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessagesTheir(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.message.tvTime,
                binding.nameMedia,
                binding.imvAvatar,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            binding.nameMedia.text = data.sender.full_name

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            var message: String = data.message_order.message
            if (data.message_order.list_tag_name.size > 0) {
                for (i in 0 until data.message_order.list_tag_name.size) {
                    val tagName: String =
                        context.resources.getString(R.string.font_tag_name_start) + data.message_order.list_tag_name[i].full_name + "</font>" + " "
                    message =
                        message.replace(data.message_order.list_tag_name[i].full_name, tagName)
                }
            }

            //Change data
            message = message.replace("\n", "<br>")
            binding.message.tvMessage.text = AXEmojiUtils.replaceWithEmojis(
                itemView.context,
                Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT), 24f
            )

            binding.order.orderName.text =
                "Mã ĐH:" + " " + data.message_order.code
            binding.order.orderTime.text =
                "Ngày tạo:" + " " + data.message_order.created_at
            binding.order.orderContent.text =
                "Giá:" + " " + formatCurrencyDecimal(
                    data.message_order.total
                ) + " VND"
            binding.order.replyChatOrder.setOnClickListener {
                CacheManager.getInstance().put(TechresEnum.GET_BY_ID.toString(), data.message_order.order_id.toString())
                val intent = Intent(itemView.context, DetailOrderActivity::class.java)
                intent.putExtra(TechresEnum.GET_BY_ID_GROUP.toString(), data.receiver_id)
                ContextCompat.startActivity(context, intent, null)
            }
        }

    }

    /**
     * ReplyMessage gui di ( ben phai )
     */
    inner class ReplyMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageReplyRightBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.message.tvTime,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            checkUserViewMessage(
                binding.rclUserView,
                binding.send.lnSendMessage,
                data,
                position,
                binding.lnView,
                binding.lnUserView,
                binding.tvMoreView
            )

            if (position != 0) {
                binding.lnView.visibility = View.GONE
            }

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            var message = data.message
            if (data.list_tag_name.isNotEmpty()) {
                for (i in data.list_tag_name.indices) {
                    val tagName: String =
                        context.resources.getString(R.string.font_tag_name_start) + data.list_tag_name[i].full_name + "</font>" + " "
                    message = message.replace(data.list_tag_name[i].full_name, tagName)
                }
            }

            //Change data
            message = message.replace("\n", "<br>")
            binding.message.tvMessage.text = AXEmojiUtils.replaceWithEmojis(
                itemView.context,
                Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT), 24f
            )

            binding.reply.replyName.text = data.message_reply.sender!!.full_name

            if (data.message_reply.status == 0) {
                binding.reply.replyText.text = context.resources.getString(R.string.revoke_message)
                binding.reply.rltMedia.visibility = View.GONE
            } else {
                binding.reply.rltMedia.visibility = View.VISIBLE
                when (data.message_reply.message_type) {
                    1, 7 -> {
                        val messageLineReply = data.message_reply.message!!.replace("\n", "<br>")
                        binding.reply.replyText.text = AXEmojiUtils.replaceWithEmojis(
                            itemView.context,
                            Html.fromHtml(messageLineReply, Html.FROM_HTML_MODE_COMPACT), 24f
                        )
                        binding.reply.rltMedia.visibility = View.GONE
                    }
                    8 -> {
                        if (data.message_reply.message == "") {
                            binding.reply.replyText.text =
                                data.message_reply.message_link!!.cannonical_url
                        } else binding.reply.replyText.text = data.message_reply.message
                        if (data.message_reply.message_link!!.media_thumb != "") {
                            AppUtils.callPhotoLocal(
                                data.message_reply.message_link!!.media_thumb,
                                binding.reply.replyImage
                            )
                            binding.reply.rltMedia.visibility = View.VISIBLE
                        } else {
                            binding.reply.rltMedia.visibility = View.GONE
                        }
                        binding.reply.imvPlayMedia.visibility = View.GONE
                    }
                    2 -> {
                        binding.reply.replyText.text =
                            context.resources.getString(R.string.message_type_image)
                        binding.reply.rltMedia.visibility = View.VISIBLE
                        AppUtils.callPhoto(
                            data.message_reply.files!![0].link_original,
                            binding.reply.replyImage
                        )
                        binding.reply.imvPlayMedia.visibility = View.GONE
                    }
                    5 -> {
                        binding.reply.replyText.text =
                            context.resources.getString(R.string.message_type_video)
                        binding.reply.rltMedia.visibility = View.VISIBLE
                        binding.reply.imvPlayMedia.visibility = View.VISIBLE
                        AppUtils.callPhoto(
                            data.message_reply.files!![0].link_original,
                            binding.reply.replyImage
                        )
                    }
                    4 -> {
                        binding.reply.replyText.text =
                            context.resources.getString(R.string.message_type_sticker)
                        binding.reply.rltMedia.visibility = View.VISIBLE
                        binding.reply.imvPlayMedia.visibility = View.GONE
                        AppUtils.callPhoto(data.message_reply.message, binding.reply.replyImage)
                    }
                    3 -> {
                        binding.reply.replyText.text =
                            getNameNoType(data.message_reply.files!![0].link_original!!)
                        binding.reply.rltMedia.visibility = View.VISIBLE
                        binding.reply.imvPlayMedia.visibility = View.GONE
                        val typeMedia: String =
                            AppUtils.getMimeType(data.message_reply.files!![0].link_original!!)!!
                        binding.reply.replyImage.layoutParams.width =
                            AppUtils.dpToPx(context, 24F).toInt()
                        binding.reply.replyImage.layoutParams.height =
                            AppUtils.dpToPx(context, 32F).toInt()
                        binding.reply.replyImage.requestLayout()
                        binding.reply.lnCardView.layoutParams.width =
                            AppUtils.dpToPx(context, 24F).toInt()
                        binding.reply.lnCardView.layoutParams.height =
                            AppUtils.dpToPx(context, 32F).toInt()
                        binding.reply.lnCardView.requestLayout()
                        if (typeMedia == MediaTypeEnum.TYPE_JPG.toString() || typeMedia == MediaTypeEnum.TYPE_JPEG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_SVG.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_photo, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_DOCX.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_word, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_AI.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_ai, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_DMG.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_dmg, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_XLSX.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_excel, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_HTML.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_html, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_MP3.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_music, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_PDF.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_pdf, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_PPTX.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_powerpoint, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_PSD.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_psd, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_REC.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_record, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_EXE.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_setup, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_SKETCH.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_sketch, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_TXT.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_txt, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_MP4.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_video, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_XML.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_xml, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_ZIP.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_zip, null
                                )
                            )
                        } else binding.reply.replyImage.setImageDrawable(
                            context.resources.getDrawable(
                                R.drawable.icon_file_attach, null
                            )
                        )
                    }
                }
            }
        }

    }

    /**
     * ReplyMessage nhan lai ( ben trai )
     */
    inner class ReplyTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageReplyLeftBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessagesTheir(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.message.tvTime,
                binding.nameMedia,
                binding.ctlAvatar,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            binding.nameMedia.text = data.sender.full_name

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            var message = data.message
            if (data.list_tag_name.isNotEmpty()) {
                for (i in data.list_tag_name.indices) {
                    val tagName: String =
                        context.resources.getString(R.string.font_tag_name_start) + data.list_tag_name[i].full_name + "</font>" + " "
                    message = message.replace(data.list_tag_name[i].full_name, tagName)
                }
            }

            //Change data
            message = message.replace("\n", "<br>")
            binding.message.tvMessage.text = AXEmojiUtils.replaceWithEmojis(
                itemView.context,
                Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT), 24f
            )

            binding.reply.replyName.text = data.message_reply.sender!!.full_name

            if (data.message_reply.status == 0) {
                binding.reply.replyText.text = context.resources.getString(R.string.revoke_message)
                binding.reply.rltMedia.visibility = View.GONE
            } else {
                binding.reply.rltMedia.visibility = View.VISIBLE
                when (data.message_reply.message_type) {
                    1, 7 -> {
                        val messageLineReply = data.message_reply.message!!.replace("\n", "<br>")
                        binding.reply.replyText.text = AXEmojiUtils.replaceWithEmojis(
                            itemView.context,
                            Html.fromHtml(messageLineReply, Html.FROM_HTML_MODE_COMPACT), 24f
                        )
                        binding.reply.rltMedia.visibility = View.GONE
                    }
                    8 -> {
                        if (data.message_reply.message == "") {
                            binding.reply.replyText.text =
                                data.message_reply.message_link!!.cannonical_url
                        } else binding.reply.replyText.text = data.message_reply.message
                        if (data.message_reply.message_link!!.media_thumb != "") {
                            AppUtils.callPhotoLocal(
                                data.message_reply.message_link!!.media_thumb,
                                binding.reply.replyImage
                            )
                            binding.reply.rltMedia.visibility = View.VISIBLE
                        } else {
                            binding.reply.rltMedia.visibility = View.GONE
                        }
                        binding.reply.imvPlayMedia.visibility = View.GONE
                    }
                    2 -> {
                        binding.reply.replyText.text =
                            context.resources.getString(R.string.message_type_image)
                        binding.reply.rltMedia.visibility = View.VISIBLE
                        AppUtils.callPhoto(
                            data.message_reply.files!![0].link_original,
                            binding.reply.replyImage
                        )
                        binding.reply.imvPlayMedia.visibility = View.GONE
                    }
                    5 -> {
                        binding.reply.replyText.text =
                            context.resources.getString(R.string.message_type_video)
                        binding.reply.rltMedia.visibility = View.VISIBLE
                        binding.reply.imvPlayMedia.visibility = View.VISIBLE
                        AppUtils.callPhoto(
                            data.message_reply.files!![0].link_original,
                            binding.reply.replyImage
                        )
                    }
                    3 -> {
                        binding.reply.replyText.text =
                            getNameNoType(data.message_reply.files!![0].link_original!!)
                        binding.reply.rltMedia.visibility = View.VISIBLE
                        binding.reply.imvPlayMedia.visibility = View.GONE
                        val typeMedia: String =
                            AppUtils.getMimeType(data.message_reply.files!![0].link_original!!)!!
                        binding.reply.replyImage.layoutParams.width =
                            AppUtils.dpToPx(itemView.context, 24F).toInt()
                        binding.reply.replyImage.layoutParams.height =
                            AppUtils.dpToPx(itemView.context, 32F).toInt()
                        binding.reply.replyImage.requestLayout()
                        binding.reply.lnCardView.layoutParams.width =
                            AppUtils.dpToPx(itemView.context, 24F).toInt()
                        binding.reply.lnCardView.layoutParams.height =
                            AppUtils.dpToPx(itemView.context, 32F).toInt()
                        binding.reply.lnCardView.requestLayout()
                        if (typeMedia == MediaTypeEnum.TYPE_JPG.toString() || typeMedia == MediaTypeEnum.TYPE_JPEG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_SVG.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_photo, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_DOCX.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_word, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_AI.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_ai, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_DMG.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_dmg, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_XLSX.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_excel, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_HTML.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_html, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_MP3.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_music, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_PDF.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_pdf, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_PPTX.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_powerpoint, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_PSD.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_psd, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_REC.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_record, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_EXE.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_setup, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_SKETCH.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_sketch, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_TXT.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_txt, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_MP4.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_video, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_XML.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_xml, null
                                )
                            )
                        } else if (typeMedia == MediaTypeEnum.TYPE_ZIP.toString()) {
                            binding.reply.replyImage.setImageDrawable(
                                context.resources.getDrawable(
                                    R.drawable.icon_file_zip, null
                                )
                            )
                        } else binding.reply.replyImage.setImageDrawable(
                            context.resources.getDrawable(
                                R.drawable.icon_file_attach, null
                            )
                        )
                    }
                    4 -> {
                        binding.reply.replyText.text =
                            context.resources.getString(R.string.message_type_sticker)
                        binding.reply.rltMedia.visibility = View.VISIBLE
                        binding.reply.imvPlayMedia.visibility = View.GONE
                        AppUtils.callPhoto(data.message_reply.message, binding.reply.replyImage)
                    }
                }
            }
        }

    }

    /**
     * Sticker gui di ( been phai)
     */
    inner class StickerMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageStickerRightBinding.bind(view)

        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            checkUserViewMessage(
                binding.rclUserView,
                binding.send.lnSendMessage,
                data,
                position,
                binding.lnView,
                binding.lnUserView,
                binding.tvMoreView
            )

            if (position != 0) {
                binding.lnView.visibility = View.GONE
            }

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            val lp = binding.rltVideo.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.tvTime.visibility == View.GONE) {
                if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                    lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_10).toInt())
                } else {
                    lp.setMargins(0, 0, 0, 0)
                }
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_20).toInt())
            }

            AppUtils.callPhoto(data.message, binding.imvSticker)
        }

    }

    /**
     *  Sticker nhan lai ( ben trai)
     */
    inner class StickerTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageStickerLeftBinding.bind(view)

        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessagesTheir(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                binding.nameMedia,
                binding.imvAvatar,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            binding.nameMedia.text = data.sender.full_name

            val lp = binding.rltVideo.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.tvTime.visibility == View.GONE) {
                if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                    lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_10).toInt())
                } else {
                    lp.setMargins(0, 0, 0, 0)
                }
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_20).toInt())
            }

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            AppUtils.callPhoto(data.message, binding.imvSticker)
        }

    }

    /**
     * ghim tin nhan chat ( ben tren )
     */
    inner class PinnedMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessagePinnedBinding.bind(view)

        fun bind(position: Int) {
            val data = dataSource[position]
            if (data.today == 1) {
                binding.time.lnTimeHeader.visibility = View.VISIBLE
                binding.time.tvTimeHeader.text = getToDayMessage(data.interval_of_time)

            } else {
                binding.time.lnTimeHeader.visibility = View.GONE

            }
            binding.messagePinned.text = data.message
            if (data.message_type == Integer.parseInt(SocketChatMessageTypeEnum.PINNED.toString())) {
                binding.messagePinned.text = data.message
                binding.imgPinned.setImageResource(R.drawable.ic_pin_line_16)
            }
            if (data.message_type == Integer.parseInt(SocketChatMessageTypeEnum.REMOVE_PINNED.toString())) {
                binding.messagePinned.text = data.message
                binding.imgPinned.setImageResource(R.drawable.ic_unpin_line_16)
            }

        }

    }

    /**
     * Thong bao them nhan vien vao group chat
     */
    inner class AddMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageNotificationBinding.bind(view)

        fun bind(position: Int) {
            val data = dataSource[position]
            val INT_START = 0
            val INT_END: Int
            if (data.today == 1) {
                binding.time.lnTimeHeader.visibility = View.VISIBLE
                binding.time.tvTimeHeader.text = getToDayMessage(data.interval_of_time)
            } else {
                binding.time.lnTimeHeader.visibility = View.GONE
            }

            val membersList: List<Members> = data.list_member
            when {
                membersList.size == 1 -> {
                    binding.lnAvatarOne.visibility = View.VISIBLE
                    binding.lnAvatarThree.visibility = View.GONE
                    binding.lnAvatarTwo.visibility = View.GONE
                    INT_END = membersList[0].full_name.length
                    val name: String = java.lang.String.format(
                        context.resources.getString(R.string.str_people_name_1),
                        membersList[0].full_name,
                        data.sender.full_name
                    )
                    val str = SpannableStringBuilder(name)
                    str.setSpan(
                        StyleSpan(Typeface.BOLD),
                        INT_START,
                        INT_END,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding.textNotificationAddUser.text = str
                    AppUtils.callPhotoAvatar(membersList[0].avatar, binding.imgAvatarOne)
                }
                membersList.size == 2 -> {
                    binding.lnAvatarOne.visibility = View.GONE
                    binding.lnAvatarThree.visibility = View.GONE
                    binding.lnAvatarTwo.visibility = View.VISIBLE
                    INT_END = membersList[0].full_name.length + membersList[1].full_name.length + 4
                    val name: String = String.format(
                        context.resources.getString(R.string.str_people_name_2),
                        membersList[0].full_name,
                        membersList[1].full_name,
                        data.sender.full_name
                    )
                    val str = SpannableStringBuilder(name)
                    str.setSpan(
                        StyleSpan(Typeface.BOLD),
                        INT_START,
                        INT_END,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding.textNotificationAddUser.text = str
                    AppUtils.callPhotoAvatar(membersList[0].avatar, binding.imgAvatarOneTwo)
                    AppUtils.callPhotoAvatar(membersList[1].avatar, binding.imgAvatarTwoTwo)
                }
                membersList.size >= 3 -> {
                    binding.lnAvatarOne.visibility = View.GONE
                    binding.lnAvatarThree.visibility = View.VISIBLE
                    binding.lnAvatarTwo.visibility = View.GONE
                    if (membersList.size > 3) {
                        INT_END =
                            membersList[0].full_name.length + membersList[1].full_name.length + membersList[2].full_name.length + 5
                        val name: String = String.format(
                            context.resources.getString(R.string.str_people_name_3s),
                            membersList[0].full_name,
                            membersList[1].full_name,
                            membersList[2].full_name,
                            data.sender.full_name,
                            membersList.size - 3
                        )
                        val str = SpannableStringBuilder(name)
                        str.setSpan(
                            StyleSpan(Typeface.BOLD),
                            INT_START,
                            INT_END,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        binding.textNotificationAddUser.text = str
                    } else {
                        INT_END =
                            membersList[0].full_name.length + membersList[1].full_name.length + membersList[2].full_name.length + 8
                        val name: String = String.format(
                            context.resources.getString(R.string.str_people_name_3),
                            membersList[0].full_name,
                            membersList[1].full_name,
                            membersList[2].full_name,
                            data.sender.full_name
                        )
                        val str = SpannableStringBuilder(name)
                        str.setSpan(
                            StyleSpan(Typeface.BOLD),
                            INT_START,
                            INT_END,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        binding.textNotificationAddUser.text = str
                    }
                    AppUtils.callPhotoAvatar(membersList[0].full_name, binding.imgAvatarOneThree)
                    AppUtils.callPhotoAvatar(membersList[1].full_name, binding.imgAvatarTwoThree)
                    AppUtils.callPhotoAvatar(membersList[2].full_name, binding.imgAvatarThreeThree)
                }
            }
        }
    }

    /**
     * Thu hoi tin nhan gui di ( ben phai)
     */
    inner class RevokeMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageRevokeRightBinding.bind(view)

        fun bind(position: Int) {
            val data = dataSource[position]
            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                position
            )
            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())

            binding.tvMessage.text = context.resources.getString(R.string.revoke_message)
        }
    }

    /**
     * Thu hoi tin nhan nhan lai ( ben trai)
     */
    inner class RevokeTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageRevokeLeftBinding.bind(view)

        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessagesTheir(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                binding.nameMedia,
                binding.imvAvatar,
                position
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            binding.nameMedia.text = data.sender.full_name

            binding.tvMessage.text = context.resources.getString(R.string.revoke_message)
        }
    }

    /**
     * gui file gui di (ben phai)
     */
    inner class FileMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageFileRightBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            checkUserViewMessage(
                binding.rclUserView,
                binding.send.lnSendMessage,
                data,
                position,
                binding.lnView,
                binding.lnUserView,
                binding.tvMoreView
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            if (position != 0) {
                binding.lnView.visibility = View.GONE
            }

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.padding_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            binding.tvSizeFile.text = getStringSizeLengthFile(data.files[0].size)
            binding.tvNameFile.text = getNameNoType(data.files[0].name_file!!)
            binding.tvTypeFile.text = AppUtils.getMimeType(data.files[0].link_original!!)
            binding.tvTypeFile.setEllipsize(TextUtils.TruncateAt.MARQUEE)
            binding.tvTypeFile.setMarqueeRepeatLimit(-1)
            binding.tvTypeFile.setSelected(true)
            val typeMedia: String = AppUtils.getMimeType(data.files[0].link_original!!)!!

            if (typeMedia == MediaTypeEnum.TYPE_JPG.toString() || typeMedia == MediaTypeEnum.TYPE_JPEG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_SVG.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_photo,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_DOCX.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_word,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_AI.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_ai,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_DMG.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_dmg,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_XLSX.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_excel,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_HTML.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_html,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_MP3.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_music,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_PDF.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_pdf,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_PPTX.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_powerpoint,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_PSD.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_psd,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_REC.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_record,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_EXE.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_setup,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_SKETCH.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_sketch,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_TXT.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_txt,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_MP4.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_video,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_XML.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_xml,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_ZIP.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_zip,
                        null
                    )
                )
            } else binding.imvTypeFile.setImageDrawable(
                context.resources.getDrawable(
                    R.drawable.icon_file_attach,
                    null
                )
            )

            binding.tvOpen.setOnClickListener {
                AppUtils.downloadFile(
                    context, data.files[0].link_original
                )
            }
        }
    }

    /**
     * gui file nhan lai (ben trai)
     */
    inner class FileTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageFileLeftBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessagesTheir(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                binding.nameMedia,
                binding.imvAvatar,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            binding.nameMedia.text = data.sender.full_name

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            binding.tvSizeFile.text = getStringSizeLengthFile(data.files[0].size)
            binding.tvNameFile.text = getNameNoType(data.files[0].name_file!!)
            binding.tvTypeFile.text = AppUtils.getMimeType(data.files[0].link_original!!)
            binding.tvTypeFile.setEllipsize(TextUtils.TruncateAt.MARQUEE)
            binding.tvTypeFile.setMarqueeRepeatLimit(-1)
            binding.tvTypeFile.setSelected(true)
            val typeMedia: String = AppUtils.getMimeType(data.files[0].link_original!!)!!

            if (typeMedia == MediaTypeEnum.TYPE_JPG.toString() || typeMedia == MediaTypeEnum.TYPE_JPEG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_PNG.toString() || typeMedia == MediaTypeEnum.TYPE_SVG.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_photo,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_DOCX.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_word,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_AI.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_ai,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_DMG.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_dmg,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_XLSX.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_excel,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_HTML.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_html,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_MP3.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_music,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_PDF.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_pdf,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_PPTX.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_powerpoint,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_PSD.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_psd,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_REC.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_record,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_EXE.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_setup,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_SKETCH.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_sketch,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_TXT.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_txt,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_MP4.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_video,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_XML.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_xml,
                        null
                    )
                )
            } else if (typeMedia == MediaTypeEnum.TYPE_ZIP.toString()) {
                binding.imvTypeFile.setImageDrawable(
                    context.resources.getDrawable(
                        R.drawable.icon_file_zip,
                        null
                    )
                )
            } else binding.imvTypeFile.setImageDrawable(
                context.resources.getDrawable(
                    R.drawable.icon_file_attach,
                    null
                )
            )

            binding.tvOpen.setOnClickListener {
                AppUtils.downloadFile(
                    context, data.files[0].link_original
                )
            }
        }
    }

    /**
     * thong bao tin goi dien gui di ( ben phai )
     */
    inner class CallMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageCallRightBinding.bind(view)


        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {
            val data = dataSource[position]

            //Default data
            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            checkUserViewMessage(
                binding.rclUserView,
                binding.send.lnSendMessage,
                data,
                position,
                binding.lnView,
                binding.lnUserView,
                binding.tvMoreView
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            if (position != 0) {
                binding.lnView.visibility = View.GONE
            }

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.tvTime.visibility == View.GONE) {
                if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                    lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_10).toInt())
                } else {
                    lp.setMargins(0, 0, 0, 0)
                }
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_20).toInt())
            }

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            if (data.message_type == SocketChatMessageTypeEnum.CALL_AUDIO.toString().toInt()) {
                when (data.call_status) {
                    "call_complete" -> {
                        binding.tvStatusCall.text = context.getString(R.string.call_away)
                        binding.tvMessage.text = data.call_time
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.decor_csc_voicecall_out_small,
                            0,
                            0,
                            0
                        )
                    }
                    "call_cancel" -> {
                        binding.tvStatusCall.text = context.getString(R.string.you_canceled)
                        binding.tvMessage.text = context.getString(R.string.voice_calls)
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.decor_csc_voicecall_out_miss_small,
                            0,
                            0,
                            0
                        )
                    }
                    "call_no_answer" -> {
                        binding.tvStatusCall.text =
                            context.getString(R.string.the_recipient_did_not_respond)
                        binding.tvMessage.text = context.getString(R.string.voice_calls)
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.decor_csc_voicecall_out_miss_small,
                            0,
                            0,
                            0
                        )
                    }
                    "call_reject" -> {
                        binding.tvStatusCall.text = context.getString(R.string.recipients_reject)
                        binding.tvMessage.text = context.getString(R.string.voice_calls)
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.icn_csc_voicecall_cancel_small,
                            0,
                            0,
                            0
                        )
                    }
                }
            } else {
                when (data.call_status) {
                    "call_complete" -> {
                        binding.tvStatusCall.text = context.getString(R.string.go_video_call)
                        binding.tvMessage.text = data.call_time
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.decor_csc_videocall_out_small,
                            0,
                            0,
                            0
                        )
                    }
                    "call_no_answer" -> {
                        binding.tvStatusCall.text =
                            context.getString(R.string.the_recipient_did_not_respond)
                        binding.tvMessage.text = context.getString(R.string.video_calls)
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.decor_csc_videocall_miss_small,
                            0,
                            0,
                            0
                        )
                    }
                    "call_cancel" -> {
                        binding.tvStatusCall.text = context.getString(R.string.you_canceled)
                        binding.tvMessage.text = context.getString(R.string.video_calls)
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.decor_csc_videocall_miss_small,
                            0,
                            0,
                            0
                        )
                    }
                    "call_reject" -> {
                        binding.tvStatusCall.text = context.getString(R.string.recipients_reject)
                        binding.tvMessage.text = context.getString(R.string.video_calls)
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.icn_csc_videocall_cancel_small,
                            0,
                            0,
                            0
                        )
                    }
                }
            }

            binding.tvRecall.setOnClickListener {
                EventBus.getDefault().post(EvenBusNewCall(data.message_type))
            }
        }
    }

    /**
     * thong bao tin goi dien nhan lai ( ben trai )
     */
    inner class CallTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageCallLeftBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessagesTheir(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                binding.nameMedia,
                binding.imvAvatar,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.tvTime.visibility == View.GONE) {
                if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                    lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_10).toInt())
                } else {
                    lp.setMargins(0, 0, 0, 0)
                }
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.size_20).toInt())
            }

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            if (data.message_type == SocketChatMessageTypeEnum.CALL_AUDIO.toString().toInt()) {
                when (data.call_status) {
                    "call_complete" -> {
                        binding.tvStatusCall.text = context.getString(R.string.incoming_voice_calls)
                        binding.tvMessage.text = data.call_time
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.decor_csc_voicecall_in_small,
                            0,
                            0,
                            0
                        )
                    }
                    "call_no_answer", "call_cancel" -> {
                        binding.tvStatusCall.text = context.getString(R.string.you_missed)
                        binding.tvStatusCall.setTextColor(context.getColor(R.color.red_call))
                        binding.tvMessage.text = context.getString(R.string.voice_calls)
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.icn_csc_voicecall_reject_small,
                            0,
                            0,
                            0
                        )
                    }
                    "call_reject" -> {
                        binding.tvStatusCall.text = context.getString(R.string.you_refused)
                        binding.tvMessage.text = context.getString(R.string.voice_calls)
                        binding.tvStatusCall.setTextColor(context.getColor(R.color.red_call))
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.icn_csc_voicecall_cancel_small,
                            0,
                            0,
                            0
                        )
                    }
                }
            } else if (data.message_type == SocketChatMessageTypeEnum.CALL_VIDEO.toString()
                    .toInt()
            ) {
                when (data.call_status) {
                    "call_complete" -> {
                        binding.tvStatusCall.text = context.getString(R.string.incoming_video_call)
                        binding.tvMessage.text = data.call_time
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.decor_csc_videocall_in_small,
                            0,
                            0,
                            0
                        )
                    }
                    "call_no_answer", "call_cancel" -> {
                        binding.tvStatusCall.text = context.getString(R.string.you_missed)
                        binding.tvMessage.text = context.getString(R.string.video_calls)
                        binding.tvStatusCall.setTextColor(context.getColor(R.color.red_call))
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.icn_csc_videocall_miss_small,
                            0,
                            0,
                            0
                        )
                    }
                    "call_reject" -> {
                        binding.tvStatusCall.text = context.getString(R.string.you_refused)
                        binding.tvMessage.text = context.getString(R.string.video_calls)
                        binding.tvStatusCall.setTextColor(context.getColor(R.color.red_call))
                        binding.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.icn_csc_videocall_cancel_small,
                            0,
                            0,
                            0
                        )
                    }
                }
            }

            binding.tvRecall.setOnClickListener {
                EventBus.getDefault().post(
                        EvenBusNewCall(
                                data.message_type
                        )
                )
            }
        }
    }

    /**
     * gui danh thiep gui di ( ben phai )
     */
    inner class ContactMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageContactRightBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {
            val data = dataSource[position]

            //Default data
            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            checkUserViewMessage(
                binding.rclUserView,
                binding.send.lnSendMessage,
                data,
                position,
                binding.lnView,
                binding.lnUserView,
                binding.tvMoreView
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            if (position != 0) {
                binding.lnView.visibility = View.GONE
            }

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            AppUtils.callPhotoAvatar(
                data.message_phone!!.avatar,
                binding.messageContact.avatarContact
            )
            binding.messageContact.nameContact.text = data.message_phone!!.full_name
        }
    }

    /**
     * gui danh thiep nhan lai ( ben trai )
     */
    inner class ContactTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageContactLeftBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessagesTheir(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                binding.nameMedia,
                binding.imvAvatar,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.padding_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            binding.nameMedia.text = data.sender.full_name

            var message = data.message
            if (data.list_tag_name.isNotEmpty()) {
                for (i in data.list_tag_name.indices) {
                    val tagName: String =
                        context.getString(R.string.font_tag_name_start) + data.list_tag_name[i].full_name + "</font>" + " "
                    message = message.replace(data.list_tag_name[i].full_name, tagName)
                }
            }
            AppUtils.callPhotoAvatar(
                data.message_phone!!.avatar,
                binding.messageContact.avatarContact
            )
            binding.messageContact.nameContact.text = data.message_phone!!.full_name
        }
    }

    /**
     *  gui link  gui di ( ben phai )
     */
    inner class LinkMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageLinkRightBinding.bind(view)

        private var youTubePlayer: YouTubePlayer? = null
        private var currentVideoId: String? = null

        private var config = false
        private var seconds = 0f
        var pictureInPictureIcon: ImageView? = null

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {
            val data = dataSource[position]
            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                position
            )

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            checkUserViewMessage(
                binding.rclUserView,
                binding.send.lnSendMessage,
                data,
                position,
                binding.lnView,
                binding.lnUserView,
                binding.tvMoreView
            )

            if (position != 0) {
                binding.lnView.visibility = View.GONE
            }
            if (!config) {
                lifecycle!!.addObserver(binding.link.youtube)
                initPictureInPicture(binding.link.youtube)
                config = true
            }
            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }

            val lp1 = binding.link.tvMediaSubTitlePost.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.tvTime.visibility == View.VISIBLE) {
                lp1.setMargins(0, 0, 0, 0)
            } else {
                lp1.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.dp8).toInt())
            }

            cueVideo(getVideo(data.message_link!!.cannonical_url), data)

            if (!config) {
                lifecycle!!.addObserver(binding.link.youtube)
                config = true
            }
            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }
            if (data.message.isNotEmpty()) {
                var message = data.message
                if (data.list_tag_name.isNotEmpty()) {
                    for (i in data.list_tag_name.indices) {
                        val tagName: String =
                            context.getString(R.string.font_tag_name_start) + data.list_tag_name[i].full_name + "</font>" + " "
                        message = message.replace(data.list_tag_name[i].full_name, tagName)
                    }
                }

                //Change data
                message = message.replace("\n", "<br>")
                binding.tvMessage.text = AXEmojiUtils.replaceWithEmojis(
                    itemView.context,
                    Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT), 24f
                )
                binding.tvMessage.visibility = View.VISIBLE
            } else {
                binding.tvMessage.text = data.message_link!!.cannonical_url
            }
            if (data.message_link!!.cannonical_url.isNotEmpty()) {
                binding.lnLink.visibility = View.VISIBLE
                if (data.message_link!!.cannonical_url.contains(itemView.context.getString(R.string.link_youtube_1)) || data.message_link!!.cannonical_url.contains(
                        itemView.context.getString(R.string.link_youtube_2)
                    )
                ) {
                    binding.link.lnNewFeedLinkPost.visibility = View.GONE
                    binding.link.youtube.visibility = View.VISIBLE
                    binding.link.youtube.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(@NotNull youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)
                            this@LinkMessageViewHolder.youTubePlayer = youTubePlayer
                            this@LinkMessageViewHolder.youTubePlayer!!.cueVideo(
                                currentVideoId!!,
                                0f
                            )
                            if (AppUtils.on_off_youtube) {
                                binding.link.youtube.getPlayerUiController().setVolume(
                                    context.getDrawable(
                                        R.drawable.ic_volume_on
                                    )!!
                                )
                                AppUtils.on_off_youtube = true
                                this@LinkMessageViewHolder.youTubePlayer!!.setVolume(100)
                            } else {
                                binding.link.youtube.getPlayerUiController().setVolume(
                                    context.getDrawable(
                                        R.drawable.ic_volume_off
                                    )!!
                                )
                                AppUtils.on_off_youtube = false
                                this@LinkMessageViewHolder.youTubePlayer!!.setVolume(0)
                            }
                        }

                        override fun onCurrentSecond(
                            @NotNull youTubePlayer: YouTubePlayer,
                            second: Float,
                        ) {
                            super.onCurrentSecond(youTubePlayer, second)
                            seconds = second
                        }
                    })
                } else {
                    binding.link.lnNewFeedLinkPost.visibility = View.VISIBLE
                    binding.link.youtube.visibility = View.GONE
                    AppUtils.callPhotoLocal(
                        data.message_link!!.media_thumb,
                        binding.link.imvMediaThumbPost
                    )
                    binding.link.tvMediaTitlePost.text = data.message_link!!.title
                    binding.link.tvMediaSubTitlePost.text = data.message_link!!.description
                }
            } else {
                binding.lnLink.visibility = View.GONE
            }
            binding.link.lnNewFeedLinkPost.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(data.message_link!!.cannonical_url))
                context.startActivity(intent)
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun initPictureInPicture(youTubePlayerView: YouTubePlayerView) {
            pictureInPictureIcon = ImageView(itemView.context)
            pictureInPictureIcon!!.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.ic_picture_in_picture_24dp
                )
            )
            youTubePlayerView.getPlayerUiController().addView(pictureInPictureIcon!!)
            youTubePlayerView.getPlayerUiController().showVideoTitle(true)
            youTubePlayerView.getPlayerUiController().setVolumeListener {
                if (AppUtils.on_off_youtube) {
                    youTubePlayerView.getPlayerUiController().setVolume(
                        context.getDrawable(
                            R.drawable.ic_volume_off
                        )!!

                    )
                    AppUtils.on_off_youtube = false
                    youTubePlayer!!.setVolume(0)
                } else {
                    youTubePlayerView.getPlayerUiController().setVolume(
                        context.getDrawable(
                            R.drawable.ic_volume_on
                        )!!
                    )
                    AppUtils.on_off_youtube = true
                    youTubePlayer!!.setVolume(100)
                }
            }
            youTubePlayerView.getPlayerUiController().showVolume(true)
            youTubePlayerView.getPlayerUiController().setCustomAction1(
                context.getDrawable(
                    R.drawable.btn_prev_video
                )!!
            ) { youTubePlayer!!.seekTo(seconds - 10) }
            youTubePlayerView.getPlayerUiController().setCustomAction2(
                context.getDrawable(
                    R.drawable.btn_next_video
                )!!
            ) { youTubePlayer!!.seekTo(seconds + 10) }
            youTubePlayerView.getPlayerUiController().showCustomAction1(false)
        }

        private fun cueVideo(videoId: String, messagesByGroup: MessagesByGroup) {
            currentVideoId = videoId
            if (youTubePlayer == null) return
            youTubePlayer!!.cueVideo(currentVideoId!!, messagesByGroup.seconds)
        }

        private fun getVideo(url: String): String {
            var link = ""
            val a: Int
            if (url.contains(context.getString(R.string.link_youtube_1))) {
                link = url.substring(17)
            } else if (url.contains(context.getString(R.string.link_youtube_4))) {
                link = url.substring(28)
            } else if (url.contains(context.getString(R.string.link_youtube_3))) {
                a = url.lastIndexOf("?")
                link = url.substring(27, a)
            }
            return link
        }
    }

    /**
     *  gui link  nhan lai ( ben phai )
     */
    inner class LinkTheirMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageLinkLeftBinding.bind(view)
        private var youTubePlayer: YouTubePlayer? = null
        private var currentVideoId: String? = null
        private var config = false
        private var seconds = 0f

        var pictureInPictureIcon: ImageView? = null

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(position: Int) {
            val data = dataSource[position]

            checkTimeMessages(
                data.today,
                data.created_at,
                binding.time.lnTimeHeader,
                binding.time.tvTimeHeader,
                binding.tvTime,
                position
            )

            setMarginStart(itemView, binding.time.lnTimeHeader, position)

            setReaction(
                binding.reaction.imgReactionOne,
                binding.reaction.imgReactionTwo,
                binding.reaction.imgReactionThree,
                binding.reaction.tvReactionCount,
                data,
                binding.reaction.lnReaction,
                binding.reaction.lnReactionMine,
                binding.reaction.imgReactionMe
            )

            val lp = binding.lnText.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.reaction.lnReaction.visibility == View.VISIBLE) {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_12dp).toInt())
            } else {
                lp.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.margin_4dp).toInt())
            }
            if (!config) {
                lifecycle!!.addObserver(binding.link.youtube)
                initPictureInPicture(binding.link.youtube)
                config = true
            }
            val lp1 = binding.link.tvMediaSubTitlePost.layoutParams as ViewGroup.MarginLayoutParams
            if (binding.tvTime.visibility == View.VISIBLE) {
                lp1.setMargins(0, 0, 0, 0)
            } else {
                lp1.setMargins(0, 0, 0, context.resources.getDimension(R.dimen.dp8).toInt())
            }

            cueVideo(getVideo(data.message_link!!.cannonical_url), data)
            if (!config) {
                lifecycle!!.addObserver(binding.link.youtube)
                config = true
            }

            AppUtils.callPhotoAvatar(data.sender.avatar, binding.imvAvatar)

            binding.nameMedia.text = data.sender.full_name

            itemView.setOnLongClickListener { v: View? ->
                revokeMessageHandler!!.onRevoke(data, v)
                true
            }
            if (data.message.isNotEmpty()) {
                var message = data.message
                if (data.list_tag_name.isNotEmpty()) {
                    for (i in data.list_tag_name.indices) {
                        val tagName: String =
                            context.getString(R.string.font_tag_name_start) + data.list_tag_name[i].full_name + "</font>" + " "
                        message = message.replace(data.list_tag_name[i].full_name, tagName)
                    }
                }

                //Change data
                message = message.replace("\n", "<br>")
                binding.tvMessage.text = AXEmojiUtils.replaceWithEmojis(
                    itemView.context,
                    Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT), 24f
                )
                binding.tvMessage.visibility = View.VISIBLE
            } else {
                binding.tvMessage.text = data.message_link!!.cannonical_url
            }
            if (data.message_link!!.cannonical_url.isNotEmpty()) {
                binding.lnLink.visibility = View.VISIBLE
                if (data.message_link!!.cannonical_url.contains(itemView.context.getString(R.string.link_youtube_1)) || data.message_link!!.cannonical_url.contains(
                        itemView.context.getString(R.string.link_youtube_2)
                    )
                ) {
                    binding.link.lnNewFeedLinkPost.visibility = View.GONE
                    binding.link.youtube.visibility = View.VISIBLE
                    binding.link.youtube.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)
                            this@LinkTheirMessageViewHolder.youTubePlayer = youTubePlayer
                            this@LinkTheirMessageViewHolder.youTubePlayer!!.cueVideo(
                                currentVideoId!!,
                                0f
                            )
                            if (AppUtils.on_off_youtube) {
                                binding.link.youtube.getPlayerUiController().setVolume(
                                    context.getDrawable(
                                        R.drawable.ic_volume_on
                                    )!!
                                )
                                AppUtils.on_off_youtube = true
                                this@LinkTheirMessageViewHolder.youTubePlayer!!.setVolume(100)
                            } else {
                                binding.link.youtube.getPlayerUiController().setVolume(
                                    context.getDrawable(
                                        R.drawable.ic_volume_off
                                    )!!
                                )
                                AppUtils.on_off_youtube = false
                                this@LinkTheirMessageViewHolder.youTubePlayer!!.setVolume(0)
                            }
                        }

                        override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                            super.onCurrentSecond(youTubePlayer, second)
                            seconds = second
                        }

                        override fun onStateChange(
                            youTubePlayer: YouTubePlayer,
                            state: PlayerConstants.PlayerState,
                        ) {
                            super.onStateChange(youTubePlayer, state)
                            this@LinkTheirMessageViewHolder.youTubePlayer = youTubePlayer
                            if (state === PlayerConstants.PlayerState.PLAYING) {
                                binding.link.youtube.getPlayerUiController()
                                    .setFullScreenButtonClickListener {
                                        this@LinkTheirMessageViewHolder.youTubePlayer!!.pause()
                                        clickFullScreen!!.clickFullScreen(
                                            currentVideoId,
                                            seconds,
                                            position
                                        )
                                    }
                                pictureInPictureIcon!!.setOnClickListener {
                                    this@LinkTheirMessageViewHolder.youTubePlayer!!.pause()
                                    clickFullScreen!!.clickPictureInPicture(
                                        currentVideoId,
                                        seconds,
                                        position
                                    )
                                }
                            }
                        }
                    })
                } else {
                    binding.link.lnNewFeedLinkPost.visibility = View.VISIBLE
                    binding.link.youtube.visibility = View.GONE
                    AppUtils.callPhotoLocal(
                        data.message_link!!.media_thumb,
                        binding.link.imvMediaThumbPost
                    )
                    binding.link.tvMediaTitlePost.text = data.message_link!!.title
                    binding.link.tvMediaSubTitlePost.text = data.message_link!!.description
                }
            } else {
                binding.lnLink.visibility = View.GONE
            }
            binding.link.lnNewFeedLinkPost.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(data.message_link!!.cannonical_url)
                )
                context.startActivity(intent)
            }
        }

        fun getVideo(url: String): String {
            var link = ""
            val a: Int
            if (url.contains(context.getString(R.string.link_youtube_1))) {
                link = url.substring(17)
            } else if (url.contains(context.getString(R.string.link_youtube_4))) {
                link = url.substring(28)
            } else if (url.contains(context.getString(R.string.link_youtube_3))) {
                a = url.lastIndexOf("?")
                link = url.substring(27, a)
            }
            return link
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun initPictureInPicture(youTubePlayerView: YouTubePlayerView) {
            pictureInPictureIcon = ImageView(itemView.context)
            pictureInPictureIcon!!.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.ic_picture_in_picture_24dp
                )
            )
            youTubePlayerView.getPlayerUiController().addView(pictureInPictureIcon!!)
            youTubePlayerView.getPlayerUiController().showVideoTitle(true)
            youTubePlayerView.getPlayerUiController().setVolumeListener {
                if (AppUtils.on_off_youtube) {
                    youTubePlayerView.getPlayerUiController().setVolume(
                        context.getDrawable(
                            R.drawable.ic_volume_off
                        )!!

                    )
                    AppUtils.on_off_youtube = false
                    youTubePlayer!!.setVolume(0)
                } else {
                    youTubePlayerView.getPlayerUiController().setVolume(
                        context.getDrawable(
                            R.drawable.ic_volume_on
                        )!!
                    )
                    AppUtils.on_off_youtube = true
                    youTubePlayer!!.setVolume(100)
                }
            }
            youTubePlayerView.getPlayerUiController().showVolume(true)
            youTubePlayerView.getPlayerUiController().setCustomAction1(
                context.getDrawable(
                    R.drawable.btn_prev_video
                )!!
            ) { youTubePlayer!!.seekTo(seconds - 10) }
            youTubePlayerView.getPlayerUiController().setCustomAction2(
                context.getDrawable(
                    R.drawable.btn_next_video
                )!!
            ) { youTubePlayer!!.seekTo(seconds + 10) }
            youTubePlayerView.getPlayerUiController().showCustomAction1(false)
        }

        private fun cueVideo(videoId: String, messagesByGroup: MessagesByGroup) {
            currentVideoId = videoId
            if (youTubePlayer == null) return
            youTubePlayer!!.cueVideo(currentVideoId!!, messagesByGroup.seconds)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSource(dataSource: ArrayList<MessagesByGroup>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message: MessagesByGroup = dataSource[position]
        if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.TEXT.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as TextMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.TEXT.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as TextTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.IMAGE.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as ImageMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.IMAGE.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as ImageTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.VIDEO.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as VideoMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.VIDEO.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as VideoTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.REPLY.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as ReplyMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.REPLY.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as ReplyTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.STICKER.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as StickerMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.ORDER.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as ChatOrderMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.ORDER.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as ChatOrderTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.STICKER.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as StickerTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.LINK.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as LinkMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.LINK.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as LinkTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.PINNED.toString()) || message.message_type == Integer.parseInt(
                SocketChatMessageTypeEnum.REMOVE_PINNED.toString()
            )
        ) {
            (holder as PinnedMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.ADD.toString())) {
            (holder as AddMessageViewHolder).bind(position)
        } else if (message.status == 0 && checkSenderMessage(message)) {
            (holder as RevokeMessageViewHolder).bind(position)
        } else if (message.status == 0 && !checkSenderMessage(message)) {
            (holder as RevokeTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.FILE.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as FileMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.FILE.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as FileTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CALL_AUDIO.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as CallMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CALL_AUDIO.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as CallTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CALL_VIDEO.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as CallMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CALL_VIDEO.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as CallTheirMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CONTACT.toString()) && checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as ContactMessageViewHolder).bind(position)
        } else if (message.message_type == Integer.parseInt(SocketChatMessageTypeEnum.CONTACT.toString()) && !checkSenderMessage(
                message
            ) && message.status != 0
        ) {
            (holder as ContactTheirMessageViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    fun checkTimeMessagesTheir(
        toDay: Int,
        str_date: String,
        lnTimeHeader: LinearLayout,
        timeHeader: TextView,
        timeMessage: TextView,
        name: View,
        avatar: View,
        position: Int,
    ) {
        if (toDay == 1) {
            lnTimeHeader.visibility = View.VISIBLE
            timeHeader.text = getToDayMessage(str_date)
        } else {
            lnTimeHeader.visibility = View.GONE
        }
        timeMessage.text = getTimeMessage(str_date)
        if (position == 0 && dataSource.size > 1) {
            if (checkTimeNew(str_date, position)
                && dataSource[position + 1].message_type != SocketChatMessageTypeEnum.PINNED.toString()
                    .toInt() && dataSource[position + 1]
                    .message_type != SocketChatMessageTypeEnum.ADD.toString().toInt()
            ) {
                name.visibility = View.GONE
                avatar.visibility = View.INVISIBLE
            } else {
                name.visibility = View.VISIBLE
                avatar.visibility = View.VISIBLE
            }
            timeMessage.visibility = View.VISIBLE
        } else {
            //Giua
            if (checkTimeOldAndNew(
                    str_date,
                    position
                ) && dataSource[position].sender.member_id == dataSource[position - 1]
                    .sender.member_id && dataSource[position].sender.member_id == dataSource[position + 1]
                    .sender.member_id && dataSource[position - 1]
                    .message_type != SocketChatMessageTypeEnum.ADD.toString()
                    .toInt() && dataSource[position - 1].message_type != SocketChatMessageTypeEnum.PINNED.toString()
                    .toInt() && dataSource[position + 1].message_type != SocketChatMessageTypeEnum.ADD.toString()
                    .toInt() && dataSource[position + 1]
                    .message_type != SocketChatMessageTypeEnum.PINNED.toString().toInt()
            ) {
                name.visibility = View.GONE
                avatar.visibility = View.INVISIBLE
                timeMessage.visibility = View.GONE
            } else {
                //Cuoi
                if (checkTimeNew(str_date, position) && !checkTimeOld(str_date, position)
                    && dataSource[position].sender.member_id == dataSource[position + 1]
                        .sender.member_id && dataSource[position + 1].message_type != SocketChatMessageTypeEnum.ADD.toString()
                        .toInt() && dataSource[position + 1]
                        .message_type != SocketChatMessageTypeEnum.PINNED.toString().toInt()
                ) {
                    name.visibility = View.GONE
                    avatar.visibility = View.INVISIBLE
                    timeMessage.visibility = View.VISIBLE
                    //Dau
                } else if (checkTimeOld(str_date, position) && !checkTimeNew(
                        str_date,
                        position
                    ) && dataSource[position].sender.member_id == dataSource[position - 1]
                        .sender.member_id && dataSource[position - 1]
                        .message_type != SocketChatMessageTypeEnum.ADD.toString()
                        .toInt() && dataSource[position - 1]
                        .message_type != SocketChatMessageTypeEnum.PINNED.toString().toInt()
                ) {
                    name.visibility = View.VISIBLE
                    avatar.visibility = View.VISIBLE
                    timeMessage.visibility = View.GONE
                } else {
                    name.visibility = View.VISIBLE
                    avatar.visibility = View.VISIBLE
                    timeMessage.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getToDayMessage(str_date: String): String? {
        try {
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
            val time: Date = format.parse(str_date)
            @SuppressLint("SimpleDateFormat") val dayFormat = SimpleDateFormat("dd/MM/yyyy")
            @SuppressLint("SimpleDateFormat") val timeFormat = SimpleDateFormat("HH:mm")
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val currentTime: String? = sdf.format(Date())
            return if (currentTime == dayFormat.format(time)) {
                String.format(
                    "%s%s%s",
                    timeFormat.format(time),
                    ", ",
                    context.getString(R.string.to_day)
                )
            } else {
                java.lang.String.format(
                    "%s%s%s",
                    timeFormat.format(time),
                    ", ",
                    dayFormat.format(time)
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }

    private fun getTimeMessage(str_date: String): String? {
        try {
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
            val time: Date? = format.parse(str_date)
            @SuppressLint("SimpleDateFormat") val newFormat = SimpleDateFormat("HH:mm")
            return newFormat.format(time!!)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }

    private fun checkTimeNew(str_date: String, position: Int): Boolean {
        return try {
            if (position + 1 == dataSource.size) {
                return false
            }
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("yyyy/MM/dd hh:mm")
            try {
                return format.parse(str_date) == format.parse(
                    dataSource[position + 1].created_at
                )
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            false
        } catch (ex: java.lang.Exception) {
            false
        }
    }

    private fun checkTimeOldAndNew(str_date: String, position: Int): Boolean {
        return try {
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("yyyy/MM/dd hh:mm")
            try {
                return format.parse(str_date) == format.parse(
                    dataSource[position - 1].created_at
                ) && format.parse(str_date) == format.parse(
                    dataSource[position + 1].created_at
                )
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            false
        } catch (e: java.lang.Exception) {
            false
        }
    }

    private fun checkTimeOld(str_date: String, position: Int): Boolean {
        return try {
            if (position + 1 == dataSource.size) {
                return false
            }
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("yyyy/MM/dd hh:mm")
            try {
                return format.parse(str_date) == format.parse(
                    dataSource[position - 1].created_at
                )
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            false
        } catch (e: java.lang.Exception) {
            false
        }
    }

    private fun setReaction(imageView: ImageView, name: Int) {
        when (name) {
            3 -> imageView.setImageResource(R.drawable.ic_like)
            1 -> imageView.setImageResource(R.drawable.ic_heart)
            2 -> imageView.setImageResource(R.drawable.ic_smile)
            6 -> imageView.setImageResource(R.drawable.ic_wow)
            5 -> imageView.setImageResource(R.drawable.ic_angry)
            4 -> imageView.setImageResource(R.drawable.ic_sad)
        }
    }

    private fun setImageReaction(imageView: ImageView, reactionItem: ReactionItem) {
        if (reactionItem.number > 0) {
            imageView.visibility = View.VISIBLE
        } else {
            imageView.visibility = View.GONE
        }
        when (reactionItem.name) {
            3 -> imageView.setImageResource(R.drawable.ic_like)
            1 -> imageView.setImageResource(R.drawable.ic_heart)
            2 -> imageView.setImageResource(R.drawable.ic_smile)
            6 -> imageView.setImageResource(R.drawable.ic_wow)
            5 -> imageView.setImageResource(R.drawable.ic_angry)
            4 -> imageView.setImageResource(R.drawable.ic_sad)
        }
    }

    private fun getReactionItem(messages: MessagesByGroup): List<ReactionItem> {
        val reactionItemList: ArrayList<ReactionItem> = ArrayList()
        val reactionItemLike = ReactionItem()
        reactionItemLike.name = 3
        reactionItemLike.number = messages.reactions.like
        val reactionItemWow = ReactionItem()
        reactionItemWow.name = 6
        reactionItemWow.number = messages.reactions.wow
        val reactionItemAngy = ReactionItem()
        reactionItemAngy.name = 5
        reactionItemAngy.number = messages.reactions.angry
        val reactionItemSad = ReactionItem()
        reactionItemSad.name = 4
        reactionItemSad.number = messages.reactions.sad
        val reactionItemSmile = ReactionItem()
        reactionItemSmile.name = 2
        reactionItemSmile.number = messages.reactions.smile
        val reactionItemLove = ReactionItem()
        reactionItemLove.name = 1
        reactionItemLove.number = messages.reactions.love
        reactionItemList.add(reactionItemLike)
        reactionItemList.add(reactionItemWow)
        reactionItemList.add(reactionItemAngy)
        reactionItemList.add(reactionItemSad)
        reactionItemList.add(reactionItemSmile)
        reactionItemList.add(reactionItemLove)
        return reactionItemList
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun setReaction(
        imageOne: ImageView?,
        imageTwo: ImageView?,
        imageThree: ImageView?,
        count: TextView,
        message: MessagesByGroup,
        lnReaction: LinearLayout,
        lnReactionMine: LinearLayout,
        imgReactionMe: ImageView?,
    ) {
        if (message.reactions.reactions_count == 0) {
            lnReaction.visibility = View.GONE
            lnReactionMine.visibility = View.GONE
        } else {
            lnReaction.visibility = View.VISIBLE
            lnReactionMine.visibility = View.VISIBLE
            if (message.reactions.last_reactions_id != CurrentUser.getCurrentUser(context)!!.id) {
                lnReactionMine.visibility = View.GONE
            } else {
                lnReactionMine.visibility = View.VISIBLE
                setReaction(imgReactionMe!!, message.reactions.last_reactions)
            }
            count.text = message.reactions.reactions_count.toString() + ""
            val reactionItems = getReactionItem(message)
            Collections.sort(reactionItems, NumberComparator())
            setImageReaction(imageOne!!, reactionItems[5])
            setImageReaction(imageTwo!!, reactionItems[4])
            setImageReaction(imageThree!!, reactionItems[3])
        }
    }

    fun setMarginStart(view: View, time: View, position: Int) {
        val lp = view.layoutParams as ViewGroup.MarginLayoutParams
        if (position == dataSource.size - 1 && time.visibility == View.GONE) {
            lp.setMargins(0, context.resources.getDimension(R.dimen.dp8).toInt(), 0, 0)
        }
    }

    fun checkTimeMessages(
        toDay: Int,
        str_date: String,
        lnTimeHeader: LinearLayout,
        timeHeader: TextView,
        timeMessage: TextView,
        position: Int,
    ) {
        if (toDay == 1) {
            lnTimeHeader.visibility = View.VISIBLE
            timeHeader.text = getToDayMessage(str_date)
            timeMessage.visibility = View.GONE
        } else {
            lnTimeHeader.visibility = View.GONE
            timeMessage.visibility = View.VISIBLE
        }
        timeMessage.text = getTimeMessage(str_date)
        if (position != 0 && position != dataSource.size - 1) {
            if (checkTimeOld(str_date, position)) {
                if (dataSource[position].sender.member_id == dataSource[position - 1].sender.member_id && dataSource[position - 1]
                        .message_type != SocketChatMessageTypeEnum.ADD.toString()
                        .toInt() && dataSource[position - 1]
                        .message_type != SocketChatMessageTypeEnum.PINNED.toString().toInt()
                ) {
                    timeMessage.visibility = View.GONE
                } else timeMessage.visibility = View.VISIBLE
            } else timeMessage.visibility = View.VISIBLE
        } else {
            timeMessage.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    fun checkUserViewMessage(
        rclUserView: RecyclerView,
        lnSendMessage: LinearLayout,
        item: MessagesByGroup,
        position: Int,
        lnView: LinearLayout,
        lnUserView: LinearLayout,
        tvMoreView: TextView,
    ) {
        if (position == 0 && item.message_viewed.isNotEmpty()) {
            lnSendMessage.visibility = View.GONE
            lnView.visibility = View.VISIBLE
            rclUserView.visibility = View.VISIBLE
            lnUserView.visibility = View.GONE
            setAdapterUserViewMessage(rclUserView, item.message_viewed)
            if (item.message_viewed.size > 7) {
                lnUserView.visibility = View.VISIBLE
                tvMoreView.text = "+" + (item.message_viewed.size - 7)
            } else {
                lnUserView.visibility = View.GONE
            }
        } else if (position == 0 && item.message_viewed.isEmpty()) {
            lnSendMessage.visibility = View.VISIBLE
            rclUserView.visibility = View.GONE
            lnView.visibility = View.VISIBLE
            lnUserView.visibility = View.GONE
        } else if (position != 0 && item.message_viewed.isEmpty()) {
            lnSendMessage.visibility = View.GONE
            rclUserView.visibility = View.GONE
            lnUserView.visibility = View.GONE
            lnView.visibility = View.GONE
        }
    }

    private fun setAdapterUserViewMessage(
        recyclerView: RecyclerView,
        messageViewList: ArrayList<MessageView>,
    ) {
        recyclerView.layoutManager =
            CustomLayoutManager(recyclerView.context, RecyclerView.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator!!.changeDuration = 0
        val adapter = UserViewMessageAdapter(context)
        if (messageViewList.size > 7) {

            adapter.setDataSource(
                ArrayList<MessageView>(
                    messageViewList.subList(
                        messageViewList.size - 7,
                        messageViewList.size
                    )
                )
            )
        } else {
            adapter.setDataSource(messageViewList)
        }
        recyclerView.adapter = adapter
    }

    fun setAdapterImageVideo(recyclerView: RecyclerView, item: MessagesByGroup) {
        val adapter = LoadImageChatAdapter(context)
        adapter.setDataSource(item.files as ArrayList<FileChat>)
        val layoutManager = FlexboxLayoutManager(recyclerView.context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.CENTER
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun getNameNoType(path: String): String {
        val filename = path.substring(path.lastIndexOf("/") + 1)
        val parts = filename.split("\\.").toTypedArray()
        return parts[0]
    }

    fun getStringSizeLengthFile(size: Long): String {
        val df = DecimalFormat("0.00")
        val sizeKb = 1024.0f
        val sizeMb = sizeKb * sizeKb
        val sizeGb = sizeMb * sizeKb
        val sizeTerra = sizeGb * sizeKb
        if (size < sizeMb) return df.format(size / sizeKb)
            .toString() + " KB" else if (size < sizeGb) return df.format(size / sizeMb)
            .toString() + " MB" else if (size < sizeTerra) return df.format(size / sizeGb)
            .toString() + " GB"
        return ""
    }

    @JvmName("setClickFullScreen1")
    fun setClickFullScreen(clickFullScreen: ClickFullScreen) {
        this.clickFullScreen = clickFullScreen
    }

    @JvmName("setLifecycle1")
    fun setLifecycle(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }
}

