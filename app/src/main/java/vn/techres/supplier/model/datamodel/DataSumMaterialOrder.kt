package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class DataSumMaterialOrder : Serializable {
    @JsonField(name = ["id"])
    var id: Int = 0

    @JsonField(name = ["quantity"])
    var quantity: Float = 0f

    @JsonField(name = ["wastage_rate"])
    var wastage_rate: Float = 0f

    @JsonField(name = ["total_quantity"])
    var total_quantity: Float = 0f

    @JsonField(name = ["supplier_id"])
    var supplier_id: Int = 0

    @JsonField(name = ["supplier_material_id"])
    var supplier_material_id: Int = 0

    @JsonField(name = ["supplier_material_name"])
    var supplier_material_name: String = ""

    @JsonField(name = ["supplier_unit_name"])
    var supplier_unit_name: String = ""

    @JsonField(name = ["supplier_unit_full_name"])
    var supplier_unit_full_name: String = ""

}