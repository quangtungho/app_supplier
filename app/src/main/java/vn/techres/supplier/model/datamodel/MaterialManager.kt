package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class MaterialManager : Serializable {
    @JsonField(name = ["id"])
    var id: Int = 0

    @JsonField(name = ["supplier_material_id"])
    var supplier_material_id: Int = 0

    @JsonField(name = ["name"])
    var name: String = ""

    @JsonField(name = ["code"])
    var code: String = ""

    @JsonField(name = ["prefix"])
    var prefix: String = ""

    @JsonField(name = ["description"])
    var description: String = ""

    @JsonField(name = ["normalize_name"])
    var normalize_name: String = ""

    @JsonField(name = ["avatar"])
    var avatar: String = ""


    @JsonField(name = ["avatar_thumb"])
    var avatar_thumb: String = ""

    @JsonField(name = ["status"])
    var status: Int = 0

    @JsonField(name = ["supplier_id"])
    var supplier_id: Int = 0

    @JsonField(name = ["restaurant_material_copy_id"])
    var restaurant_material_copy_id: Int = 0

    @JsonField(name = ["material_category_id"])
    var material_category_id: Int = 0

    @JsonField(name = ["material_unit_id"])
    var material_unit_id: Int = 0

    @JsonField(name = ["material_unit_specification_id"])
    var material_unit_specification_id: Int = 0

    @JsonField(name = ["cost_price"])
    var cost_price: Double = 0.0

    @JsonField(name = ["wholesa.le_price"])
    var wholesale_price: Double = 0.0

    @JsonField(name = ["retail_price"])
    var retail_price: Double = 0.0

    @JsonField(name = ["wholesale_price_quantity"])
    var wholesale_price_quantity: Float = 0f

    @JsonField(name = ["out_stock_alert_quantity"])
    var out_stock_alert_quantity: Float = 0f

    @JsonField(name = ["material_category_name"])
    var material_category_name: String = ""

    @JsonField(name = ["material_unit_name"])
    var material_unit_name: String = ""

    @JsonField(name = ["material_unit_full_name"])
    var material_unit_full_name: String = ""

    @JsonField(name = ["material_unit_specification_name"])
    var material_unit_specification_name: String = ""

    @JsonField(name = ["material_unit_specification_exchange_name"])
    var material_unit_specification_exchange_name: String = ""

    @JsonField(name = ["material_unit_specification_exchange_value"])
    var material_unit_specification_exchange_value: Int = 0

    @JsonField(name = ["remain_quantity"])
    var remain_quantity: Double = 0.0

    @JsonField(name = ["quantity"])
    var quantity: Double = 1.0

    @JsonField(name = ["note"])
    var note: String = ""

    @JsonField(name = ["created_at"])
    var created_at: String = ""

    @JsonField(name = ["updated_at"])
    var updated_at: String = ""


    //get materia selected
    @JsonField(name = ["supplier_warehouse_session_id"])
    var supplier_warehouse_session_id: Int = 0

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
    var unit_price: Double = 0.0

    @JsonField(name = ["supplier_input_quantity"])
    var supplier_input_quantity: Double = 0.0

    @JsonField(name = ["supplier_input_unit_type"])
    var supplier_input_unit_type: Int = 0

    @JsonField(name = ["supplier_input_unit_name"])
    var supplier_input_unit_name: String = ""

    @JsonField(name = ["supplier_input_price"])
    var supplier_input_price: Double = 0.0

    @JsonField(name = ["total_price"])
    var total_price: Double = 0.0

    @JsonField(name = ["price"])
    var price: Double = 0.0

    @JsonField(name = ["delivery_date"])
    var delivery_date: String = ""

    @JsonField(name = ["type"])
    var type: Int = 0

    @JsonField(name = ["supplier_warehouse_session_status"])
    var supplier_warehouse_session_status: Int = 0

    @JsonField(name = ["sort"])
    var sort: String = ""

    @JsonField(name = ["is_deleted"])
    var is_deleted: Int = 0

    @JsonField(name = ["wastage_rate"])
    var wastage_rate: Float = 0f
}