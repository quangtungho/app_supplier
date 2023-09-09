package vn.techres.supplier.model.datamodel
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class   DataSpecification : Serializable {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("status")
    var status: Int = 0

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("supplier_id")
    var supplier_id: Int = 0

    @JsonProperty("exchange_value")
    var exchange_value: Int = 0

    @JsonProperty("material_unit_specification_exchange_name_id")
    var material_unit_specification_exchange_name_id: Int = 0

    @JsonProperty("material_unit_specification_exchange_name")
    var material_unit_specification_exchange_name: String = ""

    @JsonProperty("created_at")
    var created_at: String = ""

    @JsonProperty("updated_at")
    var updated_at: String = ""
}
