package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataListEmployee : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int? = 0

    @JsonProperty("supplier_role_id")
    var supplier_role_id: Int? = 0

    @JsonProperty("name")
    var name: String? = ""

    @JsonProperty("email")
    var email: String? = ""

    @JsonProperty("phone")
    var phone: String? = ""

    @JsonProperty("address")
    var address: String? = ""

    @JsonProperty("avatar")
    var avatar: String? = ""

    @JsonProperty("supplier_employee_position")
    var supplier_employee_position: String? = ""

    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("name")
    var prefix: String? = ""

    @JsonProperty("normalize_name")
    var normalize_name: String? = ""

    @JsonProperty("role_name")
    var role_name: String? = ""

    @JsonProperty("created_at")
    var created_at: String? = ""

    @JsonProperty("is_join")
    var is_join: Boolean? = false

    var join_check: Boolean? = false

}