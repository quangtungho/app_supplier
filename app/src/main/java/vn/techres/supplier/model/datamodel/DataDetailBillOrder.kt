package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataDetailBillOrder : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("restaurant_name")
    var restaurant_name: String? = ""

    @JsonProperty("restaurant_brand_id")
    var restaurant_brand_id: Int? = 0

    @JsonProperty("restaurant_brand_name")
    var restaurant_brand_name: String? = ""

    @JsonProperty("branch_id")
    var branch_id: Int? = 0

    @JsonProperty("branch_name")
    var branch_name: String? = ""

    @JsonProperty("supplier_id")
    var supplier_id: Int? = 0

    @JsonProperty("supplier_name")
    var supplier_name: String? = ""

    @JsonProperty("expected_delivery_time")
    var expected_delivery_time: String? = ""

    @JsonProperty("quantity")
    var quantity: Int? = 0

    @JsonProperty("total_amount")
    var total_amount: Double? = 0.0

    @JsonProperty("employee_created_id")
    var employee_created_id: Int? = 0

    @JsonProperty("employee_created_full_name")
    var employee_created_full_name: String? = ""

    @JsonProperty("restaurant_material_order_request_id")
    var restaurant_material_order_request_id: Int? = 0

    @JsonProperty("created_at")
    var created_at: String? = ""

    @JsonProperty("quantity_input")
    var quantity_input: Float? = 0f

    @JsonProperty("supplier_quantity")
    var supplier_quantity: Float? = 0f

    @JsonProperty("supplier_total_amount")
    var supplier_total_amount: Float? = 0f

    @JsonProperty("is_supplier_update")
    var is_supplier_update: Int? = 0

    @JsonProperty("supplier_vat")
    var supplier_vat: Float? = 0f

    @JsonProperty("supplier_discount_percent")
    var supplier_discount_percent: Float? = 0f

    @JsonProperty("reason")
    var reason: String? = " "

    @JsonProperty("code")
    var code: String? = " "

    @JsonProperty("total_amount_reality")
    var total_amount_reality: BigDecimal? = BigDecimal.ZERO

}