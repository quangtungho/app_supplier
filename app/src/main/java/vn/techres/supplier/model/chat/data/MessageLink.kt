package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class MessageLink {
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

    constructor(media_thumb: String, cannonical_url: String, title: String, description: String) {
        this.media_thumb = media_thumb
        this.cannonical_url = cannonical_url
        this.title = title
        this.description = description
    }

    constructor()


}