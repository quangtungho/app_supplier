package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class StatusAdditionFeesRequest {
    @JsonField(name = ["status"])
    var status: Int? = 0

    @JsonField(name = ["note"])
    var note: String? = ""

}