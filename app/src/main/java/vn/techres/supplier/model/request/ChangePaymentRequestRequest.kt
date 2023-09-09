package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class ChangePaymentRequestRequest {
    @JsonField(name = ["status"])
    var status: Int? = 0


}