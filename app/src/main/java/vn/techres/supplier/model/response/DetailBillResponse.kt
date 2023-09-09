package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataChangeEmployee
import vn.techres.supplier.model.datamodel.DataDetailBill

class DetailBillResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataDetailBill()
}