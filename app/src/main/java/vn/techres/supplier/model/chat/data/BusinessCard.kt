package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class BusinessCard {
    @JsonField(name = ["member_id"])
    var member_id = 0

    @JsonField(name = ["full_name"])
    var full_name = ""

    @JsonField(name = ["phone"])
    var phone = ""

    @JsonField(name = ["role_id"])
    var role_id = 0

    @JsonField(name = ["role_name"])
    var role_name = ""

    @JsonField(name = ["avatar"])
    var avatar = ""
}