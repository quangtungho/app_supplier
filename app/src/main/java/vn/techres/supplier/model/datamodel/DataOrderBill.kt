package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataOrderBill : Serializable {
    @JsonProperty("limit")
    var limit: Int? = 0

    @JsonProperty("list")
    var list = ArrayList<DataListOrderBill>()

    @JsonProperty("total_record")
    var total_record: Int? = 0
}