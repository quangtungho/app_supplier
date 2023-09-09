package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class GroupEmit {
    @JsonField(name = ["member_id"])
    var member_id = 0

    @JsonField(name = ["group_id"])
    var group_id = ""

    @JsonField(name = ["app_name"])
    var app_name = "supplier"

    @JsonField(name = ["code"])
    var code = ""

    @JsonField(name = ["group_id_tms_supplier"])
    var group_id_tms_supplier = ""

    constructor(member_id: Int, group_id: String) {
        this.member_id = member_id
        this.group_id = group_id
    }

    constructor(member_id: Int, group_id: String, code: String) {
        this.member_id = member_id
        this.group_id = group_id
        this.code = code
    }

    constructor(member_id: Int, group_id: String, code: String, group_id_tms_supplier: String) {
        this.member_id = member_id
        this.group_id = group_id
        this.code = code
        this.group_id_tms_supplier = group_id_tms_supplier
    }


}