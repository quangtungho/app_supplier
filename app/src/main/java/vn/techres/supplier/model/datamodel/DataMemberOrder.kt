package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataMemberOrder : Serializable {
    @JsonProperty("member_id")
    var member_id: Int? = 0

    @JsonProperty("full_name")
    var full_name: String? = ""

    @JsonProperty("avatar")
    var avatar: String? = ""

    @JsonProperty("role_name")
    var role_name: String? = ""

    @JsonProperty("role_id")
    var role_id: Int? = 0

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

    @JsonProperty("is_calling")
    var is_calling: Int? = 0

    @JsonProperty("number_order")
    var number_order: Int = 0

    @JsonProperty("app_name")
    var app_name: String? = ""

    @JsonProperty("restaurant_id")
    var restaurant_id: Int = 0

}