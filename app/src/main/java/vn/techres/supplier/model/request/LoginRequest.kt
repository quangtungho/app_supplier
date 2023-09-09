package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class LoginRequest {
    @JsonField(name = ["username"])
    var username: String? = null

    @JsonField(name = ["password"])
    var password: String? = null
}

