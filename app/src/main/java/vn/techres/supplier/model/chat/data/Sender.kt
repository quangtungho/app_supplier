package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class Sender {
    @JsonField(name = ["full_name"])
    var full_name: String? = null

    @JsonField(name = ["avatar"])
    var avatar: String? = null

    @JsonField(name = ["member_id"])
    var member_id = 0
    @JsonField(name = ["permissions"])
    var permissions = 0

    @JsonField(name = ["app_name"])
    var app_name = "supplier"

    constructor(full_name: String?, avatar: String?, member_id: Int) {
        this.full_name = full_name
        this.avatar = avatar
        this.member_id = member_id
    }

    constructor()


}