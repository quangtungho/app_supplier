package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.entity.User

@JsonObject
class LoginResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: User? = null
}
