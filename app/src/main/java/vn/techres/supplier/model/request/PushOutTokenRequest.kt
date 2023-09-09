package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

@JsonObject
class PushOutTokenRequest {


    @JsonProperty("device_uid")
    var device_uid: String? = ""

    @JsonProperty("push_token")
    var push_token: String? = ""

    @JsonProperty("os_name")
    var os_name: String? = "tms_supplier_android"


}

