package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class MessageView {
    @JsonField(name = ["member_id"])
    var member_id = 0

    @JsonField(name = ["full_name"])
    var full_name: String? = null

    @JsonField(name = ["group_id"])
    var group_id: String? = null

    @JsonField(name = ["avatar"])
    var avatar: String? = null

    @JsonField(name = ["app_name"])
    var app_name = ""
}