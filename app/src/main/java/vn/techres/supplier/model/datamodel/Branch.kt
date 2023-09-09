package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class Branch : Serializable {
    ////GETTER
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("restaurant_id")
    var restaurant_id: Int = 0

    @JsonProperty("restaurant_brand_id")
    var restaurant_brand_id: Int = 0

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("street_name")
    var street_name: String = ""

    @JsonProperty("country_name")
    var country_name: String = ""

    @JsonProperty("country_id")
    var country_id: Int = 0

    @JsonProperty("city_id")
    var city_id: Int = 0

    @JsonProperty("district_id")
    var district_id: Int = 0

    @JsonProperty("ward_id")
    var ward_id: Int = 0

    @JsonProperty("address_full_text")
    var address_full_text: String = ""

    @JsonProperty("address_note")
    var address_note: String = ""

    @JsonProperty("phone_number")
    var phone_number: String = ""

    @JsonProperty("lat")
    var lat: String = ""

    @JsonProperty("lng")
    var lng: String = ""


}
