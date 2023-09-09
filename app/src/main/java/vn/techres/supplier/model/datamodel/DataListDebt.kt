package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataListDebt : Serializable {
    @JsonField(name = ["restaurant_id"])
    var restaurant_id: Int = 0

    @JsonField(name = ["restaurant_name"])
    var restaurant_name: String = ""

    @JsonField(name = ["debt_amount"])
    var debt_amount: BigDecimal = BigDecimal.ZERO
}