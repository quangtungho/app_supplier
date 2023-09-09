package vn.techres.supplier.model.entity

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
@JsonObject
class UserNode {
    @JsonProperty("node_access_token")
    var node_access_token: String = ""

    @JsonProperty("_id")
    var _id: String = ""
}