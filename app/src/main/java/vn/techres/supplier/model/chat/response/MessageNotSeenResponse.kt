package vn.techres.supplier.model.chat.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.chat.data.MessageNotSeen
import vn.techres.supplier.model.response.BaseResponse

class MessageNotSeenResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = MessageNotSeen()
}