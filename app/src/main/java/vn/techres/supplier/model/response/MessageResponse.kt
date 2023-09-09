package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.chat.data.MessagesByGroup

@JsonObject
class MessageResponse : BaseResponse(){
    @JsonField(name = ["data"])
    var data = MessagesByGroup()
}