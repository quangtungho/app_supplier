package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable
import java.math.BigDecimal


@JsonObject
class RevenueDetail : Serializable {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("supplier_material_name")
    var supplier_material_name: String = ""

    @JsonProperty("total_price_material")
    var total_price_material: BigDecimal = BigDecimal.ZERO

}
