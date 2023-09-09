package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.WareHouse
import java.io.Serializable

@JsonObject
class DataWareHouse : Serializable {
    @JsonField(name = ["limit"])
    var limit : Int = 0

    @JsonField(name = ["list"])
    var list = ArrayList<WareHouse>()

    @JsonField(name = ["total_record"])
    var total_record : Int = 0
}