package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class StatusRequest {
    @JsonField(name = ["status"])
    var status: Int? = 0

    @JsonField(name = ["reason"])
    var reason: String? = ""

    @JsonField(name = ["supplier_warehouse_session_type"])
    var supplier_warehouse_session_type: Int? = 0
    @JsonField(name = ["supplier_warehouse_session_status"])
    var supplier_warehouse_session_status: Int? = 0
}