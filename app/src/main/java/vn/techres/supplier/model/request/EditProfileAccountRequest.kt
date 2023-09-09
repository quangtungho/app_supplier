package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class EditProfileAccountRequest {
    @JsonField(name = ["email"])
    var email: String? = null

    @JsonField(name = ["phone"])
    var phone: String? = null

    @JsonField(name = ["name"])
    var name: String? = null

    @JsonField(name = ["address"])
    var address: String? = null

    @JsonField(name = ["avatar"])
    var avatar: String? = null

    @JsonField(name = ["supplier_role_id"])
    var supplier_role_id: Int? = null

}

