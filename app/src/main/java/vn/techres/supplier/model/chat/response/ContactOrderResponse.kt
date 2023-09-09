package vn.techres.supplier.model.chat.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.chat.data.DetailGroup
import vn.techres.supplier.model.response.BaseResponse

class ContactOrderResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DetailGroup()
}