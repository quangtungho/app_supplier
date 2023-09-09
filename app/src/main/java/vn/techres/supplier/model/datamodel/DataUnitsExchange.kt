package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class DataUnitsExchange : Serializable {
    @JsonField(name = ["id"])
    var id: Int = 0

    @JsonField(name = ["name"])
    var name: String = ""
}