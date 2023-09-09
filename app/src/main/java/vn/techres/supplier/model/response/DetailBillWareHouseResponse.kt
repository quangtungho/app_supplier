package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataDetailBillWareHouse

class DetailBillWareHouseResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataDetailBillWareHouse()
}