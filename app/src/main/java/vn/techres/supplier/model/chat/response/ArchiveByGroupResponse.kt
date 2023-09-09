package vn.techres.supplier.model.chat.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.chat.data.MessageFile
import vn.techres.supplier.model.response.BaseResponse

class ArchiveByGroupResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = MessageFile()
}