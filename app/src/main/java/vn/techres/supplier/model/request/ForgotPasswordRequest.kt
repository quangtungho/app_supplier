package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class ForgotPasswordRequest {
    @JsonField(name = ["username"])
    var username: String? = ""

    @JsonField(name = ["baisic_token"])
    var baisic_token: String? = ""


}