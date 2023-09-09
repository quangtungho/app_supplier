package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable
import java.math.BigDecimal


@JsonObject
class DetailPayment : Serializable {
    ////GETTER
    @JsonProperty("supplier_order_id")
    var supplier_order_id: Int = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int = 0

    @JsonProperty("restaurant_brand_id")
    var restaurant_brand_id: Int = 0

    @JsonProperty("branch_id")
    var branch_id: Int = 0

    @JsonProperty("code")
    var code: String = ""

    @JsonProperty("total_material")
    var total_material: String = ""

    @JsonProperty("discount_amount")
    var discount_amount: BigDecimal = BigDecimal.ONE

    @JsonProperty("vat_amount")
    var vat_amount: BigDecimal = BigDecimal.ONE

    @JsonProperty("restaurant_debt_amount")
    var restaurant_debt_amount: BigDecimal = BigDecimal.ONE


}
