package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable


@JsonObject
class DataSupplierOrder : Serializable {
    ////GETTER
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("code")
    var code: String = ""

    @JsonProperty("restaurant_debt_amount")
    var restaurant_debt_amount: Float = 0f

    @JsonProperty("received_at")
    var received_at: String = ""

}
