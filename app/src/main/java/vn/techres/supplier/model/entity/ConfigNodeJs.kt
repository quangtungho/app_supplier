package vn.techres.supplier.model.entity

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject


@JsonObject
class ConfigNodeJs {
    @JsonField(name = ["api_key"])
    var api_key: String? = ""

    @JsonField(name = ["_id"])
    var _id: String? = ""

    @JsonField(name = ["api_chat_supplier"])
    var api_chat_supplier: String? = "https://api.chat.tms.techres.vn"

    @JsonField(name = ["api_ads"])
    var api_ads: String? = "https://api.upload.techres.vn"
}
