package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class MessageNotSeen : Serializable {
    @JsonField(name = ["message_not_seen_order"])
    var message_not_seen_order = 0
    @JsonField(name = ["message_not_seen_all"])
    var message_not_seen_all = 0

}