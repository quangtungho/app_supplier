package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataDetailOrderBillAll : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("restaurant_name")
    var restaurant_name: String? = ""

    @JsonProperty("restaurant_brand_id")
    var restaurant_brand_id: Int? = 0

    @JsonProperty("restaurant_material_order_request_id")
    var restaurant_material_order_request_id: Int? = 0

    @JsonProperty("supplier_order_request_id")
    var supplier_order_request_id: Int? = 0

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

    @JsonProperty("code")
    var code: String? = ""

    @JsonProperty("supplier_warehouse_session_id")
    var supplier_warehouse_session_id: String = ""

    @JsonProperty("supplier_employee_id")
    var supplier_employee_id: Int? = 0

    @JsonProperty("supplier_employee_name")
    var supplier_employee_name: String? = ""

    @JsonProperty("amount")
    var amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_material")
    var total_material: Float? = 0f

    @JsonProperty("discount_amount")
    var discount_amount: Float? = 0f

    @JsonProperty("discount_percent")
    var discount_percent: Float? = 0f

    @JsonProperty("total_amount")
    var total_amount: Double? = 0.0

    @JsonProperty("payment_status")
    var payment_status: Int? = 0

    @JsonProperty("delivery_at")
    var delivery_at: String? = ""

    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("created_at")
    var created_at: String? = ""

    @JsonProperty("vat_amount")
    var vat_amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("vat")
    var vat: Float = 0f

    @JsonProperty("quantity")
    var quantity: Float = 0f
    @JsonProperty("total_amount_reality")
    var total_amount_reality: BigDecimal = BigDecimal.ZERO

}