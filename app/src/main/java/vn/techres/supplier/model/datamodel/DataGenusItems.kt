package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class DataGenusItems : Serializable {
    ////GETTER
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int = 0

    @JsonProperty("supplier_addition_fee_reason_category_id")
    var supplier_addition_fee_reason_category_id: Int = 0

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("description")
    var description: String = ""

    @JsonProperty("supplier_addition_fee_type")
    var supplier_addition_fee_type: Int = 0

    @JsonProperty("is_hidden")
    var is_hidden: Int = 0

    @JsonProperty("created_at")
    var created_at: String = ""

    @JsonProperty("updated_at")
    var updated_at: String = ""

}
