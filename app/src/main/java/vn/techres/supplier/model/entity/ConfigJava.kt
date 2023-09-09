package vn.techres.supplier.model.entity

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject


@JsonObject
class ConfigJava {
    @JsonField(name = ["type"])
    var type: String? = ""

    @JsonField(name = ["version"])
    var version: String? = ""

    @JsonField(name = ["domain"])
    var domain: String? = ""

    @JsonField(name = ["api_key"])
    var api_key: String? = ""

    @JsonField(name = ["api_domain"])
    var api_domain: String? = ""

    @JsonField(name = ["chat_domain"])
    var chat_domain: String? = ""

    @JsonField(name = ["realtime_domain"])
    var realtime_domain: String? = ""

    @JsonField(name = ["ads_domain"])
    var ads_domain: String? = ""
}
