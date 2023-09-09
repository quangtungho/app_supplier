package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataListMaterial : Serializable {
    @JsonProperty("supplier_material_id")
    var supplier_material_id: Int = 0

    @JsonProperty("quantity")
    var quantity: Float = 0f

    @JsonProperty("price_reality")
    var price_reality: Float = 0f

    constructor(supplier_material_id: Int, quantity: Float, price_reality: Float) {
        this.supplier_material_id = supplier_material_id
        this.quantity = quantity
        this.price_reality = price_reality
    }
}
