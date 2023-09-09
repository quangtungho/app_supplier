package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataListOrderBill : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("code")
    var code: String? = ""

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
    var total_amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("employee_created_id")
    var employee_created_id: Int? = 0

    @JsonProperty("employee_created_full_name")
    var employee_created_full_name: String? = ""

    @JsonProperty("material_category_type_parent_id")
    var material_category_type_parent_id: Int? = 0

    @JsonProperty("created_at")
    var created_at: String? = ""

    @JsonProperty("retail_price")
    var retail_price: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("supplier_quantity")
    var supplier_quantity: Int? = 0

    @JsonProperty("total_material")
    var total_material: Int? = 0

    @JsonProperty("supplier_total_amount")
    var supplier_total_amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("is_supplier_update")
    var is_supplier_update: Int = 0

    @JsonProperty("supplier_vat")
    var supplier_vat: Float = 0f

    @JsonProperty("supplier_discount_percent")
    var supplier_discount_percent: Float = 0f

    @JsonProperty("reason")
    var reason: String = ""

    @JsonProperty("supplier_employee_confirm_id")
    var supplier_employee_confirm_id: Int = 0

    @JsonProperty("supplier_employee_cancel_id")
    var supplier_employee_cancel_id: Int = 0

    @JsonProperty("employee_cancel_id")
    var employee_cancel_id: Int = 0

    @JsonProperty("supplier_employee_confirm_full_name")
    var supplier_employee_confirm_full_name: String = ""

    @JsonProperty("supplier_employee_cancel_full_name")
    var supplier_employee_cancel_full_name: String = ""

    @JsonProperty("employee_cancel_full_name")
    var employee_cancel_full_name: String = ""

    @JsonProperty("supplier_avatar")
    var supplier_avatar: String = ""

    @JsonProperty("branch_avatar")
    var branch_avatar: String = ""

    @JsonProperty("supplier_employee_delivering_name")
    var supplier_employee_delivering_name: String? = ""

    @JsonProperty("supplier_employee_delivering_avatar")
    var supplier_employee_delivering_avatar: String? = ""

    @JsonProperty("employee_complete_id")
    var employee_complete_id: Int? = 0

    @JsonProperty("employee_complete_full_name")
    var employee_complete_full_name: String? = ""


    @JsonProperty("supplier_employee_created_id")
    var supplier_employee_created_id: Int? = 0


    @JsonProperty("supplier_employee_created_full_name")
    var supplier_employee_created_full_name: String? = ""

    @JsonProperty("supplier_order_detail")
    var supplier_order_detail = ArrayList<DataListOrderDetail>()

    @JsonProperty("supplier_order_detail")
    var supplier_order_request_detail = ArrayList<DataListOrderDetail>()


}