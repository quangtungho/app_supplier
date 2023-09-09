package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataGroupOrder : Serializable {
    @JsonProperty("list")
    var list = ArrayList<ListGroupOrder>()

    @JsonProperty("total_records")
    var total_records: Int? = 0
}


