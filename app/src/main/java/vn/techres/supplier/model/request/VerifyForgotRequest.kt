package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class VerifyForgotRequest {
    @JsonField(name = ["username"])
    var username: String? = ""

    @JsonField(name = ["verify_code"])
    var verify_code: String? = ""

    @JsonField(name = ["new_password"])
    var new_password: String? = ""
}