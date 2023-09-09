package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataListDetail : Serializable {
    @JsonField(name = ["id"])
    var id: Int = 0

    @JsonField(name = ["supplier_warehouse_session_id"])
    var supplier_warehouse_session_id: Int = 0

    @JsonField(name = ["supplier_id"])
    var supplier_id: Int = 0

    @JsonField(name = ["material_category_id"])
    var material_category_id: Int = 0

    @JsonField(name = ["material_category_name"])
    var material_category_name: String = ""

    @JsonField(name = ["supplier_material_id"])
    var supplier_material_id: Int = 0

    @JsonField(name = ["supplier_material_name"])
    var supplier_material_name: String = ""

    @JsonField(name = ["supplier_material_code"])
    var supplier_material_code: String = ""

    @JsonField(name = ["unit_name"])
    var unit_name: String = ""

    @JsonField(name = ["unit_full_name"])
    var unit_full_name: String = ""

    @JsonField(name = ["unit_specification_exchange_value"])
    var unit_specification_exchange_value: Int = 0

    @JsonField(name = ["unit_specification_exchange_name"])
    var unit_specification_exchange_name: String = ""

    @JsonField(name = ["unit_price"])
    var unit_price: BigDecimal = BigDecimal.ZERO

    @JsonField(name = ["quantity"])
    var quantity: Float = 0f

    @JsonField(name = ["supplier_input_quantity"])
    var supplier_input_quantity: Float = 0f

    @JsonField(name = ["supplier_input_unit_type"])
    var supplier_input_unit_type: Int = 0

    @JsonField(name = ["supplier_input_unit_name"])
    var supplier_input_unit_name: String = ""

    @JsonField(name = ["supplier_input_price"])
    var supplier_input_price: BigDecimal = BigDecimal.ZERO

    @JsonField(name = ["total_price"])
    var total_price: BigDecimal = BigDecimal.ZERO

    @JsonField(name = ["delivery_date"])
    var delivery_date: String = ""

    @JsonField(name = ["type"])
    var type: Int = 0

    @JsonField(name = ["note"])
    var note: String = ""

    @JsonField(name = ["supplier_warehouse_session_status"])
    var supplier_warehouse_session_status: Int = 0

    @JsonField(name = ["sort"])
    var sort: String = ""

    @JsonField(name = ["is_deleted"])
    var is_deleted: Int = 0
}