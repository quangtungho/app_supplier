package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class Sticker {
    @JsonField(name = ["link_original"])
    var link_original = ""

    @JsonField(name = ["_id"])
    var _id = ""
}