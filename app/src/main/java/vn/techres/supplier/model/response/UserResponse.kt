package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.entity.User

class UserResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: User? = null
}
