package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class MessageChatReply {
    @JsonField(name = ["_id"])
    var _id = ""

    @JsonField(name = ["message_type"])
    var message_type = 0

    @JsonField(name = ["random_key"])
    var random_key: String? = null

    @JsonField(name = ["status"])
    var status = 0

    @JsonField(name = ["today"])
    var today = 0

    @JsonField(name = ["sender"])
    var sender: Sender? = null

    @JsonField(name = ["files"])
    var files: List<FileChat>? = null

    @JsonField(name = ["message"])
    var message: String? = null

    @JsonField(name = ["list_tag_name"])
    var list_tag_name: List<ChatTag>? = null

    @JsonField(name = ["message_link"])
    var message_link: MessageLink? = null
}