package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

@JsonObject
class EmployeeGroup {
    @JsonProperty("avatar")
    var avatar: String = ""

    @JsonProperty("full_name")
    var full_name: String = ""

    @JsonProperty("member_id")
    var member_id: Int = 0

    @JsonProperty("role_id")
    var role_id: Int = 0

    @JsonProperty("role_name")
    var role_name: String = ""

    @JsonProperty("app_name")
    var app_name: String = "supplier"

    constructor(
        avatar: String,
        full_name: String,
        member_id: Int,
        role_id: Int,
        role_name: String
    ) {
        this.avatar = avatar
        this.full_name = full_name
        this.member_id = member_id
        this.role_id = role_id
        this.role_name = role_name
    }
}