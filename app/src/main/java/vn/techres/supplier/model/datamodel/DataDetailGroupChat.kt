package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.ArrayList

@JsonObject
class DataDetailGroupChat : Serializable {
    @JsonProperty("_id")
    var _id: String? = ""

    @JsonProperty("name")
    var name: String? = ""

    @JsonProperty("admin_id")
    var admin_id: Int? = 0

    @JsonProperty("avatar")
    var avatar: String? = ""

    @JsonProperty("conversation_type")
    var conversation_type: Int? = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("normalize_name")
    var normalize_name: String? = ""

    @JsonProperty("prefix")
    var prefix: String? = ""

    @JsonProperty("number_employee")
    var number_employee: Int? = 0

    @JsonProperty("members")
    var members: ArrayList<DataMembers>? = null
}