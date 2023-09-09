package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataListGroupChat : Serializable {
    @JsonProperty("_id")
    var _id: String? = ""

    @JsonProperty("name")
    var name: String? = ""

    @JsonProperty("admin_id")
    var admin_id: Int? = 0

    @JsonProperty("avatar")
    var avatar: String? = ""

    @JsonProperty("background")
    var background: String? = ""

    @JsonProperty("status_last_message")
    var status_last_message: Int? = 0

    @JsonProperty("last_message")
    var last_message: String? = ""

    @JsonProperty("user_name_last_message")
    var user_name_last_message: String? = ""

    @JsonProperty("user_last_message_id")
    var user_last_message_id: Int? = 0

    @JsonProperty("last_message_type")
    var last_message_type: Int? = 0

    @JsonProperty("conversation_type")
    var conversation_type: Int? = 0

    @JsonProperty("normalize_name")
    var normalize_name: String? = ""

    @JsonProperty("prefix")
    var prefix: String? = ""

    @JsonProperty("member")
    var member: DataMembers? = null

    @JsonProperty("created_last_message")
    var created_last_message: String? = ""
}

