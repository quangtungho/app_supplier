package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable
import java.math.BigDecimal


@JsonObject
class PaymentRequest : Serializable {
    ////GETTER
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("status")
    var status: Int = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int = 0

    @JsonProperty("restaurant_brand_id")
    var restaurant_brand_id: Int = 0

    @JsonProperty("branch_id")
    var branch_id: Int = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int = 0

    @JsonProperty("restaurant_name")
    var restaurant_name: String = ""

    @JsonProperty("restaurant_brand_name")
    var restaurant_brand_name: String = ""

    @JsonProperty("branch_name")
    var branch_name: String = ""

    @JsonProperty("supplier_order_ids")
    var supplier_order_ids = ArrayList<Int>()

    @JsonProperty("total_amount")
    var total_amount: BigDecimal = BigDecimal.ONE

    @JsonProperty("from_date")
    var from_date: String = ""

    @JsonProperty("to_date")
    var to_date: String = ""

    @JsonProperty("is_deleted")
    var is_deleted: Int = 0

    @JsonProperty("created_at")
    var created_at: String = ""

    @JsonProperty("updated_at")
    var updated_at: String = ""

    @JsonProperty("supplier_orders")
    var supplier_orders = ArrayList<DetailPayment>()

}
