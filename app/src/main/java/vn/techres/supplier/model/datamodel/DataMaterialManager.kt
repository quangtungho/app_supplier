package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class DataMaterialManager : Serializable {
    @JsonField(name = ["id"])
    var id: Int = 0

    @JsonField(name = ["name"])
    var name: String = ""

    @JsonField(name = ["code"])
    var code: String = ""

    @JsonField(name = ["status"])
    var status: Int = 0

    @JsonField(name = ["supplier_id"])
    var supplier_id: Int = 0

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

    @JsonField(name = ["material_unit_specification_name"])
    var material_unit_specification_name: String = ""

    @JsonField(name = ["material_unit_specification_exchange_name"])
    var material_unit_specification_exchange_name: String = ""

    @JsonField(name = ["quantity"])
    var quantity: Int = 1

    @JsonField(name = ["note"])
    var note: Int = 1

    @JsonField(name = ["typeUnitSelected"])
    var typeUnitSelected: Int = 1
}