package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class DataListOrderRestaurant : Serializable {
    @JsonField(name = ["restaurant_id"])
    var restaurant_id: Int = 0

    @JsonField(name = ["restaurant_name"])
    var restaurant_name: String = ""

    @JsonField(name = ["restaurant_email"])
    var restaurant_email: String = ""

    @JsonField(name = ["restaurant_phone"])
    var restaurant_phone: String = ""

    @JsonField(name = ["restaurant_logo"])
    var restaurant_logo: String = ""

    @JsonField(name = ["restaurant_address"])
    var restaurant_address: String = ""

    @JsonField(name = ["total_order"])
    var total_order: Int = 0
}