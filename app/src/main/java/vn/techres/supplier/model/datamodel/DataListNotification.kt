package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

@JsonObject
class DataListNotification : Serializable {
    @JsonProperty("_id")
    var _id: String = ""

    @JsonProperty("employee_id")
    var employee_id: Int = 0

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("avatar")
    var avatar: String = ""

    @JsonProperty("title")
    var title: String = ""

    @JsonProperty("content")
    var content: String = ""

    @JsonProperty("value")
    var value: String = ""

    @JsonProperty("type")
    var type: Int = 0

    @JsonProperty("action_type")
    var action_type: Int = 0

    @JsonProperty("is_viewed")
    var is_viewed: Int = 0

    @JsonProperty("created_at")
    var created_at: String = ""

}
