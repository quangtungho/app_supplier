package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.entity.UserNode

class UserNodeResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: UserNode? = null
}