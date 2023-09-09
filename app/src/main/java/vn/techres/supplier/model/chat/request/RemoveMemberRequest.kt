package vn.techres.supplier.model.chat.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class RemoveMemberRequest {
    @JsonField(name = ["member_id"])
    var member_id = 0
}