package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class DataGetVersion : Serializable {
    ////GETTER
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("id")
    var version: String = ""

    @JsonProperty("message")
    var message: String = ""

    @JsonProperty("is_require_update")
    var is_require_update: Int = 0

    @JsonProperty("download_link")
    var download_link: String = ""

}
