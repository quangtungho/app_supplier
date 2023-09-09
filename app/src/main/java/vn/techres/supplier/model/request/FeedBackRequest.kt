package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class FeedBackRequest {
    @JsonField(name = ["name"])
    var name: String? = ""

    @JsonField(name = ["email"])
    var email: String? = ""

    @JsonField(name = ["phone"])
    var phone: String? = ""

    @JsonField(name = ["content"])
    var content: String? = ""

    @JsonField(name = ["type"])
    var type: Int? = 0

    @JsonField(name = ["project_id"])
    var project_id: String? = ""


}