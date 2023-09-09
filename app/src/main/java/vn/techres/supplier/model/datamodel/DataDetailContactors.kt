package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataDetailContactors : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int? = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("branch_id")
    var branch_id: Int? = 0

    @JsonProperty("employee_id")
    var employee_id: Int? = 0

    @JsonProperty("employee_name")
    var employee_name: String? = ""

    @JsonProperty("phone")
    var phone: String? = ""

    @JsonProperty("address")
    var address: String? = ""

    @JsonProperty("email")
    var email: String? = ""

    @JsonProperty("avatar")
    var avatar: String? = ""

    @JsonProperty("is_main_contact")
    var is_main_contact: Int? = 0

    @JsonProperty("created_at")
    var created_at: String? = ""

    @JsonProperty("updated_at")
    var updated_at: String? = ""

}