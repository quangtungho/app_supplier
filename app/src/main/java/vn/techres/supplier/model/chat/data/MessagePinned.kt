package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class MessagePinned {
    @JsonField(name = ["_id"])
    private var _id = ""

    @JsonField(name = ["message_type"])
    var message_type = 0

    @JsonField(name = ["random_key"])
    var random_key = ""

    @JsonField(name = ["status"])
    var status = 0

    @JsonField(name = ["today"])
    var today = 0

    @JsonField(name = ["sender"])
    var sender = Sender()

    @JsonField(name = ["message"])
    var message = ""

    @JsonField(name = ["created_at"])
    var created_at = ""

    @JsonField(name = ["color"])
    var color = ""

    @JsonField(name = ["files"])
    var files: List<FileChat> = ArrayList()

    @JsonField(name = ["list_tag_name"])
    var list_tag_name: List<ChatTag> = ArrayList()

    @JsonField(name = ["message_link"])
    var message_link: MessageLink? = null
}