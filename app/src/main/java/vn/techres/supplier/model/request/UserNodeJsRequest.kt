package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class UserNodeJsRequest {
    @JsonField(name = ["type_user"])
    var type_user: Int = 0

    @JsonField(name = ["username"])
    var username: String? = ""

    @JsonField(name = ["supplier_id"])
    var supplier_id: Int = 0

    @JsonField(name = ["full_name"])
    var full_name: String = ""

    @JsonField(name = ["password"])
    var password: String? = ""

    @JsonField(name = ["os_name"])
    var os_name: String? = ""

    @JsonField(name = ["phone"])
    var phone: String? = ""

    @JsonField(name = ["avatar"])
    var avatar: String? = ""

    @JsonField(name = ["gender"])
    var gender: Int = 0

    @JsonField(name = ["restaurant_id"])
    var restaurant_id: Int = 0

    @JsonField(name = ["branch_id"])
    var branch_id: Int = 0

    @JsonField(name = ["employee_role_name"])
    var employee_role_name: String = ""

    @JsonField(name = ["device_name"])
    var device_name: String = ""

    @JsonField(name = ["device_uid"])
    var device_uid: String = ""

    @JsonField(name = ["role_name"])
    var role_name: String = ""

    @JsonField(name = ["role_id"])
    var role_id: Int = 0

    @JsonField(name = ["user_id"])
    var user_id: Int = 0

}

