package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataDetailGroupChat

class DetailGroupChatResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataDetailGroupChat()
}