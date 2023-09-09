package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class DataUnitSpecification : Serializable {

    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int = 0

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("exchange_value")
    var exchange_value: Int = 0

    @JsonProperty("material_unit_specification_exchange_name_id")
    var material_unit_specification_exchange_name_id: Int = 0

    @JsonProperty("material_unit_specification_exchange_name")
    var material_unit_specification_exchange_name: String = ""
}
