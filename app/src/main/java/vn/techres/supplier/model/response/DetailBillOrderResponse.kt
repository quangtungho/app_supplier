
package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataDetailBillOrder

class DetailBillOrderResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataDetailBillOrder()
}