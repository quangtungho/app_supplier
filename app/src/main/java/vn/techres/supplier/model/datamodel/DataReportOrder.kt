package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataReportOrder : Serializable {
    @JsonProperty("time")
    var time: Int = 0

    @JsonProperty("date")
    var date: String = ""

    @JsonProperty("number_order")
    var number_order: Int = 0

    @JsonProperty("amount")
    var amount: Float = 0f

    @JsonProperty("vat_amount")
    var vat_amount: Float = 0f

    @JsonProperty("discount_amount")
    var discount_amount: Float = 0f
}
