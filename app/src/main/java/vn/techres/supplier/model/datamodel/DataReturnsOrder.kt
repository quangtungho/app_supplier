package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable
import java.math.BigDecimal


@JsonObject
class DataReturnsOrder : Serializable {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("code")
    var code: String = ""

    @JsonProperty("amount")
    var amount: BigDecimal = BigDecimal.ZERO

    @JsonProperty("vat")
    var vat: Float = 0f

    @JsonProperty("status")
    var status: Int = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int = 0

    @JsonProperty("restaurant_name")
    var restaurant_name: String = ""

    @JsonProperty("restaurant_brand_id")
    var restaurant_brand_id: Int = 0

    @JsonProperty("restaurant_brand_name")
    var restaurant_brand_name: String = ""

    @JsonProperty("branch_id")
    var branch_id: Int = 0

    @JsonProperty("branch_name")
    var branch_name: String = ""

    @JsonProperty("supplier_id")
    var supplier_id: Int = 0

    @JsonProperty("supplier_name")
    var supplier_name: String = ""

    @JsonProperty("supplier_order_id")
    var supplier_order_id: Int = 0

    @JsonProperty("total_material")
    var total_material: Int = 0

    @JsonProperty("discount_amount")
    var discount_amount: Float = 0f

    @JsonProperty("discount_percent")
    var discount_percent: Float = 0f

    @JsonProperty("vat_amount")
    var vat_amount: Float = 0f

    @JsonProperty("total_amount")
    var total_amount: BigDecimal = BigDecimal.ZERO

    @JsonProperty("employee_created_id")
    var employee_created_id: Int = 0

    @JsonProperty("employee_created_full_name")
    var employee_created_full_name: String = ""

    @JsonProperty("supplier_employee_confirm_id")
    var supplier_employee_confirm_id: Int = 0

    @JsonProperty("supplier_employee_confirm_full_name")
    var supplier_employee_confirm_full_name: String = ""

    @JsonProperty("created_at")
    var created_at: String = ""

    @JsonProperty("updated_at")
    var updated_at: String = ""

    @JsonProperty("supplier_warehouse_session_status")
    var supplier_warehouse_session_status: Int = 0

    @JsonProperty("supplier_warehouse_session_id")
    var supplier_warehouse_session_id: Int = 0

    @JsonProperty("supplier_material_return_request_details")
    var supplier_material_return_request_details = ArrayList<DataBillOrderMaterialReturn>()
}
