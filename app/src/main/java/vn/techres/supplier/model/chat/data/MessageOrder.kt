package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.chat.data.ChatTag
import vn.techres.supplier.model.chat.data.FileChat

@JsonObject
class MessageOrder {
    @JsonField(name = ["code"])
    var code = ""

    @JsonField(name = ["total"])
    var total: Float = 0f

    @JsonField(name = ["message_type"])
    var message_type = 0

    @JsonField(name = ["order_id "])
    var order_id = 0

    @JsonField(name = ["order_status "])
    var order_status = 0

    @JsonField(name = ["message"])
    var message = ""

    @JsonField(name = ["restaurant_name"])
    var restaurant_name = ""

    @JsonField(name = ["supplier_name"])
    var supplier_name = ""

    @JsonField(name = ["created_at"])
    var created_at = ""

    @JsonField(name = ["supplier_id"])
    var supplier_id = 0

    @JsonField(name = ["order_time_delivery"])
    var order_time_delivery = ""

    @JsonField(name = ["files"])
    var files = ArrayList<FileChat>()

    @JsonField(name = ["list_tag_name"])
    var list_tag_name = ArrayList<ChatTag>()

}