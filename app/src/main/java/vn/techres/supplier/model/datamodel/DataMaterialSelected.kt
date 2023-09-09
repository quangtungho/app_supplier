package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import java.io.Serializable

class DataMaterialSelected: Serializable {
    @JsonField(name = ["limit"])
    var limit : Int = 0

    @JsonField(name = ["list"])
    var list = ArrayList<MaterialSelectedWareHouse>()

    @JsonField(name = ["total_record"])
    var total_record : Int = 0
}