package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.chat.data.FileChat

class FileChatResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = FileChat()
}