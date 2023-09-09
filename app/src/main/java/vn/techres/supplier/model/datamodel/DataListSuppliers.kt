package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable
import java.math.BigDecimal


@JsonObject
class DataListSuppliers : Serializable {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("prefix")
    var prefix: String = ""

    @JsonProperty("email")
    var email: String = ""

    @JsonProperty("phone")
    var phone: String = ""

    @JsonProperty("address")
    var address: String = ""

    @JsonProperty("restaurant_material_id")
    var restaurant_material_id: Int = 0

    @JsonProperty("supplier_material_id")
    var supplier_material_id: Int = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int = 0

    @JsonProperty("normalize_name")
    var normalize_name: String = ""

    @JsonProperty("retail_price")
    var retail_price: BigDecimal = BigDecimal.ZERO

    @JsonProperty("cost_price")
    var cost_price: BigDecimal = BigDecimal.ZERO

    @JsonProperty("wholesale_price")
    var wholesale_price: BigDecimal = BigDecimal.ZERO

    @JsonProperty("wholesale_price_quantity")
    var wholesale_price_quantity: Float = 0f

}
