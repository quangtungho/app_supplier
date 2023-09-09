package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class ListFile {
    @JsonField(name = ["name_file"])
    var name_file = ""

    @JsonField(name = ["link_original"])
    var link_original = ""

    @JsonField(name = ["link_thumb"])
    var link_thumb = ""

    @JsonField(name = ["size"])
    var size = 0

    @JsonField(name = ["_id"])
    var _id = ""

    @JsonField(name = ["media_thumb"])
    var media_thumb = ""

    @JsonField(name = ["cannonical_url"])
    var cannonical_url = ""

    @JsonField(name = ["title"])
    var title = ""

    @JsonField(name = ["favicon"])
    var favicon = ""

    @JsonField(name = ["description"])
    var description = ""
}