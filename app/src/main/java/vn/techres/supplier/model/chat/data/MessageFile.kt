package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class MessageFile {
    @JsonField(name = ["total_record"])
    var total_record = 0

    @JsonField(name = ["list"])
    var list = ArrayList<ListMessageFile>()

}