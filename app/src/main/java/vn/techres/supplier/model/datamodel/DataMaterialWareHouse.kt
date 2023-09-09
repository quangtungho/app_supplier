package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class DataMaterialWareHouse {

    @JsonField(name = ["note"])
    var note: String = ""

    @JsonField(name = ["supplier_warehouse_session_detail_id"])
    var supplier_warehouse_session_detail_id: Int = 0

    @JsonField(name = ["supplier_material_id"])
    var supplier_material_id: Int = 0

    @JsonField(name = ["quantity"])
    var quantity: Double = 0.0

    @JsonField(name = ["supplier_input_quantity"])
    var supplier_input_quantity: Double = 0.0

    @JsonField(name = ["supplier_input_unit_type"])
    var supplier_input_unit_type: Int = 0


    @JsonField(name = ["supplier_input_price"])
    var supplier_input_price: Float = 0f


    @JsonField(name = ["status"])
    var status: Int = 0
}