package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataOrderInDay : Serializable {
    @JsonProperty("supplier_id")
    var supplier_id: Int = 0

    @JsonProperty("total_order")
    var total_order: Int = 0

    @JsonProperty("order_delivered")
    var order_delivered: Int = 0

    @JsonProperty("order_not_delivered")
    var order_not_delivered: Int = 0

    @JsonProperty("return_order")
    var return_order: Int = 0

    @JsonProperty("order_processing")
    var order_processing: Int = 0

    @JsonProperty("waiting_confirm")
    var waiting_confirm: Int = 0

    @JsonProperty("total_amount_paid")
    var total_amount_paid: BigDecimal = BigDecimal.ZERO

    @JsonProperty("debt")
    var debt: Float = 0f

    @JsonProperty("total_order_amount")
    var total_order_amount: BigDecimal = BigDecimal.ZERO

    @JsonProperty("total_all_order_amount")
    var total_all_order_amount: BigDecimal = BigDecimal.ZERO


}
