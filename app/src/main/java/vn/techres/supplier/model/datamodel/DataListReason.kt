package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable


@JsonObject
class DataListReason : Serializable {
    ////GETTER
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("supplier_addition_fee_type")
    var supplier_addition_fee_type: Int = 0

}
