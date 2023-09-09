package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataOrder : Serializable {
    @JsonProperty("limit")
    var limit: Int? = 0
    @JsonProperty("total_amount")
    var total_amount: BigDecimal? = BigDecimal.ONE

    @JsonProperty("list")
    var list = ArrayList<DataListOrder>()

    @JsonProperty("total_record")
    var total_record: Int? = 0
}