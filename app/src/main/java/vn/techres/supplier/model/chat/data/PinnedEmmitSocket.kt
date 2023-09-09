package vn.techres.supplier.model.chat.data

import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.model.entity.CurrentUser

class PinnedEmmitSocket(
    var random_key: String,
    var group_id: String,
    var message_type: Int,
     var code: String,
) {
    var member_id: Int = CurrentUser.getCurrentUser(SupplierApplication.context)!!.id
    var key_message_error = ""
    var app_name = "supplier"
}