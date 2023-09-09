package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataChangeEmployee : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("name")
    var name: String? = ""

    @JsonProperty("prefix")
    var prefix: String? = ""

    @JsonProperty("email")
    var email: String? = ""

    @JsonProperty("phone")
    var phone: String? = ""

    @JsonProperty("address")
    var address: String? = ""
    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("avatar")
    var avatar: String? = ""

    @JsonProperty("supplier_id")
    var supplier_id: Int? = 0

    @JsonProperty("normalize_name")
    var normalize_name: String? = ""
    }