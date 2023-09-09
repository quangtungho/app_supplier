package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.WareHouse

class DetailWarehouseResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = WareHouse()

}