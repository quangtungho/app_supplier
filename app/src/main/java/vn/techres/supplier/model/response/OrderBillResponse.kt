
package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataOrderBill

class OrderBillResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataOrderBill()
}