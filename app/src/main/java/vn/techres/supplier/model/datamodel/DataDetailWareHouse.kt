package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class DataDetailWareHouse : Serializable {
    @JsonField(name = ["supplier_warehouse_session_id"])
    var supplier_warehouse_session_id: Int = 0

    @JsonField(name = ["supplier_material_name"])
    var supplier_material_name: String = ""

    @JsonField(name = ["unit_name"])
    var unit_name: String = ""

    @JsonField(name = ["unit_full_name"])
    var unit_full_name: String = ""

    @JsonField(name = ["unit_specification_exchange_name"])
    var unit_specification_exchange_name: String = ""

    @JsonField(name = ["quantity"])
    var quantity: Float = 0f

    @JsonField(name = ["total_price"])
    var total_price: Double = 0.0
}