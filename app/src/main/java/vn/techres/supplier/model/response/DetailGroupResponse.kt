package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.chat.data.DetailGroup

class DetailGroupResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: DetailGroup? = null
}