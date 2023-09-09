package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class DataContact : Serializable {
    ////GETTER
    @JsonProperty("phone")
    var phone: String = ""

    @JsonProperty("name")
    var name: String = ""

    constructor(phone: String, name: String) {
        this.phone = phone
        this.name = name
    }
}
