package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class ListGroupOrder : Serializable {
    @JsonProperty("_id")
    var _id: String? = ""

    @JsonProperty("name")
    var name: String? = ""

    @JsonProperty("admin_id")
    var admin_id: Int? = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int? = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int? = 0

    @JsonProperty("avatar_supplier")
    var avatar_supplier: String? = ""

    @JsonProperty("avatar_restaurant")
    var avatar_restaurant: String? = ""

    @JsonProperty("avatar")
    var avatar: String? = ""

    @JsonProperty("background")
    var background: String? = ""

    @JsonProperty("status_last_message")
    var status_last_message: Int? = 0

    @JsonProperty("last_message")
    var last_message: String? = ""

    @JsonProperty("user_name_last_message")
    var user_name_last_message: String? = ""

    @JsonProperty("user_last_message_id")
    var user_last_message_id: Int? = 0

    @JsonProperty("last_message_type")
    var last_message_type: Int? = 0

    @JsonProperty("restaurant_name")
    var restaurant_name: String? = ""

    @JsonProperty("supplier_name")
    var supplier_name: String? = ""

    @JsonProperty("conversation_type")
    var conversation_type: Int? = 0

    @JsonProperty("normalize_name_restaurant")
    var normalize_name_restaurant: String? = ""

    @JsonProperty("normalize_name_supplier")
    var normalize_name_supplier: String? = ""

    @JsonProperty("prefix_restaurant")
    var prefix_restaurant: String? = ""

    @JsonProperty("prefix_supplier")
    var prefix_supplier: String? = ""

    @JsonProperty("member")
    var member = DataMemberOrder()

    @JsonProperty("created_last_message")
    var created_last_message: String? = ""

    @JsonProperty("total_amount")
    var total_amount: Float? = 0.0f

    @JsonProperty("app_name")
    var app_name: String? = ""

}
