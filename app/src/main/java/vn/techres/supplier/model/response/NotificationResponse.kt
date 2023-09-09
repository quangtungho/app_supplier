package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.datamodel.DataNotification

@JsonObject
class NotificationResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataNotification()
}