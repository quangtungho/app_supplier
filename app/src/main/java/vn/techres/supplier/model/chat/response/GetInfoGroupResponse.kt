package vn.techres.supplier.model.chat.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.chat.data.GetInfoGroup
import vn.techres.supplier.model.response.BaseResponse

class GetInfoGroupResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = GetInfoGroup()
}