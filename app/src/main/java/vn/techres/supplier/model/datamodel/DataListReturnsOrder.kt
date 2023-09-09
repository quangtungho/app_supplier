package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class DataListReturnsOrder : Serializable {
    @JsonField(name = ["limit"])
    var limit: Int = 0

    @JsonField(name = ["list"])
    var list = ArrayList<DataReturnsOrder>()

    @JsonField(name = ["total_record"])
    var total_record: Int = 0
}