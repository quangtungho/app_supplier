package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataListMaterialOrderUpdate : Serializable {
    @JsonProperty("supplier_material_id")
    var supplier_material_id: Int = 0

    @JsonProperty("quantity")
    var quantity: Float = 0f

    @JsonProperty("price_reality")
    var price_reality: BigDecimal = BigDecimal.ZERO

    @JsonProperty("supplier_order_detail_id")
    var supplier_order_detail_id: Int = 0

    constructor(supplier_material_id: Int, quantity: Float, supplier_order_detail_id: Int) {
        this.supplier_material_id = supplier_material_id
        this.quantity = quantity
        this.supplier_order_detail_id = supplier_order_detail_id
    }

    constructor(
        supplier_material_id: Int,
        quantity: Float,
        price_reality: BigDecimal,
        supplier_order_detail_id: Int
    ) {
        this.supplier_material_id = supplier_material_id
        this.quantity = quantity
        this.price_reality = price_reality
        this.supplier_order_detail_id = supplier_order_detail_id
    }

}
