package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataListMatialOrder : Serializable {
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

    @JsonProperty("total_quantity")
    var total_quantity: Float? = 0f

    @JsonProperty("accept_quantity")
    var accept_quantity: Float? = 0f

    @JsonProperty("return_quantity")
    var return_quantity: Float? = 0f

    @JsonProperty("price")
    var price: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_price")
    var total_price: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("out_of_stock")
    var out_of_stock: Int? = 0

    @JsonProperty("created_at")
    var created_at: String? = ""

    @JsonProperty("request_quantity")
    var request_quantity: Float? = 0f

    @JsonProperty("total_quantity_before")
    var total_quantity_before: Float? = 0f

    @JsonProperty("total_amount")
    var total_amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("restaurant_material_order_request_id")
    var restaurant_material_order_request_id: Int? = 0

    @JsonProperty("supplier_order_detail_id")
    var supplier_order_detail_id: BigDecimal = BigDecimal.ZERO

    @JsonProperty("quantity_input")
    var quantity_input: Float? = 0f

    @JsonProperty("price_reality_input")
    var price_reality_input: BigDecimal = BigDecimal.ZERO

    @JsonProperty("response_quantity")
    var response_quantity: Float? = 0f

    @JsonProperty("supplier_rate_quantity")
    var supplier_rate_quantity: Float? = 0f

    @JsonProperty("price_reality")
    var price_reality: BigDecimal = BigDecimal.ZERO

    @JsonProperty("total_price_reality")
    var total_price_reality: BigDecimal = BigDecimal.ZERO

    @JsonProperty("number_count_on_supplier")
    var number_count_on_supplier: Int = 0


}