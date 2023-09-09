package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.chat.data.ChatTag
import java.util.*

@JsonObject
class MessageReply {
    constructor(
        member_id: Int,
        group_id: String,
        message: String,
        random_key: String,
        message_type: Int,

        ) {
        this.member_id = member_id
        this.group_id = group_id
        this.message = message
        this.random_key = random_key
        this.message_type = message_type
    }


    @JsonField(name = ["member_id"])
    var member_id = 0

    @JsonField(name = ["group_id"])
    var group_id = ""

    @JsonField(name = ["message"])
    var message = ""

    @JsonField(name = ["random_key"])
    var random_key = ""

    @JsonField(name = ["message_type"])
    var message_type = 0

    @JsonField(name = ["list_tag_name"])
    var list_tag_name: List<ChatTag> = ArrayList()

    @JsonField(name = ["app_name"])
    var app_name = "supplier"

    @JsonField(name = ["code"])
    var code = ""
}