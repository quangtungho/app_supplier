package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataListOrder : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    constructor(
        id: Int
    ) {
        this.id = id

    }

    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("restaurant_name")
    var restaurant_name: String? = ""

    @JsonProperty("total_amount_reality")
    var total_amount_reality: BigDecimal? = BigDecimal.ZERO
    @JsonProperty("restaurant_debt_amount")
    var restaurant_debt_amount: BigDecimal? = BigDecimal.ZERO

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

    @JsonProperty("code")
    var code: String? = ""

    @JsonProperty("supplier_warehouse_session_id")
    var supplier_warehouse_session_id: Int? = 0

    @JsonProperty("supplier_employee_id")
    var supplier_employee_id: Int? = 0

    @JsonProperty("supplier_employee_name")
    var supplier_employee_name: String? = ""

    @JsonProperty("amount")
    var amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_material")
    var total_material: Int? = 0

    @JsonProperty("discount_amount")
    var discount_amount: Float? = 0f

    @JsonProperty("discount_percent")
    var discount_percent: Float? = 0f

    @JsonProperty("total_amount")
    var total_amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("payment_status")
    var payment_status: Int? = 0

    @JsonProperty("delivery_at")
    var delivery_at: String? = ""

    @JsonProperty("received_at")
    var received_at: String? = ""

    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("created_at")
    var created_at: String? = ""

    @JsonProperty("vat_amount")
    var vat_amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("vat")
    var vat: Float? = 0f

    @JsonProperty("reason")
    var reason: String? = ""

    @JsonProperty("supplier_rate_quantity")
    var supplier_rate_quantity: Float? = 0f

    @JsonProperty("supplier_avatar")
    var supplier_avatar: String? = ""

    @JsonProperty("restaurant_avatar")
    var restaurant_avatar: String? = ""

    @JsonProperty("supplier_employee_delivering_name")
    var supplier_employee_delivering_name: String? = ""

    @JsonProperty("supplier_employee_delivering_avatar")
    var supplier_employee_delivering_avatar: String? = ""

    @JsonProperty("employee_complete_id")
    var employee_complete_id: Int? = 0

    @JsonProperty("employee_complete_full_name")
    var employee_complete_full_name: String? = ""

    @JsonProperty("employee_cancel_id")
    var employee_cancel_id: Int? = 0

    @JsonProperty("supplier_employee_cancel_id")
    var supplier_employee_cancel_id: Int? = 0

    @JsonProperty("employee_cancel_full_name")
    var employee_cancel_full_name: String? = ""

    @JsonProperty("supplier_employee_cancel_full_name")
    var supplier_employee_cancel_full_name: String? = ""

    @JsonProperty("employee_created_id")
    var employee_created_id: Int? = 0

    @JsonProperty("supplier_employee_created_id")
    var supplier_employee_created_id: Int? = 0

    @JsonProperty("employee_created_full_name")
    var employee_created_full_name: String? = ""

    @JsonProperty("supplier_employee_created_full_name")
    var supplier_employee_created_full_name: String? = ""

    @JsonProperty("supplier_order_detail")
    var supplier_order_detail = ArrayList<DataListOrderDetail>()

    @JsonProperty("is_join")
    var is_join: Int? = 0

    var join_check: Boolean? = false

}