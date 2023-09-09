package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataRoles : Serializable {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("code")
    var code: String = ""

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("description")
    var description: String = ""

    @JsonProperty("status")
    var status: Int = 0

    @JsonProperty("created_at")
    var created_at: String = ""

    @JsonProperty("updated_at")
    var updated_at: String = ""
}