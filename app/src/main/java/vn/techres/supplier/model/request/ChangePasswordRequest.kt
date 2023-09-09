package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class ChangePasswordRequest {
    @JsonField(name = ["old_password"])
    var old_password: String? = ""

    @JsonField(name = ["new_password"])
    var new_password: String? = ""

    @JsonField(name = ["node_access_token"])
    var node_access_token: String? = ""

    @JsonField(name = ["type_user"])
    var type_user: String? = ""


}