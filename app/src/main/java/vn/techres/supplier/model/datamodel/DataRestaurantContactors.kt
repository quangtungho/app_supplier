package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataRestaurantContactors : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int? = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("restaurant_brand_id")
    var restaurant_brand_id: Int? = 0

    @JsonProperty("branch_id")
    var branch_id: Int? = 0

    @JsonProperty("restaurant_name")
    var restaurant_name: String? = ""

    @JsonProperty("branch_name")
    var branch_name: String? = ""

    @JsonProperty("full_name")
    var full_name: String? = ""

    @JsonProperty("phone")
    var phone: String? = ""

    @JsonProperty("address")
    var address: String? = ""

    @JsonProperty("email")
    var email: String? = ""

    @JsonProperty("avatar")
    var avatar: String? = ""

    @JsonProperty("is_main_contactor")
    var is_main_contactor: Int? = 0

}