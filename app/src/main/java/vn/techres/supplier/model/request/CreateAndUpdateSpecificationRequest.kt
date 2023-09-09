package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class CreateAndUpdateSpecificationRequest {
    @JsonField(name = ["name"])
    var name: String? = null

    @JsonField(name = ["exchange_value"])
    var exchange_value: Float = 0f

    @JsonField(name = ["material_unit_specification_exchange_name_id"])
    var material_unit_specification_exchange_name_id: Int = 0
}

