package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class DataListOrderDetail : Serializable {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("supplier_material_id")
    var supplier_material_id: Int = 0

    @JsonProperty("supplier_material_name")
    var supplier_material_name: String = ""

    @JsonProperty("supplier_unit_name")
    var supplier_unit_name: String = ""

    @JsonProperty("supplier_order_id")
    var supplier_order_id: Int = 0

    @JsonProperty("quantity")
    var quantity: Float = 0f

}
