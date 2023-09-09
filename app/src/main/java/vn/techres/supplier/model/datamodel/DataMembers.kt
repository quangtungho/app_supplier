package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataMembers : Serializable {
    @JsonProperty("member_id")
    var member_id: Int? = 0

    @JsonProperty("full_name")
    var full_name: String? = ""

    @JsonProperty("avatar")
    var avatar: String? = ""

    @JsonProperty("number")
    var number: Int? = 0

    @JsonProperty("tag_name")
    var tag_name: Int? = 0

    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("is_notification")
    var is_notification: Int? = 0

    @JsonProperty("is_join_room")
    var is_join_room: Int? = 0

    @JsonProperty("normalize_name")
    var normalize_name: String? = ""

    @JsonProperty("prefix")
    var prefix: String? = ""

    @JsonProperty("permissions")
    var permissions: Int? = 0

}
