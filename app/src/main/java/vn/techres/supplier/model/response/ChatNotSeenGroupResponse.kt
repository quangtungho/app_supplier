
package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataChatNotSeenGroup

class ChatNotSeenGroupResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataChatNotSeenGroup()

}