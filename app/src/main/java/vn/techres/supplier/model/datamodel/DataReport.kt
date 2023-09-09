package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class DataReport : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("name")
    var name: String? = ""

    @JsonProperty("phone")
    var phone: String? = ""

    @JsonProperty("address")
    var address: String? = ""

    @JsonProperty("email")
    var email: String? = ""

    @JsonProperty("info")
    var info: String? = ""

    @JsonProperty("total_order")
    var total_order: Int? = 0

    @JsonProperty("total_amount")
    var total_amount: Float? = 0f

    @JsonProperty("total_vat")
    var total_vat: Float? = 0f

    @JsonProperty("total_discount")
    var total_discount: Float? = 0f
}
