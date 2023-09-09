package vn.techres.supplier.model.chat.data

import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.model.entity.CurrentUser.getCurrentUser

class SocketReactionMessage{
    var last_reactions_id = getCurrentUser(SupplierApplication.context)!!.id
    var last_reactions = 0
    var random_key = ""
    var member_id = getCurrentUser(SupplierApplication.context)!!.id
    var group_id = ""
    var key_message_error = Math.random().toString()
    var app_name = "supplier"
    var code =""

    constructor(last_reactions: Int, random_key: String, group_id: String) {
        this.last_reactions = last_reactions
        this.random_key = random_key
        this.group_id = group_id
    }

    constructor(last_reactions: Int, random_key: String, group_id: String, code: String) {
        this.last_reactions = last_reactions
        this.random_key = random_key
        this.group_id = group_id
        this.code = code
    }


}