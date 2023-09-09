package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class ReturnBillOrderRequest {
    @JsonField(name = ["supplier_warehouse_session_type"])
    var supplier_warehouse_session_type: Int? = 0


}