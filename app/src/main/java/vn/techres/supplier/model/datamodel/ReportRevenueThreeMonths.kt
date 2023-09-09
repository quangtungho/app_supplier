package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class ReportRevenueThreeMonths : Serializable {
    ////GETTER
    @JsonProperty("report")
    var report = ArrayList<RevenueThreeMonths>()

    @JsonProperty("total_revenue")
    var total_revenue: BigDecimal = BigDecimal.ZERO

    @JsonProperty("total_cost")
    var total_cost: BigDecimal = BigDecimal.ZERO

    @JsonProperty("total_profit")
    var total_profit: BigDecimal = BigDecimal.ZERO

    @JsonProperty("profit_percent")
    var profit_percent: BigDecimal = BigDecimal.ZERO

}
