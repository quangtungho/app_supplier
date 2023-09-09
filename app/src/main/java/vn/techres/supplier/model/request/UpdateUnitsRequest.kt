package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class UpdateUnitsRequest {
    @JsonField(name = ["name"])
    var name: String? = null

    @JsonField(name = ["description"])
    var description: String = ""

    @JsonField(name = ["specificationIds"])
    var specificationIds: ArrayList<Int>? = null
}

