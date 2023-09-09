package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.math.BigDecimal

@JsonObject
class EditProductRequest {
    @JsonField(name = ["code"])
    var code: String? = null

    @JsonField(name = ["name"])
    var name: String? = null

    @JsonField(name = ["material_category_id"])
    var material_category_id: Int? = 0

    @JsonField(name = ["wholesale_price"])
    var wholesale_price: BigDecimal? = BigDecimal.ZERO

    @JsonField(name = ["cost_price"])
    var cost_price: BigDecimal? = BigDecimal.ZERO

    @JsonField(name = ["retail_price"])
    var retail_price: BigDecimal? = BigDecimal.ZERO

    @JsonField(name = ["wholesale_price_quantity"])
    var wholesale_price_quantity: Double? = 0.0

    @JsonField(name = ["out_stock_alert_quantity"])
    var out_stock_alert_quantity: Double? = 0.0

    @JsonField(name = ["material_unit_id"])
    var material_unit_id: Int? = 0

    @JsonField(name = ["material_unit_specification_id"])
    var material_unit_specification_id: Int? = 0

    @JsonField(name = ["status"])
    var status: Int? = 0

}