package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataBillOrderMaterial : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

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

    @JsonProperty("restaurant_material_id")
    var restaurant_material_id: Int? = 0

    @JsonProperty("supplier_material_category_id")
    var supplier_material_category_id: Int? = 0

    @JsonProperty("supplier_material_category_name")
    var supplier_material_category_name: String? = ""

    @JsonProperty("supplier_material_id")
    var supplier_material_id: Int? = 0

    @JsonProperty("supplier_order_request_id")
    var supplier_order_request_id: Int? = 0

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

    @JsonProperty("quantity")
    var quantity: Float = 0f

    @JsonProperty("out_of_stock")
    var out_of_stock: Int? = 0


    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("restaurant_name")
    var restaurant_name: String? = ""

    @JsonProperty("restaurant_brand_name")
    var restaurant_brand_name: String? = ""

    @JsonProperty("branch_name")
    var branch_name: String? = ""

    @JsonProperty("supplier_name")
    var supplier_name: String? = ""

    @JsonProperty("created_at")
    var created_at: String? = ""

    @JsonProperty("expected_delivery_time")
    var expected_delivery_time: String? = ""

    @JsonProperty("retail_price")
    var retail_price: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_amount")
    var total_amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_quantity_before")
    var total_quantity_before: Double? = 0.0

    @JsonProperty("request_quantity")
    var request_quantity: Double? = 0.0

    @JsonProperty("restaurant_material_order_request_id")
    var restaurant_material_order_request_id: Int? = 0

    @JsonProperty("total_quantity")
    var total_quantity: Double? = 0.0

    @JsonProperty("accept_quantity")
    var accept_quantity: Double? = 0.0

    @JsonProperty("return_quantity")
    var return_quantity: Double? = 0.0

    @JsonProperty("")
    var price: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_price")
    var total_price: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("quantity_input")
    var quantity_input: Float? = 0f

    @JsonProperty("price_reality_input")
    var price_reality_input: Float? = 0f

    @JsonProperty("supplier_quantity")
    var supplier_quantity: Float? = 0f

    @JsonProperty("supplier_total_amount")
    var supplier_total_amount: Float? = 0f

    @JsonProperty("is_supplier_update")
    var is_supplier_update: Int? = 0

    @JsonProperty("suppliers")
    var suppliers = ArrayList<DataListSuppliers>()

}
