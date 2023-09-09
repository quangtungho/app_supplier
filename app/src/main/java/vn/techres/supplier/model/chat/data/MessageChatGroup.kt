package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class MessageChatGroup {
    @JsonField(name = ["total_record"])
    var total_record = 0

    @JsonField(name = ["list"])
    var list: List<MessagesByGroup>? = null

    companion object {
        const val LIMIT_PAGE_MESSAGE = 50
    }
}