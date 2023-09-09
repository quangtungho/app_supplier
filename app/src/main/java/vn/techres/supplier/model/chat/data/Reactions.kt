package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class Reactions {
    @JsonField(name = ["my_reaction"])
    var my_reaction = 0

    @JsonField(name = ["last_reactions_user"])
    var last_reactions_user = ""

    @JsonField(name = ["last_reactions_id"])
    var last_reactions_id = 0

    @JsonField(name = ["reactions_count"])
    var reactions_count = 0

    @JsonField(name = ["last_reactions"])
    var last_reactions = 0

    @JsonField(name = ["love"])
    var love = 0

    @JsonField(name = ["smile"])
    var smile = 0

    @JsonField(name = ["like"])
    var like = 0

    @JsonField(name = ["angry"])
    var angry = 0

    @JsonField(name = ["sad"])
    var sad = 0

    @JsonField(name = ["wow"])
    var wow = 0
}