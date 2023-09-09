package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class CreateGenusItemsRequest {
    @JsonField(name = ["name"])
    var name: String? = ""

    @JsonField(name = ["description"])
    var description: String? = ""

    @JsonField(name = ["supplier_addition_fee_type"])
    var supplier_addition_fee_type: Int? = 0


}

