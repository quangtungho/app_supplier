package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class FileChat {
    @JsonField(name = ["name_file"])
    var name_file: String? = null

    @JsonField(name = ["link_original"])
    var link_original: String? = null

    @JsonField(name = ["link_thumb"])
    var link_thumb: String? = null

    @JsonField(name = ["size"])
    var size: Long = 0

    @JsonField(name = ["type"])
    var type = 0

    @JsonField(name = ["width"])
    var width = 0

    @JsonField(name = ["height"])
    var height = 0

    @JsonField(name = ["ratio"])
    var ratio = 0f



    constructor(
        name_file: String?,
        link_original: String?,
        link_thumb: String?,
        size: Long,
        type: Int,
        width: Int,
        height: Int
    ) {
        this.name_file = name_file
        this.link_original = link_original
        this.link_thumb = link_thumb
        this.size = size
        this.type = type
        this.width = width
        this.height = height
    }

    constructor(name_file: String?, link_original: String?, link_thumb: String?, size: Long) {
        this.name_file = name_file
        this.link_original = link_original
        this.link_thumb = link_thumb
        this.size = size
    }

    constructor()

    constructor(link_original: String?) {
        this.link_original = link_original
    }


}