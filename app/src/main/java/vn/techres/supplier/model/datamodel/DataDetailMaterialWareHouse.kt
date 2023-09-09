package vn.techres.supplier.model.datamodel
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import vn.techres.supplier.model.entity.DataWareHouse

import java.io.Serializable

@JsonObject
class   DataDetailMaterialWareHouse : Serializable {
    @JsonProperty("limit")
    var limit: Int = 0

    @JsonProperty("list")
    var list = ArrayList<DataWareHouse>()

    @JsonProperty("total_record")
    var total_record: Int = 0
}
