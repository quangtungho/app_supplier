package vn.techres.supplier.model.chat.data

import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.model.entity.CurrentUser.getCurrentUser

class RevokeMessage(var random_key: String, message_type: Int, group_id: String) {
    var member_id = getCurrentUser(SupplierApplication.context)!!.id
    var message_type = 0
    var group_id = ""
    var key_message_error = ""
    var app_name = "supplier"


    init {
        this.message_type = message_type
        this.group_id = group_id
    }

}