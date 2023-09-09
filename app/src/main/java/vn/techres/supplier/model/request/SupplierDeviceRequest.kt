package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

@JsonObject
class SupplierDeviceRequest {
    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("device_uid")
    var device_uid: String? = ""

    @JsonProperty("push_token")
    var push_token: String? = ""

    @JsonProperty("os_name")
    var os_name: String? = "tms_supplier_android"

    @JsonProperty("customer_id")
    var customer_id: Int? = 0

}

