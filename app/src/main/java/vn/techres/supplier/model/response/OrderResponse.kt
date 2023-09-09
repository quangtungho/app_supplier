
package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataOrder

class OrderResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataOrder()
}