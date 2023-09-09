package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.util.ArrayList

@JsonObject
class CategorySticker {
    @JsonField(name = ["_id"])
    var _id = ""

    @JsonField(name = ["name"])
    var name = ""

    @JsonField(name = ["link_original"])
    var link_original = ""

    @JsonField(name = ["id_category"])
    var id_category = 0

    @JsonField(name = ["key_name_category"])
    var key_name_category: List<String> = ArrayList()

    @JsonField(name = ["stickers"])
    var stickers: List<Sticker> = ArrayList()
}