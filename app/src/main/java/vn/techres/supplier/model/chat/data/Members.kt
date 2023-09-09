package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class Members {
    @JsonField(name = ["number"])
    var number = 0

    @JsonField(name = ["full_name"])
    var full_name = ""

    @JsonField(name = ["role_name"])
    var role_name = ""

    @JsonField(name = ["avatar"])
    var avatar = ""

    @JsonField(name = ["normalize_name"])
    var normalize_name = ""

    @JsonField(name = ["prefix"])
    var prefix = ""

    @JsonField(name = ["member_id"])
    var member_id = 0

    @JsonField(name = ["role_id"])
    var role_id = 0

    @JsonField(name = ["tag_name"])
    var tag_name = 0

    @JsonField(name = ["branch_id"])
    var branch_id = 0

    @JsonField(name = ["status"])
    var status = 0

    @JsonField(name = ["permissions"])
    var permissions = 0

    @JsonField(name = ["number_order"])
    var number_order = 0

    @JsonField(name = ["is_notification"])
    var is_notification = 0

    @JsonField(name = ["is_join_room"])
    var is_join_room = 0

    @JsonField(name = ["is_calling"])
    var is_calling = 0


    @JsonField(name = ["app_name"])
    var app_name = ""

    constructor(full_name: String, member_id: Int) {
        this.full_name = full_name
        this.member_id = member_id
    }

    constructor(
        full_name: String,
        role_name: String,
        avatar: String,
        member_id: Int,
        role_id: Int,
        app_name: String
    ) {
        this.full_name = full_name
        this.role_name = role_name
        this.avatar = avatar
        this.member_id = member_id
        this.role_id = role_id
        this.app_name = app_name
    }


}