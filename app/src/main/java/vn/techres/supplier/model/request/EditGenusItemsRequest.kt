package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class EditGenusItemsRequest {
    @JsonField(name = ["name"])
    var name: String? = ""

    @JsonField(name = ["description"])
    var description: String? = ""


}

