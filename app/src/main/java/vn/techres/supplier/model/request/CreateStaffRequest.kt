package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class CreateStaffRequest {
    @JsonField(name = ["name"])
    var name: String? = ""

    @JsonField(name = ["address"])
    var address: String? = ""

    @JsonField(name = ["email"])
    var email: String? = ""

    @JsonField(name = ["phone"])
    var phone: String? = ""

    @JsonField(name = ["avatar"])
    var avatar: String? = ""

    @JsonField(name = ["supplier_role_id"])
    var supplier_role_id: Int? = 0
}