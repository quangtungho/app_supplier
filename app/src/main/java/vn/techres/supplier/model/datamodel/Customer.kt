package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class Customer : Serializable {
    ////GETTER
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("restaurant_name")
    var restaurant_name: String = ""

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("contact_name")
    var contact_name: String = ""

    @JsonProperty("is_main_contactor")
    var is_main_contactor: Int = 0

    @JsonProperty("contactor_avatar")
    var contactor_avatar: String = ""

    @JsonProperty("email")
    var email: String = ""

    @JsonProperty("phone")
    var phone: String = ""

    @JsonProperty("info")
    var info: String = ""

    @JsonProperty("logo")
    var logo: String = ""

    @JsonProperty("address")
    var address: String = ""

    @JsonProperty("restaurant_balance")
    var restaurant_balance: Float = 0.0f

    @JsonProperty("is_done_setup")
    var is_done_setup: Int = 0

    @JsonProperty("is_public")
    var is_public: Int = 0

    @JsonProperty("status")
    var status: Int = 0

    @JsonProperty("tax_number")
    var tax_number: String = ""

    @JsonProperty("number_branches")
    var number_branches: Int = 0

}
