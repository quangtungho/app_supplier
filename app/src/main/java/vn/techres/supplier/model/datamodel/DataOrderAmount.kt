package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable


@JsonObject
class DataOrderAmount : Serializable {
    ////GETTER
    @JsonProperty("this_week_total_order_amount")
    var this_week_total_order_amount: Float = 0f

    @JsonProperty("last_week_total_order_amount")
    var last_week_total_order_amount:Float = 0f

    @JsonProperty("this_month_total_order_amount")
    var this_month_total_order_amount: Float = 0f

    @JsonProperty("last_month_total_order_amount")
    var last_month_total_order_amount: Float = 0f


}
