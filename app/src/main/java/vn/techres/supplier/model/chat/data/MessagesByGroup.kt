package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.util.*

@JsonObject
class MessagesByGroup {
    @JsonField(name = ["_id"])
    private var _id = ""

    @JsonField(name = ["receiver_id"])
    var receiver_id = ""

    @JsonField(name = ["message_type"])
    var message_type = 0

    @JsonField(name = ["random_key"])
    var random_key = ""

    @JsonField(name = ["status"])
    var status = 1

    @JsonField(name = ["today"])
    var today = 1

    @JsonField(name = ["sender"])
    var sender = Sender()

    @JsonField(name = ["message_viewed"])
    var message_viewed: ArrayList<MessageView> = ArrayList()

    @JsonField(name = ["message_pinned"])
    var message_pinned = MessagePinned()

    @JsonField(name = ["message_reply"])
    var message_reply = MessageChatReply()

    @JsonField(name = ["message"])
    var message = ""

    @JsonField(name = ["reactions"])
    var reactions = Reactions()

    @JsonField(name = ["message_order"])
    var message_order = MessageOrder()

    @JsonField(name = ["list_tag_name"])
    var list_tag_name: List<ChatTag> = ArrayList()

    @JsonField(name = ["files"])
    var files: List<FileChat> = ArrayList()

    @JsonField(name = ["interval_of_time"])
    var interval_of_time = ""

    @JsonField(name = ["created_at"])
    var created_at = ""

    @JsonField(name = ["message_link"])
    var message_link: MessageLink? = null

    @JsonField(name = ["key_message"])
    var key_message = ""

    @JsonField(name = ["list_member"])
    var list_member: List<Members> = ArrayList()

    @JsonField(name = ["call_status"])
    var call_status = ""

    @JsonField(name = ["call_time"])
    var call_time = ""

    @JsonField(name = ["message_phone"])
    var message_phone: BusinessCard? = null

    var typeOnTime = 0
    var seconds = 0f
}