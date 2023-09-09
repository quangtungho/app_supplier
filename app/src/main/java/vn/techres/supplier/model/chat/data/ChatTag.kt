package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class ChatTag {
    @JsonField(name = ["member_id"])
    var member_id = 0

    @JsonField(name = ["avatar"])
    var avatar = ""

    @JsonField(name = ["full_name"])
    var full_name = ""

    @JsonField(name = ["key_tag_name"])
    var key_tag_name = ""

    constructor(member_id: Int, avatar: String, full_name: String, key_tag_name: String) {
        this.member_id = member_id
        this.avatar = avatar
        this.full_name = full_name
        this.key_tag_name = key_tag_name
    }
}