package vn.techres.supplier.model.datamodel
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class   DataCategory : Serializable {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("code")
    var code: String = ""

    @JsonProperty("description")
    var description: String = ""

    @JsonProperty("material_category_type_parent_id")
    var material_category_type_parent_id: Int = 0

    @JsonProperty("material_category_type_parent_name")
    var material_category_type_parent_name: String = ""

    @JsonProperty("status")
    var status: Int = 0
}
