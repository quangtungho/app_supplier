package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable
import java.math.BigDecimal


@JsonObject
class RevenueWeek : Serializable {
    ////GETTER
    @JsonProperty("time")
    var time: Int = 0

    @JsonProperty("supplier_revenue")
    var supplier_revenue: BigDecimal = BigDecimal.ZERO

    @JsonProperty("supplier_cost")
    var supplier_cost: BigDecimal = BigDecimal.ZERO

    @JsonProperty("supplier_profit")
    var supplier_profit: BigDecimal = BigDecimal.ZERO
}
