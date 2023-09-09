package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class CreateUnitsRequest {
    @JsonField(name = ["name"])
    var name: String? = null

    @JsonField(name = ["description"])
    var description: String = ""

    @JsonField(name = ["unit_specifications"])
    var unit_specifications: ArrayList<Int>? = null
}

