package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataOrderReport : Serializable {
    @JsonProperty("total_order")
    var total_order: Int? = 0

    @JsonProperty("total_amount")
    var total_amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_vat")
    var total_vat: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_discount")
    var total_discount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("report")
    var report = ArrayList<DataReportOrder>()


}