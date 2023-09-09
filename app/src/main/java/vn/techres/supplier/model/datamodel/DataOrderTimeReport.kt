package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataOrderTimeReport : Serializable {
    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("+")
    var name: String? = ""

    @JsonProperty("total_order")
    var total_order: Int? = 0

    @JsonProperty("total_delivery_completed")
    var total_delivery_completed: Int? = 0

    @JsonProperty("total_unfinished_delivery")
    var total_unfinished_delivery: Int? = 0

    @JsonProperty("total_amount")
    var total_amount: Float? = 0f
}