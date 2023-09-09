package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class DetailGroup : Serializable {
    @JsonField(name = ["_id"])
     var _id: String? = null

    @JsonField(name = ["number_employee"])
    var number_employee = 0

    @JsonField(name = ["name"])
    var name: String? = null

    @JsonField(name = ["admin_id"])
    var admin_id = 0

    @JsonField(name = ["avatar"])
    var avatar: String? = null

    @JsonField(name = ["restaurant_id"])
    var restaurant_id = 0

    @JsonField(name = ["restaurant_name"])
    var restaurant_name = ""

    @JsonField(name = ["avatar_restaurant"])
    var avatar_restaurant = ""

    @JsonField(name = ["conversation_type"])
    var conversation_type = 0

    @JsonField(name = ["members"])
    var members: ArrayList<Members> = ArrayList()
}