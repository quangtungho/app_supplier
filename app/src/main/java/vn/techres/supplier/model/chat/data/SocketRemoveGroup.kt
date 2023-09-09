package vn.techres.supplier.model.chat.data

class SocketRemoveGroup(group_id: String) {
    var member_id = 0
    var group_id = ""

    init {
        this.group_id = group_id
    }
}