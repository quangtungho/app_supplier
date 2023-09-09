package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class ListMessageFile {
    @JsonField(name = ["list"])
    var list = ArrayList<ListFile>()

    @JsonField(name = ["time"])
    var time = ""


}