package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.chat.data.MessageChatGroup

class MessageByGroupResponse : BaseResponse()  {
    @JsonField(name = ["data"])
    var data: MessageChatGroup? = null
}