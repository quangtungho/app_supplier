package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class StickerData {
    @JsonField(name = ["version"])
    var version = 0

    @JsonField(name = ["list_category"])
    var list_category: List<CategorySticker> = ArrayList()
}