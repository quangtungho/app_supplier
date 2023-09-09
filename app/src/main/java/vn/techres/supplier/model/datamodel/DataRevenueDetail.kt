package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataRevenueDetail : Serializable {
    @JsonProperty("total_amount")
    var total_amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("revenue_detail")
    var revenue_detail = ArrayList<RevenueDetail>()
}