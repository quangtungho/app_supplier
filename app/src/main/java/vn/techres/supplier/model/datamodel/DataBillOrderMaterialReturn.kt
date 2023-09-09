package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataBillOrderMaterialReturn : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("quantity")
    var quantity: Float? = 0f

    @JsonProperty("created_at")
    var created_at: String? = ""

    @JsonProperty("updated_at")
    var updated_at: String? = ""

    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("restaurant_brand_id")
    var restaurant_brand_id: Int? = 0

    @JsonProperty("branch_id")
    var branch_id: Int? = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int? = 0

    @JsonProperty("supplier_order_id")
    var supplier_order_id: Int? = 0

    @JsonProperty("supplier_material_id")
    var supplier_material_id: Int? = 0

    @JsonProperty("supplier_material_category_id")
    var supplier_material_category_id: Int? = 0

    @JsonProperty("supplier_material_category_name")
    var supplier_material_category_name: String? = ""

    @JsonProperty("supplier_material_name")
    var supplier_material_name: String? = ""

    @JsonProperty("supplier_material_code")
    var supplier_material_code: String? = ""

    @JsonProperty("supplier_unit_name")
    var supplier_unit_name: String? = ""

    @JsonProperty("supplier_unit_full_name")
    var supplier_unit_full_name: String? = ""

    @JsonProperty("supplier_unit_specification_exchange_value")
    var supplier_unit_specification_exchange_value: Int? = 0

    @JsonProperty("supplier_unit_specification_exchange_name")
    var supplier_unit_specification_exchange_name: String? = ""

    @JsonProperty("price")
    var price: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_price")
    var total_price: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("restaurant_material_id")
    var restaurant_material_id: Int? = 0

    @JsonProperty("restaurant_material_name")
    var restaurant_material_name: String? = ""

    @JsonProperty("restaurant_material_code")
    var restaurant_material_code: String? = ""

    @JsonProperty("restaurant_material_prefix")
    var restaurant_material_prefix: String? = ""

    @JsonProperty("restaurant_material_normalize_name")
    var restaurant_material_normalize_name: String? = ""

    @JsonProperty("supplier_material_return_request_id")
    var supplier_material_return_request_id: Int? = 0

    @JsonProperty("supplier_warehouse_session_status")
    var supplier_warehouse_session_status: Int? = 0


}
