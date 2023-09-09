package vn.techres.supplier.model.chat.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.chat.data.Members

@JsonObject
class AddMemberRequest {
    @JsonField(name = ["members"])
    var members = ArrayList<Members>()
}