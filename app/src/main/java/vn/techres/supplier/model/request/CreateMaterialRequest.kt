package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class CreateMaterialRequest {
    @JsonField(name = ["code"])
    var code: String? = ""

    @JsonField(name = ["name"])
    var name: String? = ""

    @JsonField(name = ["material_category_id"])
    var material_category_id: Int? = 0

    @JsonField(name = ["cost_price"])
    var cost_price: Float = 0f

    @JsonField(name = ["wholesale_price"])
    var wholesale_price: Float = 0f

    @JsonField(name = ["retail_price"])
    var retail_price: Float = 0f

    @JsonField(name = ["wholesale_price_quantity"])
    var wholesale_price_quantity: Float? = 0f

    @JsonField(name = ["out_stock_alert_quantity"])
    var out_stock_alert_quantity: Float? = 0f

    @JsonField(name = ["material_unit_id"])
    var material_unit_id: Int? = 0

    @JsonField(name = ["material_unit_specification_id"])
    var material_unit_specification_id: Int? = 0

    @JsonField(name = ["wastage_rate"])
    var wastage_rate: Float? = 0f

    @JsonField(name = ["status"])
    var status: Int? = 0
}